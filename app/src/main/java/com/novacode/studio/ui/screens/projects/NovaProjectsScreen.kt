package com.novacode.studio.ui.screens.projects

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacode.studio.model.Project
import com.novacode.studio.model.ProjectKind
import com.novacode.studio.ui.components.NovaGlassCard
import com.novacode.studio.ui.components.NovaStatusPill
import com.novacode.studio.ui.theme.NovaAmber
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaObsidian
import com.novacode.studio.ui.theme.NovaPurple
import com.novacode.studio.ui.theme.NovaRose
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary

data class ProjectTemplate(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val language: String,
    val defaultPrompt: String
)

@Composable
fun NovaProjectsScreen(
    projects: List<Project>,
    onOpenProject: (Project) -> Unit,
    onNewProject: () -> Unit,
    onQuickProject: () -> Unit,
    onDeleteProject: (String) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val templates = listOf(
        ProjectTemplate(
            title = "Vite + React App",
            description = "Fast modern web application with hot reload",
            icon = Icons.Default.Code,
            language = "TypeScript",
            defaultPrompt = "Create a modern React app with Vite and Tailwind CSS"
        ),
        ProjectTemplate(
            title = "Python FastAPI Backend",
            description = "High-performance async REST API",
            icon = Icons.Default.Terminal,
            language = "Python",
            defaultPrompt = "Build a FastAPI backend with SQLite and Swagger docs"
        ),
        ProjectTemplate(
            title = "Android Native App",
            description = "Kotlin app runnable on this device",
            icon = Icons.Default.Android,
            language = "Kotlin",
            defaultPrompt = "Scaffold a native Jetpack Compose Android app"
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(NovaIndigo),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                    }
                    Text(
                        text = "NovaCode Studio",
                        color = NovaTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Autonomous AI Mobile IDE",
                    color = NovaTextMuted,
                    fontSize = 12.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                NovaStatusPill(statusText = "ARM64 UBUNTU", isRunning = false, color = NovaEmerald)
                IconButton(onClick = onOpenSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = NovaTextSecondary)
                }
            }
        }

        // Action Buttons Row (Quick Project + New Project)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Quick Project Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NovaIndigo)
                    .clickable(onClick = onQuickProject)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Text(
                        text = "Instant Sandbox",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // New Named Project Button
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(NovaSurfaceElevated)
                    .border(1.dp, NovaBorder, RoundedCornerShape(12.dp))
                    .clickable(onClick = onNewProject)
                    .padding(vertical = 12.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(18.dp))
                    Text(
                        text = "New Project",
                        color = NovaTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Project List
        Text(
            text = "Your Projects (${projects.size})",
            color = NovaTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        if (projects.isEmpty()) {
            NovaGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Folder, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(36.dp))
                    Text("No projects yet", color = NovaTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Text("Launch an Instant Sandbox or create a new project above to start coding.", color = NovaTextMuted, fontSize = 12.sp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(projects) { project ->
                    ProjectCardItem(
                        project = project,
                        onClick = { onOpenProject(project) },
                        onDelete = { onDeleteProject(project.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectCardItem(
    project: Project,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    NovaGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = project.name,
                        color = NovaTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (project.kind == ProjectKind.QUICK_PROJECT) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NovaCyan.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("SANDBOX", color = NovaCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                if (project.description.isNotBlank()) {
                    Text(
                        text = project.description,
                        color = NovaTextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = project.slug,
                        color = NovaIndigo,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text("•", color = NovaTextMuted)
                    Text(
                        text = project.formattedUpdatedAt,
                        color = NovaTextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Box {
                IconButton(onClick = { menuOpen = true }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = NovaTextMuted, modifier = Modifier.size(18.dp))
                }
                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                    DropdownMenuItem(
                        text = { Text("Delete Project", color = NovaRose) },
                        onClick = {
                            menuOpen = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}
