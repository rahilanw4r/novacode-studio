package com.novacode.studio.ui.screens.chat

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
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacode.studio.model.ActivityItem
import com.novacode.studio.model.ChatAttachment
import com.novacode.studio.model.ChatMessage
import com.novacode.studio.model.ToolRequest
import com.novacode.studio.ui.MarkdownText
import com.novacode.studio.ui.components.NovaGlassCard
import com.novacode.studio.ui.components.NovaStatusPill
import com.novacode.studio.ui.components.ReasoningChainBlock
import com.novacode.studio.ui.components.ToolExecutionCard
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

@Composable
fun NovaAgentChatScreen(
    messages: List<ChatMessage>,
    liveProcess: List<ActivityItem>,
    liveThinking: Boolean,
    liveThinkingSummary: String,
    liveThinkingTokens: Int,
    isRunning: Boolean,
    pendingApproval: ToolRequest?,
    attachments: List<ChatAttachment>,
    onSend: (String) -> Unit,
    onStop: () -> Unit,
    onApproval: (Boolean) -> Unit,
    onPickAttachment: () -> Unit,
    onRemoveAttachment: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var promptInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val quickPrompts = listOf(
        "⚡ Run tests and fix errors",
        "🔍 Audit project architecture",
        "🚀 Start dev server and preview",
        "📝 Generate git commit message",
        "📱 Build on-device Android APK"
    )

    LaunchedEffect(messages.size, liveProcess.size, liveThinkingSummary) {
        val total = messages.size + (if (liveProcess.isNotEmpty() || liveThinking) 1 else 0)
        if (total > 0) {
            listState.animateScrollToItem(total - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian)
            .imePadding()
    ) {
        // Chat Messages Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (messages.isEmpty() && liveProcess.isEmpty() && !liveThinking) {
                item {
                    EmptyChatGreeting(onSelectPrompt = { promptInput = it })
                }
            }

            items(messages) { message ->
                ChatMessageItem(message = message)
            }

            // Live reasoning stream
            if (liveThinking || liveThinkingSummary.isNotBlank()) {
                item {
                    ReasoningChainBlock(
                        reasoningText = liveThinkingSummary,
                        isStreaming = liveThinking,
                        tokenCount = liveThinkingTokens
                    )
                }
            }

            // Live tool execution items
            if (liveProcess.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        liveProcess.forEach { item ->
                            ToolExecutionCard(
                                toolName = item.title,
                                detail = item.detail,
                                isComplete = item.isComplete
                            )
                        }
                    }
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
                            .clip(RoundedCornerShape(16.dp))
                            .background(NovaSurfaceElevated)
                            .border(1.dp, NovaBorder, RoundedCornerShape(16.dp))
                            .clickable { promptInput = prompt.substringAfter(' ') }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = prompt,
                            color = NovaCyan,
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
                    .background(NovaSurface)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                attachments.forEach { att ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaSurfaceElevated)
                            .border(1.dp, NovaBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.AttachFile, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(13.dp))
                        Text(att.displayName, color = NovaTextPrimary, fontSize = 11.sp, maxLines = 1)
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = NovaTextMuted,
                            modifier = Modifier.size(14.dp).clickable { onRemoveAttachment(att.id) }
                        )
                    }
                }
            }
        }

        // Input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurface)
                .border(1.dp, NovaBorder)
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onPickAttachment,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(Icons.Default.AttachFile, contentDescription = "Attach", tint = NovaTextSecondary, modifier = Modifier.size(18.dp))
            }

            BasicTextField(
                value = promptInput,
                onValueChange = { promptInput = it },
                textStyle = TextStyle(
                    color = NovaTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                ),
                cursorBrush = SolidColor(NovaCyan),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        val text = promptInput.trim()
                        if (text.isNotEmpty() && !isRunning) {
                            onSend(text)
                            promptInput = ""
                        }
                    }
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (promptInput.isEmpty()) {
                        Text(
                            text = if (isRunning) "Agent is executing tasks…" else "Ask agent to write, refactor, or build…",
                            color = NovaTextMuted,
                            fontSize = 13.sp
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
                        .background(NovaRose)
                ) {
                    Icon(Icons.Default.Stop, contentDescription = "Stop", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            } else {
                IconButton(
                    onClick = {
                        val text = promptInput.trim()
                        if (text.isNotEmpty()) {
                            onSend(text)
                            promptInput = ""
                        }
                    },
                    enabled = promptInput.isNotBlank(),
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (promptInput.isNotBlank()) NovaIndigo else NovaSurfaceElevated)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (promptInput.isNotBlank()) Color.White else NovaTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
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
            .padding(vertical = 24.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(NovaIndigo.copy(alpha = 0.15f))
                .border(1.dp, NovaIndigo.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NovaCyan, modifier = Modifier.size(28.dp))
        }
        Text(
            text = "NovaCode Autonomous Agent",
            color = NovaTextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Your on-device developer pairing partner. What are we building today?",
            color = NovaTextSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

@Composable
private fun ChatMessageItem(message: ChatMessage) {
    val isUser = message.fromUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(if (isUser) 0.85f else 0.95f)
                .clip(
                    RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 14.dp
                    )
                )
                .background(if (isUser) NovaIndigo.copy(alpha = 0.18f) else NovaSurfaceElevated)
                .border(
                    width = 1.dp,
                    color = if (isUser) NovaIndigo.copy(alpha = 0.4f) else NovaBorder,
                    shape = RoundedCornerShape(
                        topStart = 14.dp,
                        topEnd = 14.dp,
                        bottomStart = if (isUser) 14.dp else 2.dp,
                        bottomEnd = if (isUser) 2.dp else 14.dp
                    )
                )
                .padding(12.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Header (Sender title)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUser) "You" else "Nova Agent",
                        color = if (isUser) NovaCyan else NovaIndigo,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Message Text / Markdown
                if (isUser) {
                    Text(
                        text = message.text,
                        color = NovaTextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                } else {
                    MarkdownText(
                        markdown = message.text,
                        color = NovaTextPrimary,
                        fontSize = 13.sp
                    )
                }

                // Completed Work items summary
                if (message.workItems.isNotEmpty()) {
                    Column(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        message.workItems.forEach { work ->
                            ToolExecutionCard(
                                toolName = work.title,
                                detail = work.detail,
                                isComplete = work.isComplete
                            )
                        }
                    }
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
    NovaGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = NovaAmber,
        glowEffect = true
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = NovaAmber, modifier = Modifier.size(18.dp))
                Text(
                    text = "Permission Request: ${request.toolName}",
                    color = NovaAmber,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = request.explanation,
                color = NovaTextPrimary,
                fontSize = 12.sp
            )
            request.commandPreview?.let { cmd ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(NovaObsidian)
                        .padding(8.dp)
                ) {
                    Text(
                        text = cmd,
                        color = NovaCyan,
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
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaRose.copy(alpha = 0.2f))
                            .border(1.dp, NovaRose, RoundedCornerShape(8.dp))
                            .clickable(onClick = onReject)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Deny", color = NovaRose, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(NovaEmerald)
                            .clickable(onClick = onApprove)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Allow", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
