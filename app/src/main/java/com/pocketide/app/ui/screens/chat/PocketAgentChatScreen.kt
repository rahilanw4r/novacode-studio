package com.pocketide.app.ui.screens.chat

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import android.widget.Toast
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketide.app.model.ActivityItem
import com.pocketide.app.model.AgentTaskLifecycle
import com.pocketide.app.model.ChatAttachment
import com.pocketide.app.model.ChatMessage
import com.pocketide.app.model.ToolRequest
import com.pocketide.app.ui.MarkdownText
import com.pocketide.app.ui.components.CompactActivityTimeline
import com.pocketide.app.ui.components.PocketGlassCard
import com.pocketide.app.ui.components.PocketStatusPill
import com.pocketide.app.ui.components.ReasoningChainBlock
import com.pocketide.app.ui.components.ToolExecutionCard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import com.pocketide.app.ui.theme.PocketAmber
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketCyan
import com.pocketide.app.ui.theme.PocketEmerald
import com.pocketide.app.ui.theme.PocketIndigo
import com.pocketide.app.ui.theme.PocketObsidian
import com.pocketide.app.ui.theme.PocketPurple
import com.pocketide.app.ui.theme.PocketRose
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary
import com.pocketide.app.ui.theme.PocketPrimaryBlue

@Composable
fun PocketAgentChatScreen(
    messages: List<ChatMessage>,
    liveProcess: List<ActivityItem>,
    liveThinking: Boolean,
    liveThinkingSummary: String,
    liveThinkingTokens: Int,
    isRunning: Boolean,
    taskLifecycle: AgentTaskLifecycle = AgentTaskLifecycle.IDLE,
    taskFailureReason: String? = null,
    taskFailureDetails: String? = null,
    pendingApproval: ToolRequest?,
    attachments: List<ChatAttachment>,
    onSend: (String) -> Unit,
    onStop: () -> Unit,
    onRetry: (() -> Unit)? = null,
    onChangeModel: (() -> Unit)? = null,
    onChangeProvider: (() -> Unit)? = null,
    onContinue: (() -> Unit)? = null,
    onClearFailure: (() -> Unit)? = null,
    onApproval: (Boolean) -> Unit,
    onPickAttachment: () -> Unit,
    onRemoveAttachment: (String) -> Unit,
    previewReady: Boolean = false,
    previewUrl: String? = null,
    onOpenPreview: (() -> Unit)? = null,
    onOpenFiles: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var promptInput by remember { mutableStateOf("") }
    var isEditingPrompt by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val listState = rememberLazyListState()

    val quickPrompts = listOf(
        "⚡ Run tests and fix errors",
        "🔍 Inspect project architecture",
        "🚀 Start dev server",
        "📝 Review git changes",
        "📱 Build Android APK"
    )

    LaunchedEffect(messages.size, liveProcess.size, liveThinkingSummary, taskLifecycle) {
        val total = messages.size + (if (liveProcess.isNotEmpty() || liveThinking || (taskLifecycle == AgentTaskLifecycle.FAILED && taskFailureReason != null)) 1 else 0)
        if (total > 0) {
            listState.animateScrollToItem(total - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PocketObsidian)
            .imePadding()
    ) {
        val visibleMessages = remember(messages) {
            messages.filterNot { msg ->
                !msg.fromUser && msg.text.isBlank() && msg.workItems.isEmpty() && !msg.isTaskFailed && msg.changedFiles.isEmpty()
            }
        }

        // Chat Messages Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (visibleMessages.isEmpty() && liveProcess.isEmpty() && !liveThinking && taskLifecycle != AgentTaskLifecycle.FAILED) {
                item {
                    EmptyChatGreeting(onSelectPrompt = { promptInput = it })
                }
            }

            items(visibleMessages) { message ->
                ChatMessageItem(
                    message = message,
                    previewReady = previewReady,
                    onOpenPreview = onOpenPreview,
                    onOpenFiles = onOpenFiles,
                    onRetry = onRetry,
                    onChangeModel = onChangeModel,
                    onChangeProvider = onChangeProvider,
                    onContinue = onContinue,
                    onEditPrompt = { text ->
                        promptInput = text
                        isEditingPrompt = true
                        focusRequester.requestFocus()
                    }
                )
            }

            // Live reasoning stream: shown ONLY while actively thinking
            if (liveThinking && liveThinkingSummary.isNotBlank()) {
                item {
                    ReasoningChainBlock(
                        reasoningText = liveThinkingSummary,
                        isStreaming = true,
                        tokenCount = liveThinkingTokens
                    )
                }
            }

            // Live tool execution items: compact timeline (filter out Think item while liveThinking is streaming)
            val filteredLiveProcess = if (liveThinking) liveProcess.filterNot { it.title == "Think" } else liveProcess
            if (filteredLiveProcess.isNotEmpty()) {
                item {
                    CompactActivityTimeline(
                        items = filteredLiveProcess,
                        isLive = true
                    )
                }
            }

            // Live task failure recovery card (when task failed and not already rendered by a failed message)
            val lastIsFailed = visibleMessages.lastOrNull()?.isTaskFailed == true
            if (taskLifecycle == AgentTaskLifecycle.FAILED && taskFailureReason != null && !lastIsFailed) {
                item {
                    TaskFailureCard(
                        title = taskFailureReason,
                        details = taskFailureDetails,
                        onRetry = onRetry,
                        onChangeModel = onChangeModel,
                        onChangeProvider = onChangeProvider,
                        onContinue = onContinue,
                        onDismiss = onClearFailure,
                    )
                }
            }

            // Pending Tool Approval Alert
            if (pendingApproval != null) {
                item {
                    ToolApprovalCard(
                        request = pendingApproval,
                        onApprove = { onApproval(true) },
                        onReject = { onApproval(false) }
                    )
                }
            }
        }

        // Quick Prompts Row (when idle)
        if (!isRunning && messages.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickPrompts.forEach { prompt ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                            .clickable { promptInput = prompt.substringAfter(' ') }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = prompt,
                            color = PocketTextSecondary,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Attachments preview chips
        if (attachments.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PocketSurface)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                attachments.forEach { att ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.AttachFile, contentDescription = null, tint = PocketPrimaryBlue, modifier = Modifier.size(13.dp))
                        Text(att.displayName, color = PocketTextPrimary, fontSize = 11.sp, maxLines = 1)
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = PocketTextMuted,
                            modifier = Modifier.size(14.dp).clickable { onRemoveAttachment(att.id) }
                        )
                    }
                }
            }
        }

        // Editing prompt indicator bar
        if (isEditingPrompt) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PocketSurfaceElevated)
                    .border(1.dp, PocketBorder)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = PocketPrimaryBlue,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Editing prompt",
                        color = PocketPrimaryBlue,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel edit",
                    tint = PocketTextMuted,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            isEditingPrompt = false
                            promptInput = ""
                        }
                )
            }
        }

        // Live Agent Working Indicator
        if (isRunning || liveThinking) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    strokeWidth = 2.dp,
                    color = PocketPrimaryBlue
                )
                Text(
                    text = when {
                        liveThinking -> "Thinking & planning…"
                        liveProcess.isNotEmpty() -> {
                            val last = liveProcess.lastOrNull()
                            if (last != null && last.title.isNotBlank()) "${last.title} · ${last.detail.take(35)}" else "Agent is executing code…"
                        }
                        else -> "Working on your request…"
                    },
                    color = PocketTextSecondary,
                    fontSize = 11.5.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // ChatGPT style floating capsule input bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(26.dp))
                    .background(PocketSurfaceElevated)
                    .border(1.dp, PocketBorder, RoundedCornerShape(26.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onPickAttachment,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Attach",
                        tint = PocketTextSecondary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                BasicTextField(
                    value = promptInput,
                    onValueChange = { promptInput = it },
                    textStyle = TextStyle(
                        color = PocketTextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    cursorBrush = SolidColor(PocketPrimaryBlue),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            val text = promptInput.trim()
                            if (text.isNotEmpty() && !isRunning) {
                                onSend(text)
                                promptInput = ""
                                isEditingPrompt = false
                            }
                        }
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    decorationBox = { inner ->
                        if (promptInput.isEmpty()) {
                            Text(
                                text = if (isRunning) "Agent is thinking…" else "Message Agent…",
                                color = PocketTextMuted,
                                fontSize = 14.sp
                            )
                        }
                        inner()
                    }
                )

                if (isRunning) {
                    IconButton(
                        onClick = onStop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PocketRose)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                } else {
                    val hasText = promptInput.isNotBlank()
                    IconButton(
                        onClick = {
                            val text = promptInput.trim()
                            if (text.isNotEmpty()) {
                                onSend(text)
                                promptInput = ""
                                isEditingPrompt = false
                            }
                        },
                        enabled = hasText,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (hasText) Color.White else Color(0xFF383838))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (hasText) Color.Black else PocketTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyChatGreeting(onSelectPrompt: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(PocketSurfaceElevated)
                .border(1.dp, PocketBorder, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Terminal, contentDescription = null, tint = PocketPrimaryBlue, modifier = Modifier.size(22.dp))
        }
        Text(
            text = "Autonomous Development Session",
            color = PocketTextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Instruct the agent to inspect files, edit code, execute terminal commands, build, or fix errors.",
            color = PocketTextSecondary,
            fontSize = 12.5.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ChatMessageItem(
    message: ChatMessage,
    previewReady: Boolean = false,
    onOpenPreview: (() -> Unit)? = null,
    onOpenFiles: (() -> Unit)? = null,
    onRetry: (() -> Unit)? = null,
    onChangeModel: (() -> Unit)? = null,
    onChangeProvider: (() -> Unit)? = null,
    onContinue: (() -> Unit)? = null,
    onEditPrompt: ((String) -> Unit)? = null
) {
    val isUser = message.fromUser
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    // If task failed, render TaskFailureCard directly without double-nesting inside another bubble
    if (message.isTaskFailed) {
        TaskFailureCard(
            title = message.taskFailureReason ?: "Task failed",
            details = message.taskFailureDetails,
            onRetry = onRetry,
            onChangeModel = onChangeModel,
            onChangeProvider = onChangeProvider,
            onContinue = onContinue,
        )
        return
    }

    // Skip empty ghost messages completely
    if (!isUser && message.text.isBlank() && message.workItems.isEmpty() && message.changedFiles.isEmpty()) {
        return
    }

    if (isUser) {
        // User message: Authentic ChatGPT style - sleek rounded pill bubble, right-aligned, #2F2F2F background, no redundant "You" label
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PocketSurfaceElevated)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = message.text,
                        color = PocketTextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (onEditPrompt != null) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit prompt",
                                    tint = PocketTextMuted,
                                    modifier = Modifier
                                        .size(13.dp)
                                        .clickable { onEditPrompt(message.text) }
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy prompt",
                                tint = PocketTextMuted,
                                modifier = Modifier
                                    .size(13.dp)
                                    .clickable {
                                        clipboard.setText(AnnotatedString(message.text))
                                        Toast.makeText(context, "Prompt copied", Toast.LENGTH_SHORT).show()
                                    }
                            )
                        }
                    }
                }
            }
        }
    } else {
        // Agent message: Authentic ChatGPT style - full-width, clean borderless layout, rich typography
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Completed Work items (tools, bash, build, git): compact timeline
            if (message.workItems.isNotEmpty()) {
                CompactActivityTimeline(
                    items = message.workItems,
                    isLive = false
                )
            }

            // Message text/markdown
            if (message.text.isNotBlank()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MarkdownText(
                        markdown = message.text,
                        color = PocketTextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy response",
                            tint = PocketTextMuted,
                            modifier = Modifier
                                .size(14.dp)
                                .clickable {
                                    clipboard.setText(AnnotatedString(message.text))
                                    Toast.makeText(context, "Response copied", Toast.LENGTH_SHORT).show()
                                }
                        )
                    }
                }
            }

            // Changed files (only show if files actually changed)
            if (message.changedFiles.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Files (${message.changedFiles.size}):",
                        color = PocketTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                    message.changedFiles.take(5).forEach { filePath ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(PocketSurfaceElevated)
                                .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = filePath.substringAfterLast('/'),
                                color = PocketPrimaryBlue,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                    if (onOpenFiles != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(PocketPrimaryBlue.copy(alpha = 0.15f))
                                .border(1.dp, PocketPrimaryBlue.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                .clickable { onOpenFiles() }
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "View Files",
                                color = PocketPrimaryBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            } else if (message.noFilesReason != null && message.workItems.isNotEmpty()) {
                Text(
                    text = message.noFilesReason,
                    color = PocketTextMuted,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

            // Preview button (only if web preview is ready)
            if (previewReady && onOpenPreview != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                        .clickable { onOpenPreview() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = PocketPrimaryBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Open Preview",
                            color = PocketTextPrimary,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskFailureCard(
    title: String,
    details: String?,
    onRetry: (() -> Unit)? = null,
    onChangeModel: (() -> Unit)? = null,
    onChangeProvider: (() -> Unit)? = null,
    onContinue: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(PocketSurface)
            .border(1.dp, PocketRose.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = PocketRose,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = title,
                    color = PocketRose,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "· Task stopped",
                    color = PocketTextMuted,
                    fontSize = 11.sp
                )
            }
            if (onDismiss != null) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Dismiss",
                    tint = PocketTextMuted,
                    modifier = Modifier
                        .size(14.dp)
                        .clickable { onDismiss() }
                )
            }
        }

        if (!details.isNullOrBlank()) {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { expanded = !expanded }
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = if (expanded) "Hide details" else "Error details",
                    color = PocketPrimaryBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = PocketPrimaryBlue,
                    modifier = Modifier.size(12.dp)
                )
            }

            if (expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketObsidian)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = details,
                        color = PocketTextSecondary,
                        fontSize = 10.5.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        // Action Recovery Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onRetry != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketPrimaryBlue)
                        .clickable(onClick = onRetry)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Retry",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (onChangeModel != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .clickable(onClick = onChangeModel)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Change Model",
                        color = PocketTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (onChangeProvider != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .clickable(onClick = onChangeProvider)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Change Provider",
                        color = PocketTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (onContinue != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .clickable(onClick = onContinue)
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Continue",
                        color = PocketTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolApprovalCard(
    request: ToolRequest,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    PocketGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = PocketBorder,
        glowEffect = false
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = PocketTextSecondary, modifier = Modifier.size(16.dp))
                Text(
                    text = "Permission Request: ${request.toolName}",
                    color = PocketTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = request.explanation,
                color = PocketTextSecondary,
                fontSize = 12.sp
            )
            request.commandPreview?.let { cmd ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketObsidian)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = cmd,
                        color = PocketTextPrimary,
                        fontSize = 11.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PocketSurfaceElevated)
                            .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                            .clickable(onClick = onReject)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Deny", color = PocketRose, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PocketPrimaryBlue)
                            .clickable(onClick = onApprove)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Allow", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
