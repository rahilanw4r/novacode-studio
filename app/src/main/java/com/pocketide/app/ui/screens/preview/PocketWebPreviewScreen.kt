package com.pocketide.app.ui.screens.preview

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.pocketide.app.model.Project
import com.pocketide.app.runtime.PreviewHealthChecker
import com.pocketide.app.ui.components.PocketGlassCard
import com.pocketide.app.ui.theme.PocketAmber
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketCyan
import com.pocketide.app.ui.theme.PocketEmerald
import com.pocketide.app.ui.theme.PocketIndigo
import com.pocketide.app.ui.theme.PocketObsidian
import com.pocketide.app.ui.theme.PocketPrimaryBlue
import com.pocketide.app.ui.theme.PocketRose
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketSurfaceVariant
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary
import kotlinx.coroutines.delay

enum class DevicePreset(val label: String, val widthDp: Int?, val isDesktop: Boolean = false) {
    RESPONSIVE("Fluid", null, false),
    MOBILE("Mobile", 375, false),
    TABLET("Tablet", 768, false),
    DESKTOP("Desktop", 1280, true),
}

data class ConsoleLogItem(
    val level: ConsoleMessage.MessageLevel,
    val message: String,
    val sourceId: String,
    val lineNumber: Int
)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PocketWebPreviewScreen(
    initialUrl: String? = null,
    project: Project? = null,
    projectWebKind: String? = null,
    devCommand: String? = null,
    staticPort: Int = 0,
    onStartDevServer: ((String) -> Unit)? = null,
    onSwitchToTerminal: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val effectiveDefault = initialUrl?.takeIf { it.isNotBlank() } ?: "http://127.0.0.1:5173"
    var urlInput by remember { mutableStateOf(effectiveDefault) }
    var currentUrl by remember { mutableStateOf(effectiveDefault) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var selectedPreset by remember { mutableStateOf(DevicePreset.RESPONSIVE) }
    var showConsole by remember { mutableStateOf(false) }
    var isConnectionError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isCheckingPort by remember { mutableStateOf(false) }
    val consoleLogs = remember { mutableStateListOf<ConsoleLogItem>() }
    var activePorts by remember { mutableStateOf(setOf<Int>()) }

    val standardPorts = listOf(
        3000 to "Node/Next",
        5000 to "Flask",
        5173 to "Vite",
        5500 to "Live Server",
        8000 to "Python",
        8080 to "Static/Java"
    )

    LaunchedEffect(currentUrl, isConnectionError) {
        val checked = PreviewHealthChecker.checkPorts(ports = listOf(3000, 5000, 5173, 5500, 8000, 8080))
        activePorts = checked
    }

    val currentPort = Regex(""":(\d{2,5})""").find(currentUrl)?.groupValues?.get(1)?.toIntOrNull()

    // Auto-reconnect polling loop: automatically refresh when server port becomes available
    LaunchedEffect(isConnectionError, currentUrl) {
        if (isConnectionError) {
            val portToCheck = currentPort ?: 5173
            while (isConnectionError) {
                delay(2000)
                val checkList = listOf(portToCheck, 3000, 5000, 5173, 5500, 8000, 8080).distinct()
                val checked = PreviewHealthChecker.checkPorts(ports = checkList)
                activePorts = checked
                if (checked.contains(portToCheck)) {
                    isConnectionError = false
                    errorMessage = null
                    webViewRef?.reload()
                    break
                }
            }
        }
    }

    val stackPort = when {
        projectWebKind?.contains("Vite", ignoreCase = true) == true -> 5173
        projectWebKind?.contains("Next", ignoreCase = true) == true -> 3000
        projectWebKind?.contains("Node", ignoreCase = true) == true -> 3000
        projectWebKind?.contains("Python", ignoreCase = true) == true -> 8000
        projectWebKind?.contains("Java", ignoreCase = true) == true -> 8080
        projectWebKind?.contains("Static", ignoreCase = true) == true -> if (staticPort > 0) staticPort else 8080
        else -> null
    }

    LaunchedEffect(initialUrl) {
        if (!initialUrl.isNullOrBlank() && initialUrl != currentUrl) {
            urlInput = initialUrl
            currentUrl = initialUrl
            isConnectionError = false
            webViewRef?.loadUrl(initialUrl)
        }
    }

    fun retryConnection() {
        isCheckingPort = true
        isConnectionError = false
        errorMessage = null
        webViewRef?.reload()
    }

    val defaultDevCommand = devCommand?.takeIf { it.isNotBlank() } ?: when {
        projectWebKind?.contains("Vite", ignoreCase = true) == true -> "npm run dev -- --host 0.0.0.0"
        projectWebKind?.contains("Next", ignoreCase = true) == true -> "npm run dev -- -H 0.0.0.0"
        projectWebKind?.contains("Node", ignoreCase = true) == true -> "npm start"
        projectWebKind?.contains("Python", ignoreCase = true) == true -> "python3 -m http.server 8000 --bind 127.0.0.1"
        projectWebKind?.contains("Java", ignoreCase = true) == true -> "./gradlew bootRun"
        else -> "npm run dev -- --host 0.0.0.0"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PocketObsidian)
    ) {
        // Navigation & Controls Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PocketSurface)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = { webViewRef?.goBack() },
                enabled = webViewRef?.canGoBack() == true,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = PocketTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton(
                onClick = { retryConnection() },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Reload",
                    tint = PocketTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            // URL Bar
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PocketSurfaceElevated)
                    .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    textStyle = TextStyle(
                        color = PocketTextPrimary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    cursorBrush = SolidColor(PocketTextPrimary),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            val trimmed = urlInput.trim()
                            val dest = if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) trimmed else "http://$trimmed"
                            currentUrl = dest
                            isConnectionError = false
                            webViewRef?.loadUrl(dest)
                        }
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            // External Browser
            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                    context.startActivity(intent)
                },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.Default.OpenInBrowser,
                    contentDescription = "Open in Browser",
                    tint = PocketTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Toggle DevTools Console
            IconButton(
                onClick = { showConsole = !showConsole },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.Default.Terminal,
                    contentDescription = "Console",
                    tint = if (showConsole) PocketTextPrimary else PocketTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Quick Ports Bar & Device Presets
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PocketSurface)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Viewport Presets
            DevicePreset.entries.forEach { preset ->
                val active = selectedPreset == preset
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (active) PocketPrimaryBlue else PocketSurfaceElevated)
                        .border(1.dp, if (active) PocketPrimaryBlue else PocketBorder, RoundedCornerShape(6.dp))
                        .clickable { selectedPreset = preset }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = preset.label,
                        color = if (active) Color.White else PocketTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(PocketBorder)
            )

            // Quick Ports
            standardPorts.forEach { (port, name) ->
                val target = "http://127.0.0.1:$port/"
                val isSelected = currentPort == port
                val isAlive = activePorts.contains(port)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isSelected) PocketPrimaryBlue else PocketSurfaceElevated)
                        .border(1.dp, if (isSelected) PocketPrimaryBlue else PocketBorder, RoundedCornerShape(6.dp))
                        .clickable {
                            urlInput = target
                            currentUrl = target
                            isConnectionError = false
                            webViewRef?.loadUrl(target)
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isAlive) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(RoundedCornerShape(2.5.dp))
                                    .background(if (isSelected) Color.White else PocketEmerald)
                            )
                        }
                        Text(
                            text = ":$port ($name)",
                            color = if (isSelected) Color.White else PocketTextSecondary,
                            fontSize = 10.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (isSelected || isAlive) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = PocketBorder)

        // Web Preview Container with Native Viewport Emulation
        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(PocketObsidian),
            contentAlignment = Alignment.TopCenter
        ) {
            val containerWidth = maxWidth
            val screenWidthDp = containerWidth.value.toInt()

            Box(
                modifier = if (selectedPreset == DevicePreset.MOBILE && containerWidth > 385.dp) {
                    Modifier
                        .width(375.dp)
                        .fillMaxHeight()
                        .padding(horizontal = 4.dp, vertical = 6.dp)
                        .border(1.dp, PocketBorder, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                } else {
                    Modifier.fillMaxSize()
                },
                contentAlignment = Alignment.TopCenter
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.allowFileAccess = true
                            settings.allowContentAccess = true
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true
                            settings.setSupportZoom(true)
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false

                            if (selectedPreset.isDesktop) {
                                settings.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                            }

                            webViewClient = object : WebViewClient() {
                                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                    val reqUrl = request?.url?.toString() ?: return false
                                    return if (reqUrl.startsWith("http://localhost") || reqUrl.startsWith("http://127.0.0.1")) {
                                        urlInput = reqUrl
                                        currentUrl = reqUrl
                                        false
                                    } else {
                                        true
                                    }
                                }

                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    isCheckingPort = false
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    isCheckingPort = false
                                    view?.let {
                                        injectEmulationScript(it, selectedPreset, screenWidthDp)
                                    }
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        isConnectionError = true
                                        errorMessage = error?.description?.toString() ?: "Connection refused"
                                        isCheckingPort = false
                                    }
                                }
                            }
                            webChromeClient = object : WebChromeClient() {
                                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                                    consoleMessage?.let {
                                        consoleLogs.add(
                                            ConsoleLogItem(
                                                level = it.messageLevel(),
                                                message = it.message() ?: "",
                                                sourceId = it.sourceId() ?: "",
                                                lineNumber = it.lineNumber()
                                            )
                                        )
                                        if (consoleLogs.size > 100) consoleLogs.removeAt(0)
                                    }
                                    return super.onConsoleMessage(consoleMessage)
                                }
                            }
                            loadUrl(currentUrl)
                            webViewRef = this
                        }
                    },
                    update = { view ->
                        val desktopUa = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                        val targetUa = if (selectedPreset.isDesktop) desktopUa else null
                        val needsUaChange = view.settings.userAgentString != targetUa

                        val lastAppliedPreset = view.getTag(com.pocketide.app.R.id.tag_preview_preset) as? DevicePreset
                        val presetChanged = lastAppliedPreset != selectedPreset
                        view.setTag(com.pocketide.app.R.id.tag_preview_preset, selectedPreset)

                        if (needsUaChange) {
                            view.settings.userAgentString = targetUa
                            view.reload()
                        } else if (presetChanged) {
                            injectEmulationScript(view, selectedPreset, screenWidthDp)
                            view.reload()
                        } else {
                            injectEmulationScript(view, selectedPreset, screenWidthDp)
                        }

                        if (view.url != currentUrl) {
                            view.loadUrl(currentUrl)
                        }
                    }
                )
            }

            // Friendly Dev Server Offline State (replaces ugly ERR_CONNECTION_REFUSED browser page)
            if (isConnectionError) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PocketObsidian.copy(alpha = 0.96f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(PocketSurfaceElevated)
                                .border(1.dp, PocketBorder, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = PocketTextSecondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Text(
                            text = "Server Not Responding",
                            color = PocketTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = currentUrl,
                            color = PocketTextSecondary,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(PocketSurfaceElevated)
                                .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        Text(
                            text = "Cannot connect to the local development server. It may still be compiling or has not been started yet.",
                            color = PocketTextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )

                        if (!projectWebKind.isNullOrBlank()) {
                            Text(
                                text = "Detected Stack: $projectWebKind",
                                color = PocketTextMuted,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        // Smart conflict resolution & active port detection
                        val aliveAlternative = activePorts.firstOrNull { it != currentPort }
                        if (aliveAlternative != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PocketEmerald.copy(alpha = 0.12f))
                                    .border(1.dp, PocketEmerald.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                    .clickable {
                                        val target = "http://127.0.0.1:$aliveAlternative/"
                                        urlInput = target
                                        currentUrl = target
                                        isConnectionError = false
                                        webViewRef?.loadUrl(target)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = PocketEmerald, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "Active server on :$aliveAlternative — Tap to switch",
                                        color = PocketEmerald,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        } else if (stackPort != null && stackPort != currentPort) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PocketSurfaceElevated)
                                    .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                                    .clickable {
                                        val target = "http://127.0.0.1:$stackPort/"
                                        urlInput = target
                                        currentUrl = target
                                        isConnectionError = false
                                        webViewRef?.loadUrl(target)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Stack default :$stackPort ($projectWebKind) — Tap to switch",
                                    color = PocketTextPrimary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        } else if (staticPort > 0 && currentPort != staticPort) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PocketSurfaceElevated)
                                    .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                                    .clickable {
                                        val target = "http://127.0.0.1:$staticPort/"
                                        urlInput = target
                                        currentUrl = target
                                        isConnectionError = false
                                        webViewRef?.loadUrl(target)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Static HTML server :$staticPort — Tap to switch",
                                    color = PocketTextPrimary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Quick switch port pills in error card
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "Try another port:",
                                color = PocketTextMuted,
                                fontSize = 10.5.sp
                            )
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                standardPorts.forEach { (port, name) ->
                                    val target = "http://127.0.0.1:$port/"
                                    val isSelected = currentPort == port
                                    val isAlive = activePorts.contains(port)
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isSelected) PocketTextPrimary else PocketSurfaceElevated)
                                            .border(1.dp, if (isSelected) PocketTextPrimary else PocketBorder, RoundedCornerShape(6.dp))
                                            .clickable {
                                                urlInput = target
                                                currentUrl = target
                                                isConnectionError = false
                                                webViewRef?.loadUrl(target)
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = ":$port",
                                            color = if (isSelected) PocketObsidian else if (isAlive) PocketEmerald else PocketTextPrimary,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = if (isAlive || isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        // Action Buttons
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (onStartDevServer != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PocketTextPrimary)
                                        .clickable {
                                            onStartDevServer(defaultDevCommand)
                                            retryConnection()
                                        }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = PocketObsidian, modifier = Modifier.size(16.dp))
                                        Text(
                                            text = "Start Dev Server ($defaultDevCommand)",
                                            color = PocketObsidian,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (onSwitchToTerminal != null) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(PocketSurfaceElevated)
                                            .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                                            .clickable { onSwitchToTerminal() }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(Icons.Default.Terminal, contentDescription = null, tint = PocketTextSecondary, modifier = Modifier.size(14.dp))
                                            Text(
                                                text = "View Logs",
                                                color = PocketTextPrimary,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PocketSurfaceElevated)
                                        .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                                        .clickable { retryConnection() }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.Refresh, contentDescription = null, tint = PocketTextPrimary, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = "Retry",
                                            color = PocketTextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live DevTools Console Drawer
        AnimatedVisibility(visible = showConsole) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(PocketSurfaceElevated)
                    .border(1.dp, PocketBorder)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Browser Console (${consoleLogs.size})",
                        color = PocketTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Clear",
                            color = PocketTextSecondary,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { consoleLogs.clear() }
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = PocketTextMuted,
                            modifier = Modifier.size(16.dp).clickable { showConsole = false }
                        )
                    }
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(consoleLogs) { log ->
                        val (textColor, prefix) = when (log.level) {
                            ConsoleMessage.MessageLevel.ERROR -> PocketRose to "[ERR]"
                            ConsoleMessage.MessageLevel.WARNING -> PocketAmber to "[WARN]"
                            else -> PocketTextSecondary to "[LOG]"
                        }
                        Text(
                            text = "$prefix ${log.message}",
                            color = textColor,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}

private fun injectEmulationScript(webView: WebView, preset: DevicePreset, screenWidthDp: Int) {
    val targetWidth = preset.widthDp
    val js = """
        (function() {
            try {
                var targetW = ${if (targetWidth != null) targetWidth else "null"};
                var screenW = $screenWidthDp || window.screen.width || 390;
                
                // Clear any intrusive root styles that break position:fixed navbars and stacking contexts
                if (document.documentElement) {
                    document.documentElement.style.zoom = '';
                    document.documentElement.style.width = '';
                    document.documentElement.style.minWidth = '';
                }
                if (document.body) {
                    document.body.style.width = '';
                    document.body.style.minWidth = '';
                }

                var meta = document.querySelector('meta[name="viewport"]');
                if (!meta) {
                    meta = document.createElement('meta');
                    meta.name = 'viewport';
                    document.head.appendChild(meta);
                }

                if (!targetW) {
                    meta.setAttribute('content', 'width=device-width, initial-scale=1.0, user-scalable=yes');
                } else {
                    var scale = 1.0;
                    if (screenW < targetW) {
                        scale = (screenW / targetW);
                    }
                    var scaleStr = scale.toFixed(4);
                    meta.setAttribute('content', 'width=' + targetW + ', initial-scale=' + scaleStr + ', minimum-scale=0.1, maximum-scale=5.0, user-scalable=yes');
                }
                window.dispatchEvent(new Event('resize'));
            } catch (e) {
                console.error('PocketIDE Emulation Error:', e);
            }
        })();
    """.trimIndent()
    webView.evaluateJavascript(js, null)
}
