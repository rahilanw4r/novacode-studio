package com.pocketide.app.update

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.pocketide.app.BuildConfig
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import org.json.JSONObject

data class AppUpdateInfo(
    val versionCode: Long,
    val versionName: String,
    val apkUrl: String,
    val sha256: String,
    val sizeBytes: Long,
    val notes: String,
)

class AppUpdater(
    private val context: Context,
    /**
     * Optional manifest URL override. When non-empty, used in place of
     * [BuildConfig.APP_UPDATE_MANIFEST_URL]. Debug builds populate this
     * from Settings → Update channel so the update flow can be exercised
     * end-to-end against a Cloudflare Tunnel or ngrok HTTPS URL without
     * publishing a release to GitHub.
     */
    private val manifestUrlOverride: String = "",
) {
    fun check(): AppUpdateInfo? {
        val manifestUrl = manifestUrlOverride.ifBlank { BuildConfig.APP_UPDATE_MANIFEST_URL }
        val info = runCatching { fetchManifest(manifestUrl) }.getOrNull()
        if (info != null) return info

        if (manifestUrlOverride.isBlank()) {
            return runCatching { fetchFromGitHubReleases() }.getOrNull()
        }
        return null
    }

    private fun fetchManifest(manifestUrl: String): AppUpdateInfo? {
        if (!manifestUrl.startsWith("https://")) return null
        val connection = URL(manifestUrl).openConnection() as HttpURLConnection
        return try {
            connection.connectTimeout = 8_000
            connection.readTimeout = 10_000
            connection.instanceFollowRedirects = true
            connection.setRequestProperty("User-Agent", "Pocket-IDE-AppUpdater")
            connection.setRequestProperty("Accept", "application/json")
            val code = connection.responseCode
            if (code !in 200..299) return null
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            parseManifest(body)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseManifest(body: String): AppUpdateInfo? {
        val root = JSONObject(body)
        val versionCode = root.optLong("versionCode")
        if (versionCode <= BuildConfig.VERSION_CODE) return null
        val artifact = root.optJSONObject("artifacts")?.optJSONObject(BuildConfig.APP_VARIANT)
            ?: root.optJSONObject(BuildConfig.APP_VARIANT)
            ?: root
        val url = artifact.optString("url").ifBlank { artifact.optString("apkUrl") }
        if (!url.startsWith("https://")) return null
        return AppUpdateInfo(
            versionCode = versionCode,
            versionName = root.optString("versionName", versionCode.toString()),
            apkUrl = url,
            sha256 = artifact.optString("sha256").lowercase(),
            sizeBytes = artifact.optLong("sizeBytes", -1L),
            notes = root.optString("notes"),
        )
    }

    private fun fetchFromGitHubReleases(): AppUpdateInfo? {
        val apiUrl = "https://api.github.com/repos/rahilanw4r/pocket-ide/releases/latest"
        val connection = URL(apiUrl).openConnection() as HttpURLConnection
        return try {
            connection.connectTimeout = 8_000
            connection.readTimeout = 10_000
            connection.instanceFollowRedirects = true
            connection.setRequestProperty("User-Agent", "Pocket-IDE-AppUpdater")
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json")
            val code = connection.responseCode
            if (code !in 200..299) return null
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            val release = JSONObject(body)
            val assets = release.optJSONArray("assets") ?: return null

            // 1. Try finding pocket-ide-update.json asset directly
            for (i in 0 until assets.length()) {
                val asset = assets.getJSONObject(i)
                if (asset.optString("name").equals("pocket-ide-update.json", ignoreCase = true)) {
                    val downloadUrl = asset.optString("browser_download_url")
                    if (downloadUrl.startsWith("https://")) {
                        val manifestInfo = fetchManifest(downloadUrl)
                        if (manifestInfo != null) return manifestInfo
                    }
                }
            }

            // 2. Direct APK asset fallback
            var apkUrl: String? = null
            var apkSize = -1L
            var apkSha256 = ""
            for (i in 0 until assets.length()) {
                val asset = assets.getJSONObject(i)
                val name = asset.optString("name")
                if (name.endsWith(".apk", ignoreCase = true)) {
                    apkUrl = asset.optString("browser_download_url")
                    apkSize = asset.optLong("size", -1L)
                    val digest = asset.optString("digest")
                    if (digest.startsWith("sha256:")) {
                        apkSha256 = digest.removePrefix("sha256:")
                    }
                    break
                }
            }
            if (apkUrl.isNullOrBlank() || !apkUrl.startsWith("https://")) return null

            val tagName = release.optString("tag_name")
            val releaseNotes = release.optString("body")
            val versionCodeMatch = Regex("""versionCode:\s*(\d+)""").find(releaseNotes)
            val extractedVersionCode = versionCodeMatch?.groupValues?.get(1)?.toLongOrNull() ?: 0L

            if (extractedVersionCode > 0L && extractedVersionCode <= BuildConfig.VERSION_CODE) {
                return null
            }

            val versionName = tagName.removePrefix("v").ifBlank { release.optString("name") }
            AppUpdateInfo(
                versionCode = extractedVersionCode,
                versionName = versionName,
                apkUrl = apkUrl,
                sha256 = apkSha256.lowercase(),
                sizeBytes = apkSize,
                notes = releaseNotes,
            )
        } finally {
            connection.disconnect()
        }
    }

    fun download(info: AppUpdateInfo, progress: (Long, Long) -> Unit): File {
        val directory = File(context.filesDir, "updates").also { it.mkdirs() }
        val partial = File(directory, "pocket-ide-${BuildConfig.APP_VARIANT}.apk.part")
        val target = File(directory, "pocket-ide-${BuildConfig.APP_VARIANT}.apk")
        val connection = URL(info.apkUrl).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.instanceFollowRedirects = true
            val code = connection.responseCode
            check(code in 200..299) { "Update download failed (HTTP $code)" }
            val total = connection.contentLengthLong.takeIf { it > 0 } ?: info.sizeBytes
            connection.inputStream.use { input ->
                partial.outputStream().use { output ->
                    val buffer = ByteArray(128 * 1024)
                    var downloaded = 0L
                    while (true) {
                        val count = input.read(buffer)
                        if (count < 0) break
                        output.write(buffer, 0, count)
                        downloaded += count
                        progress(downloaded, total)
                    }
                }
            }
        } finally {
            connection.disconnect()
        }
        if (info.sha256.isNotBlank()) {
            val actual = sha256(partial)
            check(actual.equals(info.sha256, ignoreCase = true)) { "Downloaded APK failed its SHA-256 verification" }
        }
        verifyApk(partial, info.versionCode)
        if (target.exists()) target.delete()
        check(partial.renameTo(target)) { "Could not prepare the downloaded update" }
        return target
    }

    @Suppress("DEPRECATION")
    private fun verifyApk(apk: File, expectedVersionCode: Long) {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) PackageManager.GET_SIGNING_CERTIFICATES else PackageManager.GET_SIGNATURES
        val archive = context.packageManager.getPackageArchiveInfo(apk.absolutePath, flags)
            ?: error("Downloaded file is not a valid APK")
        check(archive.packageName == context.packageName) { "Update package name does not match Pocket IDE" }
        val archiveVersion = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) archive.longVersionCode else archive.versionCode.toLong()
        check((expectedVersionCode <= 0L || archiveVersion == expectedVersionCode) && archiveVersion > BuildConfig.VERSION_CODE) { "Update version does not match its manifest" }
        val installed = context.packageManager.getPackageInfo(context.packageName, flags)
        val archiveSignatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) archive.signingInfo?.apkContentsSigners else archive.signatures
        val installedSignatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) installed.signingInfo?.apkContentsSigners else installed.signatures
        check(!archiveSignatures.isNullOrEmpty() && !installedSignatures.isNullOrEmpty() &&
            archiveSignatures.map { sha256(it.toByteArray()) }.toSet() == installedSignatures.map { sha256(it.toByteArray()) }.toSet()
        ) { "Update is not signed with the installed app's signing key" }
    }

    private fun sha256(file: File): String = file.inputStream().use { input ->
        val digest = MessageDigest.getInstance("SHA-256")
        val buffer = ByteArray(128 * 1024)
        while (true) {
            val count = input.read(buffer)
            if (count < 0) break
            digest.update(buffer, 0, count)
        }
        digest.digest().joinToString("") { "%02x".format(it) }
    }

    private fun sha256(bytes: ByteArray): String = MessageDigest.getInstance("SHA-256")
        .digest(bytes).joinToString("") { "%02x".format(it) }
}
