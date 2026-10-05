package com.novacode.studio.ui.screens.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Difference
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacode.studio.model.ChangeItem
import com.novacode.studio.model.DiffLine
import com.novacode.studio.model.DiffLineType
import com.novacode.studio.model.WorkspaceEntry
import com.novacode.studio.ui.components.NovaGlassCard
import com.novacode.studio.ui.components.NovaStatusPill
import com.novacode.studio.ui.theme.*
import com.novacode.studio.ui.theme.NovaTextSecondary

enum class CodeStudioTab { FILES, DIFFS, EDITOR }

@Composable
fun NovaCodeStudioScreen(
    files: List<WorkspaceEntry>,
    changes: List<ChangeItem>,
    openedFilePath: String?,
    openedFileContent: String?,
    isLoadingFile: Boolean,
    onOpenFile: (WorkspaceEntry) -> Unit,
    onCloseFile: () -> Unit,
    onSaveFile: ((String, String) -> Unit)? = null,
    onRefreshFiles: () -> Unit,
    onKeepFileChange: (String) -> Unit,
    onUndoFileChange: (String) -> Unit,
    onKeepAllChanges: () -> Unit,
    onUndoAllChanges: () -> Unit,
    onAskCopilotContextual: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember {
        mutableStateOf(if (changes.isNotEmpty()) CodeStudioTab.DIFFS else CodeStudioTab.FILES)
    }
    var searchQuery by remember { mutableStateOf("") }
    var showContextualAi by remember { mutableStateOf(false) }
    var customAiPrompt by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian)
    ) {
        // Studio Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurface)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StudioTabButton(
                    title = "Files",
                    icon = Icons.Default.Folder,
                    count = files.size,
                    isSelected = selectedTab == CodeStudioTab.FILES,
                    onClick = { selectedTab = CodeStudioTab.FILES }
                )
                StudioTabButton(
                    title = "Diffs",
                    icon = Icons.Default.Difference,
                    count = changes.size,
                    badgeColor = if (changes.isNotEmpty()) NovaCyan else null,
                    isSelected = selectedTab == CodeStudioTab.DIFFS,
                    onClick = { selectedTab = CodeStudioTab.DIFFS }
                )
                if (openedFilePath != null) {
                    StudioTabButton(
                        title = openedFilePath.substringAfterLast('/'),
                        icon = Icons.Default.Code,
                        isSelected = selectedTab == CodeStudioTab.EDITOR,
                        onClick = { selectedTab = CodeStudioTab.EDITOR }
                    )
                }
            }

            IconButton(onClick = onRefreshFiles, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = NovaCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        HorizontalDivider(color = NovaBorder)

        // Tab Content
        when (selectedTab) {
            CodeStudioTab.FILES -> {
                FileBrowserView(
                    files = files,
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    onOpenFile = {
                        onOpenFile(it)
                        selectedTab = CodeStudioTab.EDITOR
                    }
                )
            }
            CodeStudioTab.DIFFS -> {
                DiffInspectorView(
                    changes = changes,
                    onKeepFile = onKeepFileChange,
                    onUndoFile = onUndoFileChange,
                    onKeepAll = onKeepAllChanges,
                    onUndoAll = onUndoAllChanges
                )
            }
            CodeStudioTab.EDITOR -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CodeEditorView(
                        filePath = openedFilePath ?: "No file open",
                        initialContent = openedFileContent ?: "",
                        isLoading = isLoadingFile,
                        onClose = {
                            onCloseFile()
                            selectedTab = CodeStudioTab.FILES
                        },
                        onSave = { updated ->
                            if (openedFilePath != null) {
                                onSaveFile?.invoke(openedFilePath, updated)
                            }
                        }
                    )

                    // Floating Contextual AI Button ✦
                    if (openedFilePath != null) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(NovaEmerald)
                                .clickable { showContextualAi = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "AI Actions",
                                tint = NovaObsidian,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Contextual AI Sheet Dialog
    if (showContextualAi && openedFilePath != null) {
        val fileName = openedFilePath.substringAfterLast('/')
        val fileSnippet = openedFileContent?.take(1500) ?: ""
        AlertDialog(
            onDismissRequest = { showContextualAi = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(18.dp))
                    Text("Pocket IDE Copilot", color = NovaTextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Context: $fileName", color = NovaTextMuted, fontSize = 12.sp, fontFamily = FontFamily.Monospace)

                    val contextualActions = listOf(
                        "Explain this code" to "Explain the logic, architecture, and potential pitfalls of $fileName:\n```\n$fileSnippet\n```",
                        "Fix this error" to "Diagnose syntax, runtime, and logic errors in $fileName and provide the corrected code:\n```\n$fileSnippet\n```",
                        "Optimize" to "Optimize performance, algorithmic complexity, and memory usage for $fileName:\n```\n$fileSnippet\n```",
                        "Add feature" to "Suggest and implement production-ready features for $fileName:\n```\n$fileSnippet\n```",
                        "Write tests" to "Generate comprehensive unit tests with edge cases for $fileName:\n```\n$fileSnippet\n```"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        contextualActions.forEach { (label, prompt) ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NovaSurfaceElevated)
                                    .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                                    .clickable {
                                        showContextualAi = false
                                        onAskCopilotContextual?.invoke(prompt)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 9.dp)
                            ) {
                                Text(label, color = NovaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = customAiPrompt,
                        onValueChange = { customAiPrompt = it },
                        placeholder = { Text("Ask anything about this file…", fontSize = 12.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                if (customAiPrompt.isNotBlank()) {
                    TextButton(
                        onClick = {
                            val p = customAiPrompt.trim()
                            showContextualAi = false
                            customAiPrompt = ""
                            onAskCopilotContextual?.invoke("Regarding $fileName:\n$p\n\nFile code:\n```\n$fileSnippet\n```")
                        }
                    ) {
                        Text("Send", color = NovaEmerald, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showContextualAi = false }) {
                    Text("Close", color = NovaTextSecondary)
                }
            },
            containerColor = NovaSurface,
            shape = RoundedCornerShape(14.dp)
        )
    }
}

@Composable
private fun StudioTabButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int? = null,
    badgeColor: Color? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) NovaIndigo.copy(alpha = 0.2f) else Color.Transparent
    val border = if (isSelected) NovaIndigo else Color.Transparent
    val textColor = if (isSelected) NovaIndigo else NovaTextSecondary

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = title,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
        if (count != null && count > 0) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(badgeColor ?: NovaSurfaceElevated)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    color = if (badgeColor != null) NovaObsidian else NovaTextSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FileBrowserView(
    files: List<WorkspaceEntry>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onOpenFile: (WorkspaceEntry) -> Unit
) {
    val filtered = remember(files, searchQuery) {
        if (searchQuery.isBlank()) files
        else files.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(NovaSurfaceElevated)
                .border(1.dp, NovaBorder, RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = NovaTextMuted, modifier = Modifier.size(16.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                textStyle = TextStyle(color = NovaTextPrimary, fontSize = 13.sp),
                cursorBrush = SolidColor(NovaCyan),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (searchQuery.isEmpty()) {
                        Text("Search project files…", color = NovaTextMuted, fontSize = 13.sp)
                    }
                    inner()
                }
            )
            if (searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = NovaTextMuted,
                    modifier = Modifier.size(16.dp).clickable { onSearchChange("") }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filtered.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No files found", color = NovaTextMuted, fontSize = 13.sp)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(filtered) { entry ->
                    FileTreeItem(entry = entry, onOpen = { onOpenFile(entry) })
                }
            }
        }
    }
}

@Composable
private fun FileTreeItem(entry: WorkspaceEntry, onOpen: () -> Unit) {
    val indent = (entry.depth * 14).dp
    val icon = when {
        entry.isDirectory -> Icons.Default.Folder
        entry.name.endsWith(".kt") || entry.name.endsWith(".java") -> Icons.Default.Code
        entry.name.endsWith(".py") || entry.name.endsWith(".js") || entry.name.endsWith(".ts") -> Icons.Default.Code
        else -> Icons.Default.Description
    }
    val tint = when {
        entry.isDirectory -> NovaCyan
        entry.name.endsWith(".kt") -> NovaPurple
        entry.name.endsWith(".py") -> NovaAmber
        else -> NovaTextSecondary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = !entry.isDirectory, onClick = onOpen)
            .padding(start = indent, top = 6.dp, bottom = 6.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
        Text(
            text = entry.name,
            color = if (entry.isDirectory) NovaTextPrimary else NovaTextSecondary,
            fontSize = 13.sp,
            fontWeight = if (entry.isDirectory) FontWeight.SemiBold else FontWeight.Normal,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DiffInspectorView(
    changes: List<ChangeItem>,
    onKeepFile: (String) -> Unit,
    onUndoFile: (String) -> Unit,
    onKeepAll: () -> Unit,
    onUndoAll: () -> Unit
) {
    if (changes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Check, contentDescription = null, tint = NovaEmerald, modifier = Modifier.size(36.dp))
                Text("Workspace is clean. No uncommitted diffs.", color = NovaTextMuted, fontSize = 13.sp)
            }
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        // Bulk actions
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${changes.size} modified files", color = NovaTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NovaEmerald.copy(alpha = 0.15f))
                        .clickable(onClick = onKeepAll)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Accept All", color = NovaEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NovaRose.copy(alpha = 0.15f))
                        .clickable(onClick = onUndoAll)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("Revert All", color = NovaRose, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(changes) { change ->
                DiffFileCard(
                    change = change,
                    onKeep = { onKeepFile(change.path) },
                    onUndo = { onUndoFile(change.path) }
                )
            }
        }
    }
}

@Composable
private fun DiffFileCard(
    change: ChangeItem,
    onKeep: () -> Unit,
    onUndo: () -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    NovaGlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f).clickable { expanded = !expanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Code, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(15.dp))
                    Text(
                        text = change.path,
                        color = NovaTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.weight(1f)
                    )
                    Text("+${change.additions}", color = NovaEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text("-${change.deletions}", color = NovaRose, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(start = 8.dp)) {
                    IconButton(onClick = onKeep, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Check, contentDescription = "Accept", tint = NovaEmerald, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onUndo, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Revert", tint = NovaRose, modifier = Modifier.size(16.dp))
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(NovaObsidian)
                        .horizontalScroll(rememberScrollState())
                        .padding(8.dp)
                ) {
                    change.diffLines.take(150).forEach { line ->
                        val (lineBg, lineText) = when (line.type) {
                            DiffLineType.ADDITION -> Pair(NovaEmerald.copy(alpha = 0.15f), NovaEmerald)
                            DiffLineType.DELETION -> Pair(NovaRose.copy(alpha = 0.15f), NovaRose)
                            DiffLineType.INFO -> Pair(NovaPurple.copy(alpha = 0.12f), NovaPurple)
                            DiffLineType.CONTEXT -> Pair(Color.Transparent, NovaTextSecondary)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(lineBg)
                                .padding(vertical = 1.dp)
                        ) {
                            Text(
                                text = line.text,
                                color = lineText,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeEditorView(
    filePath: String,
    initialContent: String,
    isLoading: Boolean,
    onClose: () -> Unit,
    onSave: (String) -> Unit
) {
    var content by remember(initialContent) { mutableStateOf(initialContent) }

    Column(modifier = Modifier.fillMaxSize().background(NovaObsidian)) {
        // Editor Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurfaceElevated)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = onClose, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = NovaTextSecondary, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = filePath.substringAfterLast('/'),
                    color = NovaTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            IconButton(onClick = { onSave(content) }, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Save, contentDescription = "Save", tint = NovaCyan, modifier = Modifier.size(16.dp))
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = NovaCyan)
            }
        } else {
            val lines = remember(content) { content.lines() }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
                    .horizontalScroll(rememberScrollState())
            ) {
                // Line numbers
                Column(modifier = Modifier.padding(end = 12.dp)) {
                    lines.indices.forEach { idx ->
                        Text(
                            text = "${idx + 1}",
                            color = NovaTextMuted,
                            fontSize = 11.5.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 18.sp
                        )
                    }
                }
                // Editable Content
                BasicTextField(
                    value = content,
                    onValueChange = { content = it },
                    textStyle = TextStyle(
                        color = NovaTextPrimary,
                        fontSize = 11.5.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 18.sp
                    ),
                    cursorBrush = SolidColor(NovaCyan),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
