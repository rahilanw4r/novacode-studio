package com.pocketide.app.runtime

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

enum class ProjectWebKind(val label: String, val defaultPort: Int, val defaultDevCommand: String?) {
    STATIC_HTML("Static Web", 8080, null),
    VITE("Vite Dev Server", 5173, "npm run dev -- --host 0.0.0.0"),
    NEXT_JS("Next.js Server", 3000, "npm run dev -- -H 0.0.0.0"),
    NODE("Node.js Server", 3000, "npm start"),
    PYTHON("Python HTTP", 8000, "python3 -m http.server 8000 --bind 127.0.0.1"),
    JAVA("Java Web App", 8080, "./gradlew bootRun"),
    UNKNOWN("Web Server", 5173, "npm run dev -- --host 0.0.0.0")
}

object ProjectWebDetector {
    fun hasStaticHtml(rootDir: File): Boolean {
        if (!rootDir.isDirectory) return false
        return File(rootDir, "index.html").isFile ||
            File(rootDir, "public/index.html").isFile ||
            File(rootDir, "dist/index.html").isFile ||
            File(rootDir, "src/index.html").isFile
    }

    fun detectCustomPort(rootDir: File): Int? {
        if (!rootDir.isDirectory) return null
        val candidateFiles = listOf("server.js", "app.js", "index.js", "main.py", "app.py")
        for (name in candidateFiles) {
            val f = File(rootDir, name)
            if (f.isFile) {
                val text = runCatching { f.readText() }.getOrNull() ?: continue
                val match = Regex("""(?:listen\s*\(\s*|PORT\s*=\s*|PORT\s*\|\|\s*|port\s*=\s*)(\d{2,5})""").find(text)
                if (match != null) {
                    val p = match.groupValues[1].toIntOrNull()
                    if (p != null && p in 1..65535) return p
                }
            }
        }
        return null
    }

    fun resolveDevCommand(rootDir: File): String {
        if (!rootDir.isDirectory) return "npm run dev -- --host 0.0.0.0"
        val packageJson = File(rootDir, "package.json")
        if (packageJson.isFile) {
            val content = runCatching { packageJson.readText() }.getOrDefault("")
            if (content.contains("\"vite\"")) return "npm run dev -- --host 0.0.0.0"
            if (content.contains("\"next\"")) return "npm run dev -- -H 0.0.0.0"
            if (content.contains("\"dev\"")) return "npm run dev -- --host 0.0.0.0"
            if (content.contains("\"start\"")) return "npm start"
        }
        if (File(rootDir, "server.js").isFile) return "node server.js"
        if (File(rootDir, "app.js").isFile) return "node app.js"
        if (File(rootDir, "index.js").isFile) return "node index.js"
        if (File(rootDir, "main.py").isFile) return "python3 main.py"
        if (File(rootDir, "app.py").isFile) return "python3 app.py"
        if (File(rootDir, "requirements.txt").isFile) return "python3 -m http.server 8000 --bind 127.0.0.1"
        if (File(rootDir, "pom.xml").isFile) return "./mvnw spring-boot:run"
        if (File(rootDir, "build.gradle").isFile || File(rootDir, "build.gradle.kts").isFile) return "./gradlew bootRun"
        return "npm run dev -- --host 0.0.0.0"
    }

    fun detect(rootDir: File): ProjectWebKind {
        if (!rootDir.isDirectory) return ProjectWebKind.UNKNOWN

        val packageJson = File(rootDir, "package.json")
        if (packageJson.isFile) {
            val content = runCatching { packageJson.readText() }.getOrDefault("")
            if (content.contains("\"vite\"")) return ProjectWebKind.VITE
            if (content.contains("\"next\"")) return ProjectWebKind.NEXT_JS
            return ProjectWebKind.NODE
        }

        // Detect node entrypoint files even without package.json
        if (File(rootDir, "server.js").isFile ||
            File(rootDir, "app.js").isFile ||
            File(rootDir, "index.js").isFile
        ) {
            return ProjectWebKind.NODE
        }

        // Static HTML: has index.html at root, public/, dist/, or src/
        if (hasStaticHtml(rootDir)) {
            return ProjectWebKind.STATIC_HTML
        }

        // Python
        if (File(rootDir, "app.py").isFile ||
            File(rootDir, "main.py").isFile ||
            File(rootDir, "requirements.txt").isFile
        ) {
            return ProjectWebKind.PYTHON
        }

        // Java
        if (File(rootDir, "pom.xml").isFile ||
            File(rootDir, "build.gradle").isFile ||
            File(rootDir, "build.gradle.kts").isFile
        ) {
            return ProjectWebKind.JAVA
        }

        return ProjectWebKind.UNKNOWN
    }
}

/**
 * Lightweight, private loopback HTTP server to serve static project files (HTML/CSS/JS/assets)
 * directly in Android WebView without requiring Node.js or PRoot services.
 */
class LocalStaticServer(
    private val preferredPort: Int = 8080
) {
    private var serverSocket: ServerSocket? = null
    private val threadPool = Executors.newCachedThreadPool()
    @Volatile private var isRunning = false
    @Volatile private var currentServingDir: File? = null

    val port: Int
        get() = serverSocket?.localPort ?: preferredPort

    val isServerRunning: Boolean
        get() = isRunning && serverSocket != null && !(serverSocket?.isClosed ?: true)

    @Synchronized
    fun startServing(directory: File): Int {
        currentServingDir = directory.canonicalFile
        if (isServerRunning) {
            return port
        }

        return try {
            val ss = ServerSocket()
            ss.reuseAddress = true
            // Try preferred port, otherwise let system pick an ephemeral loopback port
            val bound = runCatching {
                ss.bind(InetSocketAddress("127.0.0.1", preferredPort))
                true
            }.getOrDefault(false)

            if (!bound) {
                ss.bind(InetSocketAddress("127.0.0.1", 0))
            }

            serverSocket = ss
            isRunning = true

            threadPool.execute {
                while (isRunning) {
                    try {
                        val client = ss.accept()
                        threadPool.execute { handleClientConnection(client) }
                    } catch (_: Exception) {
                        if (!isRunning) break
                    }
                }
            }
            ss.localPort
        } catch (e: Exception) {
            isRunning = false
            serverSocket = null
            0
        }
    }

    @Synchronized
    fun stop() {
        isRunning = false
        runCatching { serverSocket?.close() }
        serverSocket = null
        currentServingDir = null
    }

    private fun handleClientConnection(socket: Socket) {
        try {
            socket.soTimeout = 5000
            val input = socket.getInputStream()
            val output = BufferedOutputStream(socket.getOutputStream())

            val reader = input.bufferedReader()
            val requestLine = reader.readLine() ?: return
            val parts = requestLine.split(" ")
            if (parts.size < 2) return

            val method = parts[0].uppercase()
            var rawPath = parts[1].substringBefore('?').substringBefore('#')
            rawPath = URLDecoder.decode(rawPath, "UTF-8")

            val baseDir = currentServingDir ?: return
            var targetFile = File(baseDir, rawPath.removePrefix("/")).canonicalFile

            // Directory traversal prevention
            if (!targetFile.toPath().startsWith(baseDir.toPath())) {
                sendError(output, 403, "Forbidden")
                return
            }

            if (targetFile.isDirectory) {
                val indexHtml = File(targetFile, "index.html")
                if (indexHtml.isFile) {
                    targetFile = indexHtml
                }
            }

            if (!targetFile.exists() || targetFile.isDirectory) {
                // Check if public/ or dist/ holds it
                val publicFallback = File(baseDir, "public/$rawPath".removePrefix("/")).canonicalFile
                val distFallback = File(baseDir, "dist/$rawPath".removePrefix("/")).canonicalFile
                targetFile = when {
                    publicFallback.isFile && publicFallback.toPath().startsWith(baseDir.toPath()) -> publicFallback
                    distFallback.isFile && distFallback.toPath().startsWith(baseDir.toPath()) -> distFallback
                    else -> targetFile
                }
            }

            if (!targetFile.isFile) {
                sendError(output, 404, "Not Found")
                return
            }

            val mimeType = getMimeType(targetFile.name)
            val fileLength = targetFile.length()

            val header = buildString {
                append("HTTP/1.1 200 OK\r\n")
                append("Content-Type: $mimeType\r\n")
                append("Content-Length: $fileLength\r\n")
                append("Access-Control-Allow-Origin: *\r\n")
                append("Cache-Control: no-cache, no-store, must-revalidate\r\n")
                append("Connection: close\r\n\r\n")
            }
            output.write(header.toByteArray(Charsets.UTF_8))

            if (method == "GET") {
                FileInputStream(targetFile).use { fis ->
                    fis.copyTo(output, bufferSize = 8192)
                }
            }
            output.flush()
        } catch (_: Exception) {
            // Socket or client connection closed
        } finally {
            runCatching { socket.close() }
        }
    }

    private fun sendError(output: BufferedOutputStream, statusCode: Int, statusText: String) {
        val body = "<html><body><h1>$statusCode $statusText</h1></body></html>"
        val header = "HTTP/1.1 $statusCode $statusText\r\n" +
            "Content-Type: text/html; charset=utf-8\r\n" +
            "Content-Length: ${body.length}\r\n" +
            "Connection: close\r\n\r\n"
        output.write(header.toByteArray(Charsets.UTF_8))
        output.write(body.toByteArray(Charsets.UTF_8))
        output.flush()
    }

    private fun getMimeType(fileName: String): String {
        val ext = fileName.substringAfterLast('.', "").lowercase()
        return when (ext) {
            "html", "htm" -> "text/html; charset=utf-8"
            "js", "mjs" -> "application/javascript; charset=utf-8"
            "css" -> "text/css; charset=utf-8"
            "json" -> "application/json; charset=utf-8"
            "svg" -> "image/svg+xml"
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            "gif" -> "image/gif"
            "ico" -> "image/x-icon"
            "webp" -> "image/webp"
            "woff" -> "font/woff"
            "woff2" -> "font/woff2"
            "ttf" -> "font/ttf"
            "wasm" -> "application/wasm"
            "txt", "md" -> "text/plain; charset=utf-8"
            "mp3" -> "audio/mpeg"
            "wav" -> "audio/wav"
            "ogg" -> "audio/ogg"
            "mp4" -> "video/mp4"
            else -> "application/octet-stream"
        }
    }
}

/**
 * Socket-based loopback health check to verify if a server is accepting connections on a given port.
 */
object PreviewHealthChecker {
    suspend fun isReachable(host: String = "127.0.0.1", port: Int, timeoutMs: Int = 1200): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(host, port), timeoutMs)
                socket.close()
                true
            } catch (_: Exception) {
                false
            }
        }

    suspend fun checkPorts(host: String = "127.0.0.1", ports: List<Int>, timeoutMs: Int = 300): Set<Int> =
        withContext(Dispatchers.IO) {
            ports.filter { port -> isReachable(host, port, timeoutMs) }.toSet()
        }

    suspend fun findFirstReachablePort(host: String = "127.0.0.1", ports: List<Int>, timeoutMs: Int = 300): Int? =
        withContext(Dispatchers.IO) {
            ports.firstOrNull { port -> isReachable(host, port, timeoutMs) }
        }
}
