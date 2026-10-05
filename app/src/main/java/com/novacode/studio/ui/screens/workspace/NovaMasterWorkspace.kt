package com.pocketide.app.ui.screens.workspace

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import com.pocketide.app.ui.components.CommandCenterDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.pocketide.app.ui.components.PocketPrimaryButton
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
import com.pocketide.app.model.ChatAttachment
import com.pocketide.app.model.ProjectChat
import com.pocketide.app.model.WorkspaceEntry
import com.pocketide.app.ui.AppUiState
import com.pocketide.app.ui.components.PocketGlassCard
import com.pocketide.app.ui.components.PocketStatusPill
import com.pocketide.app.ui.screens.chat.PocketAgentChatScreen
import com.pocketide.app.ui.screens.editor.PocketIDEStudioScreen
import com.pocketide.app.ui.screens.preview.PocketWebPreviewScreen
import com.pocketide.app.ui.screens.terminal.PocketTerminalScreen
import com.pocketide.app.ui.theme.PocketAmber
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketCyan
import com.pocketide.app.ui.theme.PocketEmerald
import com.pocketide.app.ui.theme.PocketIndigo
import com.pocketide.app.ui.theme.PocketObsidian
import com.pocketide.app.ui.theme.PocketRose
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary

enum class PocketWorkspaceTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    FILES("Files", Icons.Default.Folder),
    TERMINAL("Terminal", Icons.Default.Terminal),
    AGENT("AI ✦", Icons.Default.AutoAwesome),
    PREVIEW("Preview", Icons.Default.Language)
}

@Composable
fun PocketMasterWorkspace(
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
    var currentTab by rememberSaveable { mutableStateOf(PocketWorkspaceTab.FILES) }
    var showCommandCenter by rememberSaveable { mutableStateOf(false) }
    var workspaceMenuOpen by rememberSaveable { mutableStateOf(false) }

    val attachmentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = onAddAttachments,
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PocketObsidian)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Workspace Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(PocketSurface)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PocketTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = state.activeProject?.name ?: "Workspace",
                        color = PocketTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = state.agentKind.title,
                        color = PocketCyan,
                        fontSize = 10.5.sp,
                        maxLines = 1
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Command Center ⌘
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                        .clickable { showCommandCenter = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text("⌘", color = PocketTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Status Pill
                PocketStatusPill(
                    statusText = if (state.isRunning) "RUNNING" else "READY",
                    isRunning = state.isRunning,
                    color = if (state.isRunning) PocketCyan else PocketEmerald
                )

                // Menu ⋮
                Box {
                    IconButton(onClick = { workspaceMenuOpen = true }, modifier = Modifier.size(30.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = PocketTextSecondary, modifier = Modifier.size(18.dp))
                    }
                    DropdownMenu(
                        expanded = workspaceMenuOpen,
                        onDismissRequest = { workspaceMenuOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Refresh Files") },
                            leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            onClick = {
                                workspaceMenuOpen = false
                                onRefreshFiles()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Undo Last Changes") },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            onClick = {
                                workspaceMenuOpen = false
                                onUndoChanges()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Close Workspace") },
                            leadingIcon = { Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            onClick = {
                                workspaceMenuOpen = false
                                onBack()
                            }
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = PocketBorder)

        // Main Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (currentTab) {
                PocketWorkspaceTab.FILES -> {
                    PocketIDEStudioScreen(
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
                        onUndoAllChanges = onUndoChanges,
                        onAskCopilotContextual = { prompt ->
                            onSend(prompt)
                            currentTab = PocketWorkspaceTab.AGENT
                        }
                    )
                }
                PocketWorkspaceTab.AGENT -> {
                    PocketAgentChatScreen(
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
                PocketWorkspaceTab.TERMINAL -> {
                    PocketTerminalScreen(
                        outputLines = state.projectTerminalLines,
                        activeCommand = state.projectTerminalCommand,
                        isProcessRunning = state.projectTerminalRunning,
                        onRunCommand = onTerminalRun,
                        onSendInput = onTerminalInput,
                        onInterrupt = onTerminalInterrupt,
                        onClear = onTerminalClear
                    )
                }
                PocketWorkspaceTab.PREVIEW -> {
                    PocketWebPreviewScreen(
                        initialUrl = "http://localhost:5173"
                    )
                }
            }
        }

        HorizontalDivider(color = PocketBorder)

        // Floating Workspace Dock: [ Files ] [ Terminal ] [ Run ▶ ] [ AI ✦ ] [ Preview ]
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            shape = RoundedCornerShape(14.dp),
            color = PocketSurface,
            border = BorderStroke(1.dp, PocketBorder),
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Files tab
                val filesActive = currentTab == PocketWorkspaceTab.FILES
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (filesActive) PocketSurfaceElevated else Color.Transparent)
                        .clickable { currentTab = PocketWorkspaceTab.FILES }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Folder, contentDescription = "Files", tint = if (filesActive) PocketEmerald else PocketTextMuted, modifier = Modifier.size(16.dp))
                        Text("Files", color = if (filesActive) PocketEmerald else PocketTextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Terminal tab
                val termActive = currentTab == PocketWorkspaceTab.TERMINAL
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (termActive) PocketSurfaceElevated else Color.Transparent)
                        .clickable { currentTab = PocketWorkspaceTab.TERMINAL }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Terminal, contentDescription = "Terminal", tint = if (termActive) PocketEmerald else PocketTextMuted, modifier = Modifier.size(16.dp))
                        Text("Terminal", color = if (termActive) PocketEmerald else PocketTextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Run Action Button
                Box(
                    modifier = Modifier
                        .weight(1.1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PocketEmerald.copy(alpha = 0.2f))
                        .border(1.dp, PocketEmerald, RoundedCornerShape(8.dp))
                        .clickable {
                            if (state.androidProjectDetected) onBuildAndRunAndroid()
                            else {
                                onTerminalRun("npm run dev 2>/dev/null || python3 main.py 2>/dev/null || cargo run 2>/dev/null || ./run.sh")
                                currentTab = PocketWorkspaceTab.TERMINAL
                            }
                        }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Run", tint = PocketEmerald, modifier = Modifier.size(16.dp))
                        Text("Run ▶", color = PocketEmerald, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // AI ✦ tab
                val agentActive = currentTab == PocketWorkspaceTab.AGENT
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (agentActive) PocketSurfaceElevated else Color.Transparent)
                        .clickable { currentTab = PocketWorkspaceTab.AGENT }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = if (agentActive) PocketEmerald else PocketTextMuted, modifier = Modifier.size(16.dp))
                        Text("AI ✦", color = if (agentActive) PocketEmerald else PocketTextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Preview tab
                val previewActive = currentTab == PocketWorkspaceTab.PREVIEW
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (previewActive) PocketSurfaceElevated else Color.Transparent)
                        .clickable { currentTab = PocketWorkspaceTab.PREVIEW }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Language, contentDescription = "Preview", tint = if (previewActive) PocketEmerald else PocketTextMuted, modifier = Modifier.size(16.dp))
                        Text("Preview", color = if (previewActive) PocketEmerald else PocketTextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    // Command Center Dialog in workspace
    CommandCenterDialog(
        isOpen = showCommandCenter,
        onDismissRequest = { showCommandCenter = false },
        onLaunchSandbox = {},
        onCreateProject = {},
        onOpenTerminal = { currentTab = PocketWorkspaceTab.TERMINAL },
        onAskCopilot = { currentTab = PocketWorkspaceTab.AGENT },
        onGitClone = {},
        onImportZip = {},
        onOpenSettings = {},
        onOpenDeveloper = {},
        onRunProject = {
            if (state.androidProjectDetected) onBuildAndRunAndroid()
            else {
                onTerminalRun("npm run dev 2>/dev/null || python3 main.py 2>/dev/null || cargo run 2>/dev/null || ./run.sh")
                currentTab = PocketWorkspaceTab.TERMINAL
            }
        }
    )

    // Terminal command confirmation dialog
    state.pendingTerminalCommand?.let { cmd ->
        AlertDialog(
            onDismissRequest = onTerminalCancel,
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = PocketAmber) },
            title = { Text("Approve Terminal Command", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("The AI copilot requested to run this command in your isolated Linux workspace:", color = PocketTextSecondary, fontSize = 12.5.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = cmd,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = PocketCyan
                        )
                    }
                }
            },
            confirmButton = {
                PocketPrimaryButton(
                    text = "Approve & Run",
                    onClick = onTerminalConfirm,
                    height = 38.dp
                )
            },
            dismissButton = {
                TextButton(onClick = onTerminalCancel) {
                    Text("Reject", color = PocketRose)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
