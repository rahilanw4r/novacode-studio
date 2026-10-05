package com.pocketide.app.ui.screens.projects

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketide.app.ui.GitHubAuthStatus
import com.pocketide.app.network.GitHubRepository
import com.pocketide.app.model.Project
import com.pocketide.app.ui.AppUiState
import com.pocketide.app.ui.components.PocketPrimaryButton
import com.pocketide.app.ui.components.PocketSecondaryButton
import com.pocketide.app.ui.components.PocketStatusPill
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketCyan
import com.pocketide.app.ui.theme.PocketEmerald
import com.pocketide.app.ui.theme.PocketIndigo
import com.pocketide.app.ui.theme.PocketObsidian
import com.pocketide.app.ui.theme.PocketRose
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketSurfaceVariant
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary

@Composable
fun PocketProjectsScreen(
    state: AppUiState,
    listState: LazyListState,
    onOpen: (Project) -> Unit,
    onCreate: (String) -> Unit,
    onCreateQuickProject: () -> Unit,
    onImportZip: (Uri) -> Unit,
    onCloneGit: (String) -> Unit,
    onStartGitHubLogin: () -> Unit,
    onGenerateNewGitHubCode: () -> Unit,
    onRefreshGitHub: () -> Unit,
    onDisconnectGitHub: () -> Unit,
    onCloneGitHub: (GitHubRepository) -> Unit,
    onRenameProject: (String, String) -> Unit,
    onDeleteProject: (String) -> Unit,
    onOpenCommandCenter: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }
    var newProjectName by remember { mutableStateOf("") }
    var showGitDialog by remember { mutableStateOf(false) }
    var gitRepoUrl by remember { mutableStateOf("") }
    var showGitHubDialog by remember { mutableStateOf(false) }

    var projectToRename by remember { mutableStateOf<Project?>(null) }
    var projectToDelete by remember { mutableStateOf<Project?>(null) }

    val importZipLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { onImportZip(it) } }
    )

    val filteredProjects = remember(state.projects, searchQuery) {
        if (searchQuery.isBlank()) {
            state.projects
        } else {
            state.projects.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                    it.language.contains(searchQuery, ignoreCase = true) ||
                    it.slug.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(PocketObsidian),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Projects",
                        color = PocketTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${state.projects.size}",
                            color = PocketEmerald,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Command Center ⌘
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                            .clickable(onClick = onOpenCommandCenter),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⌘", color = PocketTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    // + New Project button
                    PocketPrimaryButton(
                        text = "New Project",
                        icon = Icons.Default.Add,
                        onClick = {
                            newProjectName = ""
                            showCreateDialog = true
                        },
                        height = 34.dp
                    )
                }
            }
        }

        // Search Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(PocketSurface)
                    .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = PocketTextMuted,
                    modifier = Modifier.size(16.dp)
                )
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        color = PocketTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(PocketEmerald),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Filter projects by name or language…",
                                color = PocketTextMuted,
                                fontSize = 13.sp
                            )
                        }
                        innerTextField()
                    }
                )
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = PocketTextSecondary, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }

        // Import & Actions Ribbon (compact pills)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Instant Sandbox
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PocketSurface)
                        .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                        .clickable(onClick = onCreateQuickProject)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(Icons.Default.Bolt, contentDescription = null, tint = PocketEmerald, modifier = Modifier.size(14.dp))
                    Text("Instant Sandbox", color = PocketTextPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.Medium)
                }

                // Git Clone
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PocketSurface)
                        .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            gitRepoUrl = ""
                            showGitDialog = true
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = PocketIndigo, modifier = Modifier.size(14.dp))
                    Text(if (state.gitCloneRunning) "Cloning…" else "Git Clone", color = PocketTextPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.Medium)
                }

                // ZIP Import
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PocketSurface)
                        .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                        .clickable { importZipLauncher.launch("*/*") }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(Icons.Default.CloudDownload, contentDescription = null, tint = PocketCyan, modifier = Modifier.size(14.dp))
                    Text(if (state.projectImporting) "Importing…" else "Import ZIP", color = PocketTextPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.Medium)
                }

                // GitHub
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PocketSurface)
                        .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            showGitHubDialog = true
                            if (state.githubAuthStatus == GitHubAuthStatus.CONNECTED && state.githubRepositories.isEmpty()) {
                                onRefreshGitHub()
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = PocketEmerald, modifier = Modifier.size(14.dp))
                    Text(state.githubLogin?.let { "@$it" } ?: "GitHub", color = PocketTextPrimary, fontSize = 11.5.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Projects Section
        if (filteredProjects.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = PocketSurface,
                    border = BorderStroke(1.dp, PocketBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(PocketSurfaceElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Folder, contentDescription = null, tint = PocketTextMuted, modifier = Modifier.size(20.dp))
                        }
                        Text(
                            text = if (searchQuery.isNotBlank()) "No matching projects" else "No Projects Yet",
                            color = PocketTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = if (searchQuery.isNotBlank()) "Try searching with a different keyword." else "Create a project or start an instant sandbox to begin.",
                            color = PocketTextMuted,
                            fontSize = 12.sp
                        )
                        if (searchQuery.isBlank()) {
                            PocketPrimaryButton(
                                text = "Start Sandbox",
                                icon = Icons.Default.Bolt,
                                onClick = onCreateQuickProject,
                                height = 38.dp
                            )
                        }
                    }
                }
            }
        } else {
            items(filteredProjects, key = { it.id }) { project ->
                var menuOpen by remember { mutableStateOf(false) }
                val isRunning = state.isRunning && state.activeProject?.id == project.id

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onOpen(project) },
                    shape = RoundedCornerShape(10.dp),
                    color = PocketSurface,
                    border = BorderStroke(1.dp, if (isRunning) PocketEmerald else PocketBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
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
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isRunning) PocketEmerald.copy(alpha = 0.15f) else PocketSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = if (isRunning) PocketEmerald else PocketTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = project.name,
                                        color = PocketTextPrimary,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (isRunning) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(PocketEmerald.copy(alpha = 0.15f))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text("RUNNING", color = PocketEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Text(
                                    text = "${project.language.ifBlank { "Linux" }} • Modified ${project.formattedUpdatedAt}",
                                    color = PocketTextMuted,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PocketSurfaceElevated)
                                    .clickable { onOpen(project) }
                                    .padding(horizontal = 8.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text("Open", color = PocketEmerald, fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold)
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = PocketEmerald, modifier = Modifier.size(12.dp))
                                }
                            }

                            Box {
                                IconButton(
                                    onClick = { menuOpen = true },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = PocketTextMuted, modifier = Modifier.size(16.dp))
                                }
                                DropdownMenu(
                                    expanded = menuOpen,
                                    onDismissRequest = { menuOpen = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Rename Project") },
                                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                        onClick = {
                                            menuOpen = false
                                            projectToRename = project
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete Project", color = PocketRose) },
                                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = PocketRose, modifier = Modifier.size(16.dp)) },
                                        onClick = {
                                            menuOpen = false
                                            projectToDelete = project
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom clearance
        item {
            Spacer(Modifier.height(72.dp))
        }
    }

    // Create New Project Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New Project", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = newProjectName,
                    onValueChange = { newProjectName = it },
                    label = { Text("Project Name") },
                    placeholder = { Text("e.g. mobile-web-app") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                PocketPrimaryButton(
                    text = "Create",
                    onClick = {
                        val trimmed = newProjectName.trim()
                        if (trimmed.isNotBlank()) {
                            showCreateDialog = false
                            onCreate(trimmed)
                        }
                    },
                    height = 40.dp
                )
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("Cancel", color = PocketTextSecondary)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // Git Clone Dialog
    if (showGitDialog) {
        AlertDialog(
            onDismissRequest = { showGitDialog = false },
            title = { Text("Clone Git Repository", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Enter public HTTPS Git repository URL:", color = PocketTextSecondary, fontSize = 12.5.sp)
                    OutlinedTextField(
                        value = gitRepoUrl,
                        onValueChange = { gitRepoUrl = it },
                        label = { Text("Git Repository URL") },
                        placeholder = { Text("https://github.com/user/repo.git") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                PocketPrimaryButton(
                    text = "Clone",
                    onClick = {
                        val trimmed = gitRepoUrl.trim()
                        if (trimmed.isNotBlank()) {
                            showGitDialog = false
                            onCloneGit(trimmed)
                        }
                    },
                    height = 40.dp
                )
            },
            dismissButton = {
                TextButton(onClick = { showGitDialog = false }) {
                    Text("Cancel", color = PocketTextSecondary)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // Rename Dialog
    projectToRename?.let { project ->
        var renameText by remember { mutableStateOf(project.name) }
        AlertDialog(
            onDismissRequest = { projectToRename = null },
            title = { Text("Rename Project", color = PocketTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text("New Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                PocketPrimaryButton(
                    text = "Save",
                    onClick = {
                        val trimmed = renameText.trim()
                        if (trimmed.isNotBlank()) {
                            onRenameProject(project.id, trimmed)
                            projectToRename = null
                        }
                    },
                    height = 40.dp
                )
            },
            dismissButton = {
                TextButton(onClick = { projectToRename = null }) {
                    Text("Cancel", color = PocketTextSecondary)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // Delete Confirmation Dialog
    projectToDelete?.let { project ->
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = { Text("Delete Project?", color = PocketRose, fontWeight = FontWeight.Bold) },
            text = {
                Text("Are you sure you want to delete \"${project.name}\"? All files in this project workspace will be deleted.", color = PocketTextSecondary)
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteProject(project.id)
                        projectToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PocketRose)
                ) {
                    Text("Delete Permanently", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) {
                    Text("Cancel", color = PocketTextSecondary)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }

    // GitHub Repositories Sheet / Dialog
    if (showGitHubDialog) {
        AlertDialog(
            onDismissRequest = { showGitHubDialog = false },
            title = {
                Text(
                    text = state.githubLogin?.let { "GitHub Repositories (@$it)" } ?: "Connect GitHub",
                    color = PocketTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.githubAuthStatus != GitHubAuthStatus.CONNECTED) {
                        Text(
                            text = "Authenticate with GitHub to view and clone your repositories directly.",
                            color = PocketTextSecondary,
                            fontSize = 13.sp
                        )
                        if (state.githubAuthStatus == GitHubAuthStatus.AWAITING_USER) {
                            Text(
                                text = "Code: ${state.githubUserCode ?: "..."}",
                                color = PocketEmerald,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            state.githubVerificationUri?.let { uri ->
                                Text("Open: $uri", color = PocketCyan, fontSize = 12.sp)
                            }
                        }
                    } else {
                        if (state.githubRepositoriesLoading) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = PocketEmerald, modifier = Modifier.size(28.dp))
                            }
                        } else if (state.githubRepositories.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No repositories found", color = PocketTextMuted, fontSize = 13.sp)
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(state.githubRepositories, key = { it.fullName }) { repo ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(PocketSurfaceElevated)
                                            .clickable {
                                                showGitHubDialog = false
                                                onCloneGitHub(repo)
                                            }
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(repo.fullName.substringAfterLast('/'), color = PocketTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                            Text(repo.fullName, color = PocketTextMuted, fontSize = 11.sp, maxLines = 1)
                                        }
                                        Icon(Icons.Default.CloudDownload, contentDescription = "Clone", tint = PocketEmerald, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (state.githubAuthStatus != GitHubAuthStatus.CONNECTED) {
                    PocketPrimaryButton(
                        text = "Sign in with GitHub",
                        onClick = onStartGitHubLogin,
                        height = 36.dp
                    )
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onRefreshGitHub) {
                            Text("Refresh", color = PocketCyan)
                        }
                        TextButton(onClick = onDisconnectGitHub) {
                            Text("Disconnect", color = PocketRose)
                        }
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showGitHubDialog = false }) {
                    Text("Close", color = PocketTextSecondary)
                }
            },
            containerColor = PocketSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }
}
