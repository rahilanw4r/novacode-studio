package com.pocketide.app.ui.screens.more

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketide.app.BuildConfig
import com.pocketide.app.ui.AppUiState
import com.pocketide.app.ui.theme.AppThemeMode
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketDarkPalette
import com.pocketide.app.ui.theme.PocketPrimaryBlue
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary

@Composable
fun PocketMoreScreen(
    state: AppUiState,
    onToggleTheme: () -> Unit,
    onOpenDeveloper: () -> Unit,
    onOpenAiSettings: () -> Unit,
    onInstallUpdate: () -> Unit,
    onOpenCommandCenter: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showRuntimeDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showExtensionsDialog by remember { mutableStateOf(false) }
    var showMcpDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PocketDarkPalette.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Settings",
                    color = PocketTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp
                )

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                        .clickable(onClick = onOpenCommandCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⌘", color = PocketTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 1. GENERAL SECTION
        item {
            MoreSection(title = "General") {
                MoreSettingRow(
                    icon = Icons.Default.DarkMode,
                    title = "Appearance",
                    value = when (state.themeMode) {
                        AppThemeMode.DARK -> "Dark"
                        AppThemeMode.LIGHT -> "Light"
                        AppThemeMode.SYSTEM -> "System"
                    },
                    onClick = onToggleTheme
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Language,
                    title = "Language",
                    value = "English",
                    onClick = {}
                )
            }
        }

        // 2. DEVELOPMENT SECTION
        item {
            MoreSection(title = "Development") {
                MoreSettingRow(
                    icon = Icons.Default.Person,
                    title = "Developer Tools",
                    value = "Debug & Tools",
                    onClick = onOpenDeveloper
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Terminal,
                    title = "Linux Runtime",
                    value = "PRoot Isolated",
                    onClick = { showRuntimeDialog = true }
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Extension,
                    title = "Extensions",
                    value = "Installed (1)",
                    onClick = { showExtensionsDialog = true }
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Build,
                    title = "MCP / Custom Tools",
                    value = "Configured",
                    onClick = { showMcpDialog = true }
                )
            }
        }

        // 3. AI SECTION
        item {
            MoreSection(title = "AI") {
                MoreSettingRow(
                    icon = Icons.Default.AutoAwesome,
                    title = "Providers",
                    value = state.agentKind.title,
                    onClick = onOpenAiSettings
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Psychology,
                    title = "Models",
                    value = if (state.antigravityModel.isNotBlank()) state.antigravityModel.substringAfterLast('-').replaceFirstChar { it.uppercase() } else "Default",
                    onClick = onOpenAiSettings
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Tune,
                    title = "AI Configuration",
                    value = "Settings",
                    onClick = onOpenAiSettings
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Security,
                    title = "Privacy",
                    value = "Local encrypted keys",
                    onClick = { showPrivacyDialog = true }
                )
            }
        }

        // 4. UPDATES SECTION
        item {
            MoreSection(title = "Updates") {
                MoreSettingRow(
                    icon = Icons.Default.SystemUpdate,
                    title = "Update Channel",
                    value = state.appUpdate?.let { "v${it.versionName} Available" } ?: "v${BuildConfig.VERSION_NAME} Up to date",
                    onClick = onInstallUpdate
                )
            }
        }

        // 5. COMMUNITY SECTION
        item {
            MoreSection(title = "Community & Support") {
                MoreSettingRow(
                    icon = Icons.Default.Language,
                    title = "Telegram Channel",
                    value = "Updates & APKs",
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/PocketIDE"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Language,
                    title = "Community Discussion",
                    value = "Help & Suggestions",
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/PocketIDECommunity"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                )
            }
        }

        // 6. ABOUT SECTION
        item {
            MoreSection(title = "About") {
                MoreSettingRow(
                    icon = Icons.Default.Info,
                    title = "Pocket IDE",
                    value = "v${BuildConfig.VERSION_NAME}",
                    onClick = {}
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Memory,
                    title = "Build Number",
                    value = "${BuildConfig.VERSION_CODE}",
                    onClick = {}
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy",
                    value = null,
                    onClick = { showPrivacyDialog = true }
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Code,
                    title = "Open Source Repository",
                    value = "GitHub",
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rahilanw4r/pocket-ide"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                )
                HorizontalDivider(color = PocketBorder)
                MoreSettingRow(
                    icon = Icons.Default.Person,
                    title = "Creator",
                    value = "Rahil Anwar",
                    onClick = onOpenDeveloper
                )
            }
        }

        // Bottom clearance for nav bar
        item {
            Spacer(Modifier.height(72.dp))
        }
    }

    // Linux Runtime Dialog
    if (showRuntimeDialog) {
        AlertDialog(
            onDismissRequest = { showRuntimeDialog = false },
            title = { Text("Linux Runtime", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Environment: On-device Linux development environment", color = PocketTextSecondary, fontSize = 13.sp)
                    Text("Runtime: Isolated PRoot user-space environment", color = PocketTextSecondary, fontSize = 13.sp)
                    Text("Distribution: Ubuntu 24.04 LTS (Noble Numbat)", color = PocketTextSecondary, fontSize = 13.sp)
                    Text("Architecture: ARM64 (aarch64)", color = PocketTextSecondary, fontSize = 13.sp)
                    Text("Isolation: Sandboxed PRoot rootfs with seccomp filtering", color = PocketTextSecondary, fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showRuntimeDialog = false }) {
                    Text("Done", color = PocketPrimaryBlue)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(10.dp)
        )
    }

    // AI Privacy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy & Security", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• All development code executes directly inside your on-device Linux environment.", color = PocketTextSecondary, fontSize = 13.sp)
                    Text("• API keys and provider tokens are stored in Android Keystore with AES-GCM encryption.", color = PocketTextSecondary, fontSize = 13.sp)
                    Text("• No project telemetry or source code is uploaded to external telemetry services.", color = PocketTextSecondary, fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Done", color = PocketPrimaryBlue)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(10.dp)
        )
    }

    // Extensions Dialog
    if (showExtensionsDialog) {
        AlertDialog(
            onDismissRequest = { showExtensionsDialog = false },
            title = { Text("Extensions", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Installed Extensions:", color = PocketTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("• Antigravity Python Language Support (builtin)", color = PocketTextSecondary, fontSize = 12.5.sp)
                    Text("• Kotlin / Java LSP Support (builtin)", color = PocketTextSecondary, fontSize = 12.5.sp)
                    Text("• Git Integration Provider (builtin)", color = PocketTextSecondary, fontSize = 12.5.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showExtensionsDialog = false }) {
                    Text("Done", color = PocketPrimaryBlue)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(10.dp)
        )
    }

    // MCP / Custom Tools Dialog
    if (showMcpDialog) {
        AlertDialog(
            onDismissRequest = { showMcpDialog = false },
            title = { Text("MCP & Custom Tools", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Model Context Protocol (MCP):", color = PocketTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("• Local stdio sidecars supported for tool execution", color = PocketTextSecondary, fontSize = 12.5.sp)
                    Text("• Filesystem access tool provider enabled", color = PocketTextSecondary, fontSize = 12.5.sp)
                    Text("• Terminal command execution provider enabled", color = PocketTextSecondary, fontSize = 12.5.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showMcpDialog = false }) {
                    Text("Done", color = PocketPrimaryBlue)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
private fun MoreSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            color = PocketTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = PocketSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
private fun MoreSettingRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PocketSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = PocketPrimaryBlue, modifier = Modifier.size(16.dp))
            }
            Text(
                text = title,
                color = PocketTextPrimary,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (value != null) {
                Text(
                    text = value,
                    color = PocketTextSecondary,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = PocketTextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
