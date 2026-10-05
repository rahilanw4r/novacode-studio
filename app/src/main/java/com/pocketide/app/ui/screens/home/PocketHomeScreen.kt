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

data class StarterTemplate(
    val title: String,
    val tech: String,
    val badge: String,
    val icon: ImageVector,
    val defaultName: String
)

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

    val starterTemplates = remember {
        listOf(
            StarterTemplate("Android", "Kotlin · Compose", "APP", Icons.Default.Android, "android-app"),
            StarterTemplate("React + Vite", "TypeScript · SPA", "WEB", Icons.Default.Code, "vite-react-app"),
            StarterTemplate("FastAPI", "Python · REST", "API", Icons.Default.Terminal, "fastapi-service"),
            StarterTemplate("Python", "Scripting · CLI", "PY", Icons.Default.Terminal, "python-script"),
            StarterTemplate("Node.js", "Express · REST", "NODE", Icons.Default.Code, "node-express-api")
        )
    }

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

        // 2. Main Actions: + New Project & Open Project
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
                    height = 42.dp
                )
                PocketSecondaryButton(
                    text = "Open Project",
                    icon = Icons.Default.FolderOpen,
                    onClick = onViewAllProjects,
                    modifier = Modifier.weight(1f),
                    height = 42.dp
                )
            }
        }

        // 3. Recent Projects
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recent Projects",
                        color = PocketTextPrimary,
                        fontSize = 13.sp,
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
                        shape = RoundedCornerShape(8.dp),
                        color = PocketSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "No Projects Yet",
                                color = PocketTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                PocketPrimaryButton(
                                    text = "New Project",
                                    icon = Icons.Default.Add,
                                    onClick = {
                                        newProjectName = ""
                                        showCreateDialog = true
                                    },
                                    height = 36.dp
                                )
                                PocketSecondaryButton(
                                    text = "Open Project",
                                    icon = Icons.Default.FolderOpen,
                                    onClick = onViewAllProjects,
                                    height = 36.dp
                                )
                            }
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = PocketSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            recentProjects.forEachIndexed { index, project ->
                                if (index > 0) {
                                    HorizontalDivider(color = PocketBorder)
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onOpenProject(project) }
                                        .padding(horizontal = 14.dp, vertical = 11.dp),
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
                                            Icon(
                                                imageVector = Icons.Default.Folder,
                                                contentDescription = null,
                                                tint = PocketPrimaryBlue,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(
                                                text = project.name,
                                                color = PocketTextPrimary,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${project.language.ifBlank { "Android" }} · Updated ${project.formattedUpdatedAt}",
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
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Quick Start
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Quick Start",
                    color = PocketTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    starterTemplates.forEach { template ->
                        Surface(
                            modifier = Modifier
                                .width(136.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onCreateProject(template.defaultName) },
                            shape = RoundedCornerShape(8.dp),
                            color = PocketSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(
                                        imageVector = template.icon,
                                        contentDescription = null,
                                        tint = PocketPrimaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(PocketSurfaceElevated)
                                            .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = template.badge,
                                            color = PocketTextSecondary,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                                Text(
                                    text = template.title,
                                    color = PocketTextPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = template.tech,
                                    color = PocketTextSecondary,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        // 5. Tools (Compact)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Tools",
                    color = PocketTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HomeToolItem(
                        title = "Terminal",
                        icon = Icons.Default.Terminal,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenTerminal
                    )
                    HomeToolItem(
                        title = "Git",
                        icon = Icons.AutoMirrored.Filled.CallSplit,
                        modifier = Modifier.weight(1f),
                        onClick = onViewAllProjects
                    )
                    HomeToolItem(
                        title = "Files",
                        icon = Icons.Default.Folder,
                        modifier = Modifier.weight(1f),
                        onClick = onViewAllProjects
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HomeToolItem(
                        title = "Packages",
                        icon = Icons.Default.Widgets,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenTerminal
                    )
                    HomeToolItem(
                        title = "Extensions",
                        icon = Icons.Default.Extension,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenMore
                    )
                    HomeToolItem(
                        title = "AI",
                        icon = Icons.Default.AutoAwesome,
                        modifier = Modifier.weight(1f),
                        onClick = { onAskCopilot("Explain") }
                    )
                }
            }
        }

        // 6. Compact AI Entry
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = PocketSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Prompt Input Field
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BasicTextField(
                            value = copilotPrompt,
                            onValueChange = { copilotPrompt = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = PocketTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            cursorBrush = SolidColor(PocketPrimaryBlue),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (copilotPrompt.isEmpty()) {
                                    Text(
                                        text = "Ask AI about your project...",
                                        color = PocketTextMuted,
                                        fontSize = 13.sp
                                    )
                                }
                                innerTextField()
                            }
                        )

                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (copilotPrompt.isNotBlank()) PocketPrimaryBlue else PocketSurfaceElevated)
                                .border(1.dp, if (copilotPrompt.isNotBlank()) PocketPrimaryBlue else PocketBorder, RoundedCornerShape(6.dp))
                            .clickable(enabled = copilotPrompt.isNotBlank()) {
                                val p = copilotPrompt.trim()
                                if (p.isNotBlank()) {
                                    onAskCopilot(p)
                                    copilotPrompt = ""
                                }
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (copilotPrompt.isNotBlank()) Color.White else PocketTextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    // Quick Actions: Fix, Explain, Build, Refactor
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Fix", "Explain", "Build", "Refactor").forEach { action ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PocketSurfaceElevated)
                                    .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                                    .clickable { onAskCopilot(action) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = action,
                                    color = PocketTextPrimary,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
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

@Composable
private fun HomeToolItem(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        color = PocketSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, PocketBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PocketSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PocketPrimaryBlue,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = title,
                color = PocketTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
