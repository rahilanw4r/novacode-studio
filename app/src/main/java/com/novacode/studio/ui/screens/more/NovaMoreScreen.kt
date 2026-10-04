package com.novacode.studio.ui.screens.more

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Terminal
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
import com.novacode.studio.model.AppThemeMode
import com.novacode.studio.ui.AppUiState
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaObsidian
import com.novacode.studio.ui.theme.NovaPurple
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary

@Composable
fun NovaMoreScreen(
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian),
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
                    text = "More",
                    color = NovaTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NovaSurfaceElevated)
                        .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                        .clickable(onClick = onOpenCommandCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⌘", color = NovaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // GENERAL SECTION
        item {
            MoreSection(title = "GENERAL") {
                MoreSettingRow(
                    icon = Icons.Default.DarkMode,
                    iconTint = NovaCyan,
                    title = "Appearance",
                    value = when (state.themeMode) {
                        AppThemeMode.DARK -> "Dark"
                        AppThemeMode.LIGHT -> "Light"
                        AppThemeMode.SYSTEM -> "System"
                    },
                    onClick = onToggleTheme
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.Language,
                    iconTint = NovaIndigo,
                    title = "Language",
                    value = "English",
                    onClick = {}
                )
            }
        }

        // DEVELOPMENT SECTION
        item {
            MoreSection(title = "DEVELOPMENT") {
                MoreSettingRow(
                    icon = Icons.Default.Person,
                    iconTint = NovaEmerald,
                    title = "Developer Tools",
                    value = "Rahil Anwar",
                    onClick = onOpenDeveloper
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.Memory,
                    iconTint = NovaPurple,
                    title = "Linux Runtime",
                    value = "Ubuntu 24.04 ARM64",
                    onClick = { showRuntimeDialog = true }
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.SystemUpdate,
                    iconTint = NovaEmerald,
                    title = "Update Channel",
                    value = state.appUpdate?.let { "v${it.versionName} Available" } ?: "v1.0.10 Up to date",
                    onClick = onInstallUpdate
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.Extension,
                    iconTint = NovaCyan,
                    title = "Extensions & MCP",
                    value = "Custom tools",
                    onClick = { showExtensionsDialog = true }
                )
            }
        }

        // AI SECTION
        item {
            MoreSection(title = "AI") {
                MoreSettingRow(
                    icon = Icons.Default.AutoAwesome,
                    iconTint = NovaEmerald,
                    title = "AI Providers",
                    value = state.agentKind.title,
                    onClick = onOpenAiSettings
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.Psychology,
                    iconTint = NovaIndigo,
                    title = "Default Model",
                    value = if (state.antigravityModel.isNotBlank()) state.antigravityModel.substringAfterLast('-').replaceFirstChar { it.uppercase() } else "High reasoning",
                    onClick = onOpenAiSettings
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.Security,
                    iconTint = NovaCyan,
                    title = "AI Privacy",
                    value = "Local keys",
                    onClick = { showPrivacyDialog = true }
                )
            }
        }

        // ABOUT SECTION
        item {
            MoreSection(title = "ABOUT") {
                MoreSettingRow(
                    icon = Icons.Default.Info,
                    iconTint = NovaIndigo,
                    title = "NovaCode Studio",
                    value = "v1.0.10 (Build 11)",
                    onClick = {}
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.PrivacyTip,
                    iconTint = NovaEmerald,
                    title = "Privacy Policy",
                    value = null,
                    onClick = { showPrivacyDialog = true }
                )
                HorizontalDivider(color = NovaBorder)
                MoreSettingRow(
                    icon = Icons.Default.Code,
                    iconTint = NovaPurple,
                    title = "Open Source Repository",
                    value = "GitHub",
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rahilanw4r"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                )
            }
        }

        // Bottom clearance for floating nav bar
        item {
            Spacer(Modifier.height(72.dp))
        }
    }

    // Runtime Dialog
    if (showRuntimeDialog) {
        AlertDialog(
            onDismissRequest = { showRuntimeDialog = false },
            title = { Text("Linux Runtime Environment", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Architecture: aarch64 (ARM64)", color = NovaTextSecondary, fontSize = 13.sp)
                    Text("Distribution: Ubuntu 24.04 LTS Noble", color = NovaTextSecondary, fontSize = 13.sp)
                    Text("Kernel: Linux on-device isolated sandbox", color = NovaTextSecondary, fontSize = 13.sp)
                    Text("Status: ${if (state.isRunning) "Running active process" else "Online & Ready"}", color = NovaEmerald, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            },
            confirmButton = {
                TextButton(onClick = { showRuntimeDialog = false }) {
                    Text("Close", color = NovaEmerald)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // Privacy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy & Security", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• All development code executes directly on your local device.", color = NovaTextSecondary, fontSize = 13.sp)
                    Text("• API keys and credentials are encrypted using Android Keystore.", color = NovaTextSecondary, fontSize = 13.sp)
                    Text("• No user project files are transmitted to third-party telemetry servers.", color = NovaTextSecondary, fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Got it", color = NovaEmerald)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // Extensions Dialog
    if (showExtensionsDialog) {
        AlertDialog(
            onDismissRequest = { showExtensionsDialog = false },
            title = { Text("Extensions & Tool Ecosystem", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("NovaCode Studio supports MCP (Model Context Protocol) sidecars and Antigravity custom tools for enhanced copilot workflows.", color = NovaTextSecondary, fontSize = 13.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { showExtensionsDialog = false }) {
                    Text("OK", color = NovaEmerald)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(14.dp)
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
            color = NovaTextMuted,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            color = NovaSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, NovaBorder)
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
    iconTint: Color,
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
                    .background(NovaSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            }
            Text(
                text = title,
                color = NovaTextPrimary,
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
                    color = NovaTextMuted,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = NovaTextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
