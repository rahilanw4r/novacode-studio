package com.novacode.studio.ui.screens.preview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.novacode.studio.ui.components.NovaGlassCard
import com.novacode.studio.ui.theme.*

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
fun NovaWebPreviewScreen(
    initialUrl: String = "http://localhost:5173",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var urlInput by remember { mutableStateOf(initialUrl) }
    var currentUrl by remember { mutableStateOf(initialUrl) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var selectedPreset by remember { mutableStateOf(DevicePreset.RESPONSIVE) }
    var showConsole by remember { mutableStateOf(false) }
    val consoleLogs = remember { mutableStateListOf<ConsoleLogItem>() }

    val quickPorts = listOf("5173" to "Vite", "3000" to "Next", "8000" to "Python", "8080" to "Java")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian)
    ) {
        // Navigation & Controls Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurface)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IconButton(
                onClick = { webViewRef?.goBack() },
                enabled = webViewRef?.canGoBack() == true,
                modifier = Modifier.size(30.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NovaTextSecondary, modifier = Modifier.size(16.dp))
            }
            IconButton(
                onClick = { webViewRef?.reload() },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reload", tint = NovaCyan, modifier = Modifier.size(16.dp))
            }

            // URL Bar
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NovaSurfaceElevated)
                    .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = urlInput,
                    onValueChange = { urlInput = it },
                    textStyle = TextStyle(
                        color = NovaTextPrimary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    ),
                    cursorBrush = SolidColor(NovaCyan),
                    singleLine = true,
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
                Icon(Icons.Default.OpenInBrowser, contentDescription = "Open in Browser", tint = NovaIndigo, modifier = Modifier.size(18.dp))
            }

            // Toggle DevTools Console
            IconButton(
                onClick = { showConsole = !showConsole },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    Icons.Default.Terminal,
                    contentDescription = "Console",
                    tint = if (showConsole) NovaEmerald else NovaTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Quick Ports Bar & Device Presets
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurfaceVariant)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Presets
            DevicePreset.entries.forEach { preset ->
                val active = selectedPreset == preset
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (active) NovaCyan.copy(alpha = 0.2f) else Color.Transparent)
                        .border(1.dp, if (active) NovaCyan else Color.Transparent, RoundedCornerShape(6.dp))
                        .clickable { selectedPreset = preset }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = preset.label,
                        color = if (active) NovaCyan else NovaTextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quick Ports
            quickPorts.forEach { (port, name) ->
                val target = "http://localhost:$port"
                val active = currentUrl.contains(":$port")
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (active) NovaIndigo.copy(alpha = 0.25f) else NovaSurfaceElevated)
                        .clickable {
                            urlInput = target
                            currentUrl = target
                            webViewRef?.loadUrl(target)
                        }
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = ":$port ($name)",
                        color = if (active) NovaIndigo else NovaTextSecondary,
                        fontSize = 10.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        HorizontalDivider(color = NovaBorder)

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
                    .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
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
        }

        // Live DevTools Console Drawer
        AnimatedVisibility(visible = showConsole) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(NovaSurfaceElevated)
                    .border(1.dp, NovaBorder)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Browser Console (${consoleLogs.size})",
                        color = NovaTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Clear",
                            color = NovaCyan,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { consoleLogs.clear() }
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = NovaTextMuted,
                            modifier = Modifier.size(16.dp).clickable { showConsole = false }
                        )
                    }
                }
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(consoleLogs) { log ->
                        val (textColor, prefix) = when (log.level) {
                            ConsoleMessage.MessageLevel.ERROR -> NovaRose to "[ERR]"
                            ConsoleMessage.MessageLevel.WARNING -> NovaAmber to "[WARN]"
                            else -> NovaTextSecondary to "[LOG]"
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
