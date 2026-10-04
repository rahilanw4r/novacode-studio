package com.novacode.studio.ui.screens.projects

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.ui.res.painterResource
import com.novacode.studio.R
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import com.novacode.studio.ui.components.LiquidAiAssistantBar
import com.novacode.studio.ui.components.liquidBounceClick
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacode.studio.ui.GitHubAuthStatus
import com.novacode.studio.network.GitHubRepository
import com.novacode.studio.model.Project
import com.novacode.studio.model.ProjectKind
import com.novacode.studio.ui.AppUiState
import com.novacode.studio.ui.components.NovaGlassCard
import com.novacode.studio.ui.components.NovaPrimaryButton
import com.novacode.studio.ui.components.NovaSecondaryButton
import com.novacode.studio.ui.components.NovaStatusPill
import com.novacode.studio.ui.theme.NovaAmber
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaBorderGlow
import com.novacode.studio.ui.theme.NovaCardGradient
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

data class StarterBlueprint(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badge: String,
    val badgeColor: Color,
    val defaultName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaProjectsScreen(
    state: AppUiState,
    listState: LazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() },
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
    onSettings: () -> Unit,
    onOpenDeveloper: () -> Unit = {},
    onTriggerAiPrompt: (String) -> Unit = {},
    onPing: () -> Unit = {},
    onToggleTheme: () -> Unit = {},
    onInstallUpdate: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showCreateDialog by rememberSaveable { mutableStateOf(false) }
    var showGitDialog by rememberSaveable { mutableStateOf(false) }
    var showGitHubDialog by rememberSaveable { mutableStateOf(false) }
    var showUpdateDialog by rememberSaveable { mutableStateOf(false) }
    var projectToRename by remember { mutableStateOf<Project?>(null) }
    var projectToDelete by remember { mutableStateOf<Project?>(null) }
    var newProjectName by rememberSaveable { mutableStateOf("") }
    var gitRepoUrl by rememberSaveable { mutableStateOf("") }
    var importExpanded by rememberSaveable { mutableStateOf(false) }

    val importZipLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) onImportZip(uri)
    }

    LaunchedEffect(state.appUpdate?.versionCode) {
        if (state.appUpdate != null) showUpdateDialog = true
    }

    val blueprints = remember {
        listOf(
            StarterBlueprint(
                title = "React 19 + Vite",
                description = "Ultra-fast modern web application with hot reload",
                icon = Icons.Default.Code,
                badge = "WEB",
                badgeColor = NovaCyan,
                defaultName = "vite-react-app"
            ),
            StarterBlueprint(
                title = "FastAPI Backend",
                description = "Python async REST API with SQLite database",
                icon = Icons.Default.Terminal,
                badge = "PYTHON",
                badgeColor = NovaEmerald,
                defaultName = "fastapi-service"
            ),
            StarterBlueprint(
                title = "Android Native",
                description = "Kotlin + Jetpack Compose runnable on this device",
                icon = Icons.Default.Android,
                badge = "MOBILE",
                badgeColor = NovaPurple,
                defaultName = "compose-android-app"
            ),
            StarterBlueprint(
                title = "Node.js Autonomous Bot",
                description = "TypeScript bot using AI agent runtime",
                icon = Icons.Default.Psychology,
                badge = "AI AGENT",
                badgeColor = NovaAmber,
                defaultName = "autonomous-agent"
            )
        )
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // App Header
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
                        contentDescription = "NovaCode Studio",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "NovaCode",
                                color = NovaTextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.2.sp
                            )
                            Text(
                                text = "Studio",
                                color = NovaEmerald,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.2.sp
                            )
                        }
                        Text(
                            text = "Mobile Linux IDE & AI Copilot",
                            color = NovaTextMuted,
                            fontSize = 11.5.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    NovaStatusPill(
                        statusText = "ARM64 ISOLATED",
                        isRunning = state.isRunning,
                        color = if (state.isRunning) NovaAmber else NovaEmerald
                    )
                    IconButton(onClick = onOpenDeveloper) {
                        Icon(Icons.Default.Person, contentDescription = "Developer Profile", tint = NovaEmerald, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Preferences", tint = NovaTextSecondary, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        // Hero Inspiration Card (Human tone, minimal color, no gradients)
        item {
            NovaGlassCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(14.dp))
                        }
                        Text(
                            text = "WORKSPACE",
                            color = NovaEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.6.sp
                        )
                    }

                    Text(
                        text = "Build apps directly on your device",
                        color = NovaTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Your private, on-device Linux development environment. Write, compile, preview, and ship software with your autonomous AI copilot.",
                        color = NovaTextSecondary,
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(Modifier.height(4.dp))

                    // Primary Action Deck (Clean Minimal Tactile Buttons)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        NovaPrimaryButton(
                            text = "Instant Sandbox",
                            icon = Icons.Default.Bolt,
                            onClick = onCreateQuickProject,
                            modifier = Modifier.weight(1.1f),
                            height = 44.dp
                        )
                        NovaSecondaryButton(
                            text = "New Project",
                            icon = Icons.Default.Add,
                            onClick = {
                                newProjectName = ""
                                showCreateDialog = true
                            },
                            modifier = Modifier.weight(0.9f),
                            height = 44.dp
                        )
                    }
                }
            }
        }

        // Apple-style Liquid AI Assistant Bar
        item {
            LiquidAiAssistantBar(
                onTriggerAction = onTriggerAiPrompt,
                isThinking = state.isRunning
            )
        }

        // Developer Spotlight Card (Direct contact & Repo)
        item {
            NovaGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .liquidBounceClick(onClick = onOpenDeveloper)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(NovaEmerald.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Rahil Anwar", color = NovaTextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NovaEmerald.copy(alpha = 0.15f))
                                        .padding(horizontal = 5.dp, vertical = 2.dp)
                                ) {
                                    Text("CREATOR", color = NovaEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text("Telegram: @RahilAnw4r · GitHub: rahilanw4r", color = NovaTextMuted, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                        }
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = NovaTextSecondary, modifier = Modifier.size(18.dp))
                }
            }
        }

        // Import Codebase Section
        item {
            val isImportExpanded = importExpanded || state.projectImporting || state.gitCloneRunning
            NovaGlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { importExpanded = !importExpanded },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NovaSurfaceElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.CloudDownload, contentDescription = null, tint = NovaIndigo, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text(
                                    text = "Import Existing Codebase",
                                    color = NovaTextPrimary,
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isImportExpanded) "Clone full Git history or unpack ZIP archives" else "GitHub, Git URL, or ZIP archive",
                                    color = NovaTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = if (isImportExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = NovaTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(visible = isImportExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(NovaSurfaceElevated)
                                        .border(1.dp, NovaBorder, RoundedCornerShape(10.dp))
                                        .clickable(enabled = !state.projectImporting && !state.gitCloneRunning) {
                                            importZipLauncher.launch("*/*")
                                        }
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Default.Download, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(16.dp))
                                        Text(if (state.projectImporting) "Importing…" else "ZIP Archive", color = NovaTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(NovaSurfaceElevated)
                                        .border(1.dp, NovaBorder, RoundedCornerShape(10.dp))
                                        .clickable(enabled = !state.projectImporting && !state.gitCloneRunning) {
                                            gitRepoUrl = ""
                                            showGitDialog = true
                                        }
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Default.Code, contentDescription = null, tint = NovaIndigo, modifier = Modifier.size(16.dp))
                                        Text(if (state.gitCloneRunning) "Cloning…" else "Git Clone", color = NovaTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }

                            // GitHub Account Link Tile
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(NovaSurfaceElevated)
                                    .border(1.dp, NovaBorder, RoundedCornerShape(10.dp))
                                    .clickable(enabled = !state.gitCloneRunning) {
                                        showGitHubDialog = true
                                        if (state.githubAuthStatus == GitHubAuthStatus.CONNECTED && state.githubRepositories.isEmpty()) {
                                            onRefreshGitHub()
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Icon(Icons.Default.Code, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(18.dp))
                                        Column {
                                            Text(
                                                text = state.githubLogin?.let { "GitHub · @$it" } ?: "Connect GitHub Account",
                                                color = NovaTextPrimary,
                                                fontSize = 12.5.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = if (state.githubLogin != null) "Access your public and private repositories" else "Sign in to clone directly from GitHub",
                                                color = NovaTextMuted,
                                                fontSize = 10.5.sp
                                            )
                                        }
                                    }
                                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = NovaTextSecondary, modifier = Modifier.size(18.dp))
                                }
                            }

                            (state.projectImportMessage ?: state.gitCloneMessage)?.let { msg ->
                                Text(text = msg, color = NovaCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }

        // Starter Blueprints Carousel (New Feature!)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Starter Blueprints",
                    color = NovaTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    blueprints.forEach { blueprint ->
                        Box(
                            modifier = Modifier
                                .width(200.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(NovaSurfaceElevated)
                                .border(1.dp, NovaBorder, RoundedCornerShape(14.dp))
                                .clickable { onCreate(blueprint.defaultName) }
                                .padding(12.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(blueprint.badgeColor.copy(alpha = 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(blueprint.icon, contentDescription = null, tint = blueprint.badgeColor, modifier = Modifier.size(16.dp))
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(blueprint.badgeColor.copy(alpha = 0.12f))
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text(blueprint.badge, color = blueprint.badgeColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Text(
                                    text = blueprint.title,
                                    color = NovaTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = blueprint.description,
                                    color = NovaTextMuted,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // Update Notification Banner (if any)
        state.appUpdate?.let { update ->
            item {
                NovaGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showUpdateDialog = true },
                    borderColor = NovaIndigo.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(NovaIndigo.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, tint = NovaIndigo, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text(
                                    text = "NovaCode Studio ${update.versionName}",
                                    color = NovaTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "A new release is available to install",
                                    color = NovaTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        NovaStatusPill(statusText = "UPDATE", isRunning = false, color = NovaIndigo)
                    }
                }
            }
        }

        // Active Projects Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your Projects (${state.projects.size})",
                    color = NovaTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${state.projects.size} active",
                    color = NovaTextMuted,
                    fontSize = 11.5.sp
                )
            }
        }

        // Empty State or Project List
        if (state.projects.isEmpty()) {
            item {
                NovaGlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(NovaIndigo.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Folder, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(26.dp))
                        }
                        Text(
                            text = "No Projects Yet",
                            color = NovaTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Launch an Instant Sandbox or pick a template above to start coding with your AI partner.",
                            color = NovaTextMuted,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(Modifier.height(4.dp))
                        NovaPrimaryButton(
                            text = "Launch Instant Sandbox",
                            icon = Icons.Default.RocketLaunch,
                            onClick = onCreateQuickProject,
                            height = 42.dp
                        )
                    }
                }
            }
        } else {
            items(state.projects, key = { it.id }) { project ->
                var menuOpen by remember { mutableStateOf(false) }
                val isRunning = state.isRunning && state.activeProject?.id == project.id

                NovaGlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpen(project) },
                    borderColor = if (isRunning) NovaIndigo else NovaBorder
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = project.name,
                                    color = NovaTextPrimary,
                                    fontSize = 14.5.sp,
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
                                if (isRunning) {
                                    NovaStatusPill(statusText = "RUNNING", isRunning = true, color = NovaAmber)
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
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = project.slug,
                                    color = NovaIndigo,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text("•", color = NovaTextMuted, fontSize = 10.sp)
                                Text(
                                    text = project.formattedUpdatedAt,
                                    color = NovaTextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NovaIndigo.copy(alpha = 0.15f))
                                    .clickable { onOpen(project) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Open", color = NovaCyan, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(12.dp))
                                }
                            }

                            Box {
                                IconButton(onClick = { menuOpen = true }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = NovaTextMuted, modifier = Modifier.size(18.dp))
                                }
                                DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Rename Project") },
                                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                        onClick = {
                                            menuOpen = false
                                            projectToRename = project
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Delete Project", color = NovaRose) },
                                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = NovaRose, modifier = Modifier.size(16.dp)) },
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
    }

    // New Project Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create New Project", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Give your project a name to initialize an isolated workspace.", color = NovaTextSecondary, fontSize = 13.sp)
                    OutlinedTextField(
                        value = newProjectName,
                        onValueChange = { newProjectName = it },
                        label = { Text("Project Name") },
                        placeholder = { Text("e.g. nova-chat-app") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                NovaPrimaryButton(
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
                    Text("Cancel", color = NovaTextSecondary)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Git Clone Dialog
    if (showGitDialog) {
        AlertDialog(
            onDismissRequest = { showGitDialog = false },
            title = { Text("Clone Public Git Repository", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Enter a public HTTPS Git repository URL to clone complete history into an isolated workspace.", color = NovaTextSecondary, fontSize = 13.sp)
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
                NovaPrimaryButton(
                    text = "Clone Repository",
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
                    Text("Cancel", color = NovaTextSecondary)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Delete Confirmation Dialog
    projectToDelete?.let { project ->
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = { Text("Delete Project?", color = NovaRose, fontWeight = FontWeight.Bold) },
            text = {
                Text("Are you sure you want to permanently delete \"${project.name}\"? All files in this isolated workspace will be deleted.", color = NovaTextSecondary)
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteProject(project.id)
                        projectToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NovaRose)
                ) {
                    Text("Delete Permanently", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) {
                    Text("Keep Project", color = NovaTextSecondary)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Rename Dialog
    projectToRename?.let { project ->
        var renameText by remember { mutableStateOf(project.name) }
        AlertDialog(
            onDismissRequest = { projectToRename = null },
            title = { Text("Rename Project", color = NovaTextPrimary, fontWeight = FontWeight.Bold) },
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
                NovaPrimaryButton(
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
                    Text("Cancel", color = NovaTextSecondary)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
