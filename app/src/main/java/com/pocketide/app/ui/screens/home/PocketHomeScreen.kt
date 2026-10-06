package com.pocketide.app.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketide.app.R
import com.pocketide.app.model.Project
import com.pocketide.app.ui.AppUiState
import com.pocketide.app.ui.components.PocketPrimaryButton
import com.pocketide.app.ui.components.PocketSecondaryButton
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketDarkPalette
import com.pocketide.app.ui.theme.PocketPrimaryBlue
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary


@Composable
fun PocketHomeScreen(
    state: AppUiState,
    listState: LazyListState,
    onOpenProject: (Project) -> Unit,
    onCreateProject: (String) -> Unit,
    onCreateQuickProject: () -> Unit,
    onViewAllProjects: () -> Unit,
    onOpenCommandCenter: () -> Unit,
    onOpenDeveloper: () -> Unit,
    onAskCopilot: (String) -> Unit,
    modifier: Modifier = Modifier,
    onOpenTerminal: () -> Unit = {},
    onOpenMore: () -> Unit = onOpenDeveloper,
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var copilotPrompt by remember { mutableStateOf("") }

    val recentProjects = remember(state.projects) {
        state.projects.take(4)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(PocketDarkPalette.background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Clean Action Header: Pocket IDE, Search, More
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "Pocket IDE",
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                    )
                    Text(
                        text = "Pocket IDE",
                        color = PocketTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.2).sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search / Command Center
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                            .clickable(onClick = onOpenCommandCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = PocketTextSecondary,
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    // More
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                            .clickable(onClick = onOpenMore),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = PocketTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // 2. Hero Headline & Description (Developer-focused, ChatGPT aesthetic)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Build from your phone",
                    color = PocketTextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Chat with AI agents, edit code, run terminal commands, and preview your apps on the go.",
                    color = PocketTextSecondary,
                    fontSize = 13.5.sp,
                    lineHeight = 19.sp
                )
            }
        }

        // 3. Main Action Buttons: + New Project & Quick Project
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PocketPrimaryButton(
                    text = "New Project",
                    icon = Icons.Default.Add,
                    onClick = {
                        newProjectName = ""
                        showCreateDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    height = 44.dp
                )
                PocketSecondaryButton(
                    text = "Open Project",
                    icon = Icons.Default.FolderOpen,
                    onClick = onViewAllProjects,
                    modifier = Modifier.weight(1f),
                    height = 44.dp
                )
            }
        }

        // 4. Import / Existing Project Helper Card
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onViewAllProjects),
                shape = RoundedCornerShape(12.dp),
                color = PocketSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Bring an existing project",
                            color = PocketTextPrimary,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Import ZIP, Git repository, or clone workspace",
                            color = PocketTextSecondary,
                            fontSize = 11.5.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Import",
                        tint = PocketTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // 5. Recent Projects
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recent Projects",
                        color = PocketTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (state.projects.isNotEmpty()) {
                        Text(
                            text = "See all (${state.projects.size})",
                            color = PocketPrimaryBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable(onClick = onViewAllProjects)
                        )
                    }
                }

                if (recentProjects.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = PocketSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderOpen,
                                contentDescription = null,
                                tint = PocketTextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "No recent projects yet",
                                color = PocketTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Create a new project or bring an existing one to get started.",
                                color = PocketTextSecondary,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        recentProjects.forEach { project ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onOpenProject(project) },
                                shape = RoundedCornerShape(12.dp),
                                color = PocketSurface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(PocketSurfaceElevated),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Folder,
                                                contentDescription = null,
                                                tint = PocketPrimaryBlue,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(
                                                text = project.name,
                                                color = PocketTextPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = if (project.rootPath.isNotBlank()) project.rootPath else "/workspace/${project.slug}",
                                                color = PocketTextMuted,
                                                fontSize = 11.5.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontFamily = FontFamily.Monospace
                                            )
                                            Text(
                                                text = "${project.language.ifBlank { "General" }} · ${project.formattedUpdatedAt}",
                                                color = PocketTextSecondary,
                                                fontSize = 11.sp,
                                                maxLines = 1
                                            )
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Open",
                                        tint = PocketTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom clearance for nav bar
        item {
            Spacer(Modifier.height(72.dp))
        }
    }

    // Create New Project Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("New Project", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newProjectName,
                    onValueChange = { newProjectName = it },
                    label = { Text("Project Name") },
                    placeholder = { Text("my-new-app") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = PocketTextPrimary,
                        unfocusedTextColor = PocketTextPrimary,
                        focusedBorderColor = PocketPrimaryBlue,
                        unfocusedBorderColor = PocketBorder,
                        focusedLabelColor = PocketPrimaryBlue,
                        unfocusedLabelColor = PocketTextSecondary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                PocketPrimaryButton(
                    text = "Create",
                    onClick = {
                        val name = newProjectName.trim()
                        if (name.isNotBlank()) {
                            onCreateProject(name)
                            showCreateDialog = false
                        }
                    },
                    height = 38.dp
                )
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = PocketTextSecondary)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(10.dp)
        )
    }
}


