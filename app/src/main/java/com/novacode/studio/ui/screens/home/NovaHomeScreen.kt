package com.novacode.studio.ui.screens.home

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import com.novacode.studio.R
import com.novacode.studio.model.Project
import com.novacode.studio.ui.AppUiState
import com.novacode.studio.ui.components.NovaPrimaryButton
import com.novacode.studio.ui.components.NovaSecondaryButton
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaObsidian
import com.novacode.studio.ui.theme.NovaPurple
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaSurfaceVariant
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary

data class StarterTemplate(
    val title: String,
    val tech: String,
    val badge: String,
    val badgeColor: Color,
    val icon: ImageVector,
    val defaultName: String
)

@Composable
fun NovaHomeScreen(
    state: AppUiState,
    listState: LazyListState,
    onOpenProject: (Project) -> Unit,
    onCreateProject: (String) -> Unit,
    onCreateQuickProject: () -> Unit,
    onViewAllProjects: () -> Unit,
    onOpenCommandCenter: () -> Unit,
    onOpenDeveloper: () -> Unit,
    onAskCopilot: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var copilotPrompt by remember { mutableStateOf("") }

    val starterTemplates = remember {
        listOf(
            StarterTemplate("React + Vite", "TypeScript • SPA", "WEB", NovaCyan, Icons.Default.Code, "vite-react-app"),
            StarterTemplate("FastAPI", "Python • Async REST", "API", NovaEmerald, Icons.Default.Terminal, "fastapi-service"),
            StarterTemplate("Android", "Kotlin • Compose", "APP", NovaPurple, Icons.Default.Android, "android-compose-app"),
            StarterTemplate("Python", "Scripting • CLI", "PY", NovaIndigo, Icons.Default.Terminal, "python-script"),
            StarterTemplate("Node.js", "Express • REST", "NODE", NovaCyan, Icons.Default.Code, "node-express-api")
        )
    }

    val recentProjects = remember(state.projects) {
        state.projects.take(4)
    }

    LazyColumn(
        state = listState,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "NovaCode Studio",
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "NovaCode",
                                color = NovaTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.2.sp
                            )
                            Text(
                                text = "Studio",
                                color = NovaEmerald,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(NovaEmerald)
                            )
                            Text(
                                text = "Local • ARM64",
                                color = NovaTextMuted,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Command Center ⌘
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaSurfaceElevated)
                            .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                            .clickable(onClick = onOpenCommandCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⌘", color = NovaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    // Developer profile
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaSurfaceElevated)
                            .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                            .clickable(onClick = onOpenDeveloper),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Developer", tint = NovaEmerald, modifier = Modifier.size(17.dp))
                    }
                }
            }
        }

        // WORKSPACE Section (compact, subtle surface, no oversized cards)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = NovaSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NovaBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "WORKSPACE",
                        color = NovaEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.6.sp
                    )
                    Text(
                        text = "Build directly on your device",
                        color = NovaTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Your private, on-device Linux development environment.",
                        color = NovaTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NovaPrimaryButton(
                            text = "Start Sandbox",
                            icon = Icons.Default.Bolt,
                            onClick = onCreateQuickProject,
                            modifier = Modifier.weight(1f),
                            height = 42.dp
                        )
                        NovaSecondaryButton(
                            text = "New Project",
                            icon = Icons.Default.Add,
                            onClick = {
                                newProjectName = ""
                                showCreateDialog = true
                            },
                            modifier = Modifier.weight(1f),
                            height = 42.dp
                        )
                    }
                }
            }
        }

        // RECENT PROJECTS Section (compact rows)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "RECENT PROJECTS",
                        color = NovaTextMuted,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    if (state.projects.isNotEmpty()) {
                        Text(
                            text = "See all (${state.projects.size})",
                            color = NovaEmerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable(onClick = onViewAllProjects)
                        )
                    }
                }

                if (recentProjects.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = NovaSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NovaBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No Projects Yet",
                                color = NovaTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Start a sandbox or create a project to begin.",
                                color = NovaTextMuted,
                                fontSize = 12.sp
                            )
                            NovaPrimaryButton(
                                text = "Start Sandbox",
                                icon = Icons.Default.Bolt,
                                onClick = onCreateQuickProject,
                                height = 36.dp
                            )
                        }
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = NovaSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NovaBorder)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            recentProjects.forEachIndexed { index, project ->
                                if (index > 0) {
                                    HorizontalDivider(color = NovaBorder)
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
                                                .size(32.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(NovaSurfaceElevated),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Folder,
                                                contentDescription = null,
                                                tint = NovaEmerald,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                            Text(
                                                text = project.name,
                                                color = NovaTextPrimary,
                                                fontSize = 13.5.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = "${project.language.ifBlank { "Linux" }} • Modified ${project.formattedUpdatedAt}",
                                                color = NovaTextMuted,
                                                fontSize = 11.sp,
                                                maxLines = 1
                                            )
                                        }
                                    }

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "Open",
                                        tint = NovaTextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // STARTER TEMPLATES Section (compact horizontally scrollable)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "STARTER TEMPLATES",
                    color = NovaTextMuted,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
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
                                .width(138.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onCreateProject(template.defaultName) },
                            shape = RoundedCornerShape(10.dp),
                            color = NovaSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NovaBorder)
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
                                        tint = template.badgeColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(template.badgeColor.copy(alpha = 0.15f))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = template.badge,
                                            color = template.badgeColor,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = template.title,
                                    color = NovaTextPrimary,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = template.tech,
                                    color = NovaTextMuted,
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

        // AI COPILOT Section (clean prompt box + quick actions)
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = NovaSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, NovaBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(NovaEmerald.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = NovaEmerald,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Text(
                            text = "AI COPILOT",
                            color = NovaEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                    }

                    Text(
                        text = "Build, debug and understand your code with AI.",
                        color = NovaTextSecondary,
                        fontSize = 12.5.sp
                    )

                    // Prompt Input
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaSurfaceElevated)
                            .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BasicTextField(
                            value = copilotPrompt,
                            onValueChange = { copilotPrompt = it },
                            modifier = Modifier.weight(1f),
                            textStyle = TextStyle(
                                color = NovaTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            cursorBrush = SolidColor(NovaEmerald),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (copilotPrompt.isEmpty()) {
                                    Text(
                                        text = "Ask NovaCode…",
                                        color = NovaTextMuted,
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
                                .background(if (copilotPrompt.isNotBlank()) NovaEmerald else NovaSurfaceVariant)
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
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send",
                                tint = if (copilotPrompt.isNotBlank()) NovaObsidian else NovaTextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Quick Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Fix Code", "Explain", "Build", "Refactor").forEach { action ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NovaSurfaceElevated)
                                    .border(1.dp, NovaBorder, RoundedCornerShape(6.dp))
                                    .clickable { onAskCopilot(action) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = action,
                                    color = NovaTextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom clearance for floating nav bar
        item {
            Spacer(Modifier.height(72.dp))
        }
    }

    // Create New Project Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("New Project", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newProjectName,
                    onValueChange = { newProjectName = it },
                    label = { Text("Project Name") },
                    placeholder = { Text("my-new-app") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                NovaPrimaryButton(
                    text = "Create",
                    onClick = {
                        val name = newProjectName.trim()
                        if (name.isNotBlank()) {
                            onCreateProject(name)
                            showCreateDialog = false
                        }
                    },
                    height = 40.dp
                )
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = NovaTextSecondary)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }
}
