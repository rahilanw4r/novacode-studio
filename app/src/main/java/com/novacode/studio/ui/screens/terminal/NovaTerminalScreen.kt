package com.novacode.studio.ui.screens.terminal

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.novacode.studio.ui.TerminalOutputLine
import com.novacode.studio.ui.components.DeveloperKeyToolbar
import com.novacode.studio.ui.components.NovaStatusPill
import com.novacode.studio.ui.theme.NovaAmber
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaObsidian
import com.novacode.studio.ui.theme.NovaRose
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary

data class TerminalTabSession(
    val id: String,
    val title: String,
    val isRunning: Boolean = false
)

@Composable
fun NovaTerminalScreen(
    outputLines: List<TerminalOutputLine>,
    activeCommand: String?,
    isProcessRunning: Boolean,
    onRunCommand: (String) -> Unit,
    onSendInput: (String) -> Unit,
    onInterrupt: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var commandInput by remember { mutableStateOf("") }
    val history = remember { mutableStateListOf<String>() }
    var historyIndex by remember { mutableIntStateOf(-1) }
    val listState = rememberLazyListState()

    val sessions = remember {
        mutableStateListOf(
            TerminalTabSession("main", "bash: 1", isRunning = isProcessRunning),
            TerminalTabSession("server", "dev server", isRunning = false)
        )
    }
    var activeSessionId by remember { mutableStateOf("main") }

    LaunchedEffect(outputLines.size) {
        if (outputLines.isNotEmpty()) {
            listState.animateScrollToItem(outputLines.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian)
            .imePadding()
    ) {
        // Multi-Tab Session Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurface)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                sessions.forEach { session ->
                    val isSelected = session.id == activeSessionId
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NovaSurfaceElevated else Color.Transparent)
                            .border(1.dp, if (isSelected) NovaCyan else Color.Transparent, RoundedCornerShape(8.dp))
                            .clickable { activeSessionId = session.id }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (isProcessRunning) NovaEmerald else NovaTextMuted)
                        )
                        Text(
                            text = session.title,
                            color = if (isSelected) NovaTextPrimary else NovaTextSecondary,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Quick actions (Clear & Interrupt)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                if (isProcessRunning) {
                    IconButton(onClick = onInterrupt, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Stop, contentDescription = "Interrupt", tint = NovaRose, modifier = Modifier.size(16.dp))
                    }
                }
                IconButton(onClick = onClear, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = NovaTextMuted, modifier = Modifier.size(16.dp))
                }
            }
        }

        HorizontalDivider(color = NovaBorder)

        // Terminal Output Stream
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (outputLines.isEmpty()) {
                item {
                    Text(
                        text = "Pocket IDE Linux Subsystem (Ubuntu PRoot ARM64)\nType a command below or tap a quick snippet.\n",
                        color = NovaCyan,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            items(outputLines) { line ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                    // Command prompt header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "ubuntu@phone:~$",
                            color = NovaIndigo,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = line.command,
                            color = NovaTextPrimary,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace
                        )
                        if (line.exitCode != 0) {
                            NovaStatusPill("exit ${line.exitCode}", color = NovaRose)
                        }
                    }

                    // Command Output
                    if (line.output.isNotBlank()) {
                        Text(
                            text = line.output,
                            color = NovaTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                        )
                    }
                }
            }
        }

        // Active command running indicator
        if (isProcessRunning && activeCommand != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NovaSurfaceElevated)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NovaStatusPill("RUNNING", isRunning = true, color = NovaCyan)
                Text(
                    text = activeCommand,
                    color = NovaTextSecondary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Command Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NovaSurface)
                .border(1.dp, NovaBorder)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$",
                color = NovaCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            BasicTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                textStyle = TextStyle(
                    color = NovaTextPrimary,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace
                ),
                cursorBrush = SolidColor(NovaCyan),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        val trimmed = commandInput.trim()
                        if (trimmed.isNotEmpty()) {
                            history.add(trimmed)
                            historyIndex = -1
                            if (isProcessRunning) {
                                onSendInput(trimmed + "\n")
                            } else {
                                onRunCommand(trimmed)
                            }
                            commandInput = ""
                        }
                    }
                ),
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    val trimmed = commandInput.trim()
                    if (trimmed.isNotEmpty()) {
                        history.add(trimmed)
                        historyIndex = -1
                        if (isProcessRunning) {
                            onSendInput(trimmed + "\n")
                        } else {
                            onRunCommand(trimmed)
                        }
                        commandInput = ""
                    }
                },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Execute",
                    tint = NovaCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Virtual Developer Keyboard Strip
        DeveloperKeyToolbar(
            onKeyPress = { key ->
                if (isProcessRunning) {
                    onSendInput(key)
                } else {
                    commandInput += key
                }
            },
            onSnippetRun = { snippet ->
                if (isProcessRunning) {
                    onSendInput(snippet + "\n")
                } else {
                    onRunCommand(snippet)
                }
            }
        )
    }
}
