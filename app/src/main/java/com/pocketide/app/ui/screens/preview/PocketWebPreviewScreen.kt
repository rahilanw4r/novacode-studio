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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
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

enum class DevicePreset(val label: String, val widthDp: Int?) {
    RESPONSIVE("Fluid", null),
    MOBILE("Mobile (375px)", 375),
    TABLET("Tablet (768px)", 768),
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
                    tint = PocketCyan,
                    modifier = Modifier.size(16.dp)
                )
            }

            // URL Bar
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PocketSurfaceElevated)
                    .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
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
                    cursorBrush = SolidColor(PocketCyan),
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
                    tint = PocketIndigo,
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
                    tint = if (showConsole) PocketEmerald else PocketTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Quick Ports Bar & Device Presets
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PocketSurfaceVariant)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Viewport Presets
            DevicePreset.entries.forEach { preset ->
                val active = selectedPreset == preset
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (active) PocketCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .border(1.dp, if (active) PocketCyan else Color.Transparent, RoundedCornerShape(6.dp))
                        .clickable { selectedPreset = preset }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = preset.label,
                        color = if (active) PocketCyan else PocketTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(14.dp)
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
                        .background(
                            when {
                                isSelected -> PocketIndigo.copy(alpha = 0.28f)
                                isAlive -> PocketEmerald.copy(alpha = 0.15f)
                                else -> PocketSurfaceElevated
                            }
                        )
                        .border(
                            1.dp,
                            when {
                                isSelected -> PocketIndigo
                                isAlive -> PocketEmerald.copy(alpha = 0.4f)
                                else -> Color.Transparent
                            },
                            RoundedCornerShape(6.dp)
                        )
                        .clickable {
                            urlInput = target
                            currentUrl = target
                            isConnectionError = false
                            webViewRef?.loadUrl(target)
                        }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
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
                                    .background(PocketEmerald)
                            )
                        }
                        Text(
                            text = ":$port ($name)",
                            color = when {
                                isSelected -> PocketIndigo
                                isAlive -> PocketEmerald
                                else -> PocketTextSecondary
                            },
                            fontSize = 10.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (isSelected || isAlive) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = PocketBorder)

        // Web Preview Container
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            val frameModifier = if (selectedPreset.widthDp != null) {
                Modifier
                    .width(selectedPreset.widthDp!!.dp)
                    .fillMaxHeight()
                    .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
            } else {
                Modifier.fillMaxSize()
            }

            AndroidView(
                modifier = frameModifier,
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = true
                        settings.allowContentAccess = true
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
                    if (view.url != currentUrl) {
                        view.loadUrl(currentUrl)
                    }
                }
            )

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
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(PocketAmber.copy(alpha = 0.12f))
                                .border(1.dp, PocketAmber.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = PocketAmber,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Text(
                            text = "Server Not Responding",
                            color = PocketTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = currentUrl,
                            color = PocketCyan,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PocketSurfaceElevated)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )

                        Text(
                            text = "Cannot connect to the local development server. It may still be compiling or has not been started yet.",
                            color = PocketTextSecondary,
                            fontSize = 12.5.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 17.sp
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
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PocketEmerald.copy(alpha = 0.15f))
                                    .border(1.dp, PocketEmerald.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
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
                                    Icon(Icons.Default.Refresh, contentDescription = null, tint = PocketEmerald, modifier = Modifier.size(15.dp))
                                    Text(
                                        text = "⚡ Active server detected on :$aliveAlternative! Tap to switch",
                                        color = PocketEmerald,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        } else if (stackPort != null && stackPort != currentPort) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PocketIndigo.copy(alpha = 0.15f))
                                    .border(1.dp, PocketIndigo.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
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
                                    text = "💡 Stack default is :$stackPort ($projectWebKind). Tap to switch",
                                    color = PocketIndigo,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        } else if (staticPort > 0 && currentPort != staticPort) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PocketCyan.copy(alpha = 0.15f))
                                    .border(1.dp, PocketCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
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
                                    text = "📄 Static HTML preview available on :$staticPort. Tap to view",
                                    color = PocketCyan,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
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
                                            .background(if (isSelected) PocketIndigo.copy(alpha = 0.3f) else PocketSurfaceElevated)
                                            .border(1.dp, if (isAlive) PocketEmerald else PocketBorder, RoundedCornerShape(6.dp))
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
                                            color = if (isAlive) PocketEmerald else PocketTextPrimary,
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
                                        .background(PocketPrimaryBlue)
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
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Text(
                                            text = "Start Dev Server ($defaultDevCommand)",
                                            color = Color.White,
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
                                        Icon(Icons.Default.Refresh, contentDescription = null, tint = PocketCyan, modifier = Modifier.size(14.dp))
                                        Text(
                                            text = "Retry",
                                            color = PocketCyan,
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
                            color = PocketCyan,
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
