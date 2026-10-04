package com.novacode.studio.ui.screens.workspace

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Difference
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacode.studio.model.ChatAttachment
import com.novacode.studio.model.ProjectChat
import com.novacode.studio.model.WorkspaceEntry
import com.novacode.studio.ui.AppUiState
import com.novacode.studio.ui.components.NovaGlassCard
import com.novacode.studio.ui.components.NovaStatusPill
import com.novacode.studio.ui.screens.chat.NovaAgentChatScreen
import com.novacode.studio.ui.screens.editor.NovaCodeStudioScreen
import com.novacode.studio.ui.screens.preview.NovaWebPreviewScreen
import com.novacode.studio.ui.screens.terminal.NovaTerminalScreen
import com.novacode.studio.ui.theme.NovaAmber
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaObsidian
import com.novacode.studio.ui.theme.NovaRose
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary

enum class NovaWorkspaceTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    AGENT("Agent", Icons.Default.AutoAwesome),
    CODE_STUDIO("Code", Icons.Default.Code),
    TERMINAL("Terminal", Icons.Default.Terminal),
    PREVIEW("Preview", Icons.Default.Language)
}

@Composable
fun NovaMasterWorkspace(
    state: AppUiState,
    onBack: () -> Unit,
    onSend: (String) -> Unit,
    onStop: () -> Unit,
    onApproval: (Boolean) -> Unit,
    onRefreshFiles: () -> Unit,
    onOpenFile: (WorkspaceEntry) -> Unit,
    onCloseFile: () -> Unit,
    onSaveFile: ((String, String) -> Unit)? = null,
    onUndoChanges: () -> Unit,
    onKeepChanges: () -> Unit,
    onUndoFileChange: (String) -> Unit,
    onKeepFileChange: (String) -> Unit,
    onTerminalRun: (String) -> Unit,
    onTerminalInput: (String) -> Unit,
    onTerminalInterrupt: () -> Unit,
    onTerminalClear: () -> Unit,
    onTerminalConfirm: () -> Unit,
    onTerminalCancel: () -> Unit,
    onAddAttachments: (List<Uri>) -> Unit,
    onRemoveAttachment: (String) -> Unit,
    onBuildAndRunAndroid: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    var currentTab by rememberSaveable { mutableStateOf(NovaWorkspaceTab.AGENT) }

    val attachmentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = onAddAttachments,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Workspace Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurface)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = NovaTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = state.activeProject?.name ?: "Workspace",
                        color = NovaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = state.agentKind.title,
                        color = NovaCyan,
                        fontSize = 10.5.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // On-Device Android Run Button (if android project detected)
                if (state.androidProjectDetected) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaEmerald.copy(alpha = 0.2f))
                            .border(1.dp, NovaEmerald, RoundedCornerShape(8.dp))
                            .clickable(onClick = onBuildAndRunAndroid)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(14.dp))
                            Text("RUN APK", color = NovaEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Status Pill
                NovaStatusPill(
                    statusText = if (state.isRunning) "RUNNING" else "READY",
                    isRunning = state.isRunning,
                    color = if (state.isRunning) NovaCyan else NovaEmerald
                )
            }
        }

        HorizontalDivider(color = NovaBorder)

        // Main Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (currentTab) {
                NovaWorkspaceTab.AGENT -> {
                    NovaAgentChatScreen(
                        messages = state.messages,
                        liveProcess = state.liveProcess,
                        liveThinking = state.liveThinking,
                        liveThinkingSummary = "Reasoning active",
                        liveThinkingTokens = 0,
                        isRunning = state.isRunning,
                        pendingApproval = state.pendingApproval,
                        attachments = state.pendingAttachments,
                        onSend = onSend,
                        onStop = onStop,
                        onApproval = onApproval,
                        onPickAttachment = { attachmentLauncher.launch(arrayOf("*/*")) },
                        onRemoveAttachment = onRemoveAttachment
                    )
                }
                NovaWorkspaceTab.CODE_STUDIO -> {
                    NovaCodeStudioScreen(
                        files = state.workspaceFiles,
                        changes = state.changes,
                        openedFilePath = state.openedFilePath,
                        openedFileContent = state.openedFileContent,
                        isLoadingFile = state.fileContentLoading,
                        onOpenFile = onOpenFile,
                        onCloseFile = onCloseFile,
                        onSaveFile = onSaveFile,
                        onRefreshFiles = onRefreshFiles,
                        onKeepFileChange = onKeepFileChange,
                        onUndoFileChange = onUndoFileChange,
                        onKeepAllChanges = onKeepChanges,
                        onUndoAllChanges = onUndoChanges
                    )
                }
                NovaWorkspaceTab.TERMINAL -> {
                    NovaTerminalScreen(
                        outputLines = state.projectTerminalLines,
                        activeCommand = state.projectTerminalCommand,
                        isProcessRunning = state.projectTerminalRunning,
                        onRunCommand = onTerminalRun,
                        onSendInput = onTerminalInput,
                        onInterrupt = onTerminalInterrupt,
                        onClear = onTerminalClear
                    )
                }
                NovaWorkspaceTab.PREVIEW -> {
                    NovaWebPreviewScreen(
                        initialUrl = "http://localhost:5173"
                    )
                }
            }
        }

        HorizontalDivider(color = NovaBorder)

        // Floating Workspace Dock
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp),
            shape = RoundedCornerShape(20.dp),
            color = NovaSurface,
            border = BorderStroke(1.dp, NovaBorder),
            shadowElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NovaWorkspaceTab.entries.forEach { tab ->
                    val active = tab == currentTab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (active) NovaIndigo.copy(alpha = 0.2f) else Color.Transparent)
                            .border(
                                width = if (active) 1.dp else 0.dp,
                                color = if (active) NovaCyan.copy(alpha = 0.5f) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { currentTab = tab }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label,
                                tint = if (active) NovaCyan else NovaTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            if (active) {
                                Text(
                                    text = tab.label,
                                    color = NovaTextPrimary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Terminal command confirmation dialog
    state.pendingTerminalCommand?.let { cmd ->
        AlertDialog(
            onDismissRequest = onTerminalCancel,
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = NovaAmber) },
            title = { Text("Approve Terminal Command", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("The AI copilot requested to run this command in your isolated Linux workspace:", color = NovaTextSecondary, fontSize = 12.5.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaSurfaceElevated)
                            .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = cmd,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = NovaCyan
                        )
                    }
                }
            },
            confirmButton = {
                NovaPrimaryButton(
                    text = "Approve & Run",
                    onClick = onTerminalConfirm,
                    height = 38.dp
                )
            },
            dismissButton = {
                TextButton(onClick = onTerminalCancel) {
                    Text("Reject", color = NovaRose)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
