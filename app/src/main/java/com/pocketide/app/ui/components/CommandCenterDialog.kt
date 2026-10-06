package com.pocketide.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketCyan
import com.pocketide.app.ui.theme.PocketEmerald
import com.pocketide.app.ui.theme.PocketIndigo
import com.pocketide.app.ui.theme.PocketPrimaryBlue
import com.pocketide.app.ui.theme.PocketPurple
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary

data class CommandActionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val tag: String? = null,
    val action: () -> Unit
)

@Composable
fun CommandCenterDialog(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onLaunchSandbox: () -> Unit,
    onCreateProject: () -> Unit,
    onOpenTerminal: () -> Unit,
    onAskCopilot: () -> Unit,
    onGitClone: () -> Unit,
    onImportZip: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenDeveloper: () -> Unit,
    onRunProject: (() -> Unit)? = null,
) {
    if (!isOpen) return

    var query by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val allActions = remember(onRunProject) {
        buildList {
            if (onRunProject != null) {
                add(
                    CommandActionItem(
                        id = "run",
                        title = "Run Project",
                        subtitle = "Execute current active workspace",
                        icon = Icons.Default.PlayArrow,
                        tag = "Run",
                        action = { onDismissRequest(); onRunProject() }
                    )
                )
            }
            add(
                CommandActionItem(
                    id = "sandbox",
                    title = "Launch Instant Sandbox",
                    subtitle = "Spin up an on-device isolated Linux coding container",
                    icon = Icons.Default.Bolt,
                    tag = "Instant",
                    action = { onDismissRequest(); onLaunchSandbox() }
                )
            )
            add(
                CommandActionItem(
                    id = "new_project",
                    title = "Create New Project",
                    subtitle = "Initialize a clean workspace directory",
                    icon = Icons.Default.CreateNewFolder,
                    action = { onDismissRequest(); onCreateProject() }
                )
            )
            add(
                CommandActionItem(
                    id = "ask_copilot",
                    title = "Ask AI Copilot",
                    subtitle = "Generate code, analyze architecture, or debug",
                    icon = Icons.Default.AutoAwesome,
                    tag = "AI",
                    action = { onDismissRequest(); onAskCopilot() }
                )
            )
            add(
                CommandActionItem(
                    id = "terminal",
                    title = "Open Terminal",
                    subtitle = "Interactive Bash session in Linux environment",
                    icon = Icons.Default.Terminal,
                    tag = "CLI",
                    action = { onDismissRequest(); onOpenTerminal() }
                )
            )
            add(
                CommandActionItem(
                    id = "git_clone",
                    title = "Clone Git Repository",
                    subtitle = "Clone remote repo from GitHub, GitLab, or Git URL",
                    icon = Icons.Default.Code,
                    action = { onDismissRequest(); onGitClone() }
                )
            )
            add(
                CommandActionItem(
                    id = "import_zip",
                    title = "Import ZIP Archive",
                    subtitle = "Unpack and mount a project directory archive",
                    icon = Icons.Default.CloudDownload,
                    action = { onDismissRequest(); onImportZip() }
                )
            )
            add(
                CommandActionItem(
                    id = "settings",
                    title = "Settings & Preferences",
                    subtitle = "Configure AI keys, themes, and runtime specs",
                    icon = Icons.Default.Settings,
                    action = { onDismissRequest(); onOpenSettings() }
                )
            )
            add(
                CommandActionItem(
                    id = "developer",
                    title = "Developer Tools & Profile",
                    subtitle = "Rahil Anwar · Telegram @RahilAnw4r · GitHub",
                    icon = Icons.Default.Person,
                    action = { onDismissRequest(); onOpenDeveloper() }
                )
            )
        }
    }

    val filteredActions = remember(query, allActions) {
        if (query.isBlank()) {
            allActions
        } else {
            allActions.filter {
                it.title.contains(query, ignoreCase = true) ||
                    it.subtitle.contains(query, ignoreCase = true) ||
                    (it.tag?.contains(query, ignoreCase = true) == true)
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (_: Exception) {}
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp),
            color = PocketSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header with search box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⌘", color = PocketPrimaryBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    BasicTextField(
                        value = query,
                        onValueChange = { query = it },
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester),
                        textStyle = TextStyle(
                            color = PocketTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(PocketPrimaryBlue),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (query.isEmpty()) {
                                Text(
                                    text = "Search files, commands and actions…",
                                    color = PocketTextMuted,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    )

                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { query = "" },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = PocketTextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }

                HorizontalDivider(color = PocketBorder)

                // Results list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp)
                        .padding(vertical = 6.dp)
                ) {
                    if (filteredActions.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No matching actions found",
                                    color = PocketTextMuted,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    } else {
                        items(filteredActions, key = { it.id }) { action ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { action.action() }
                                    .padding(horizontal = 14.dp, vertical = 9.dp),
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
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(PocketSurfaceElevated),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = action.icon,
                                            contentDescription = null,
                                            tint = if (action.id == "sandbox" || action.id == "run") PocketEmerald else PocketTextSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = action.title,
                                            color = PocketTextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = action.subtitle,
                                            color = PocketTextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                if (action.tag != null) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(PocketSurfaceElevated)
                                            .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = action.tag,
                                            color = PocketPrimaryBlue,
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
