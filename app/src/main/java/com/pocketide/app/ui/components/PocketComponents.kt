package com.pocketide.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pocketide.app.model.ActivityItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketide.app.ui.theme.PocketAmber
import com.pocketide.app.ui.theme.PocketBorder
import com.pocketide.app.ui.theme.PocketBorderGlow
import com.pocketide.app.ui.theme.PocketCardGradient
import com.pocketide.app.ui.theme.PocketCyan
import com.pocketide.app.ui.theme.PocketEmerald
import com.pocketide.app.ui.theme.PocketIndigo
import com.pocketide.app.ui.theme.PocketObsidian
import com.pocketide.app.ui.theme.PocketPrimaryBlue
import com.pocketide.app.ui.theme.PocketPurple
import com.pocketide.app.ui.theme.PocketRose
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceVariant
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary

/**
 * Clean developer surface card with subtle neutral border.
 */
@Composable
fun PocketGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = PocketBorder,
    glowEffect: Boolean = false,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(PocketSurface)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        content()
    }
}

/**
 * Clean Status Indicator Badge for live Agent / Runtime states.
 */
@Composable
fun PocketStatusPill(
    statusText: String,
    isRunning: Boolean = false,
    color: Color = PocketEmerald,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
            .padding(horizontal = 7.dp, vertical = 2.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = statusText,
            color = color,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.3.sp
        )
    }
}

/**
 * Virtual Developer Keyboard Strip for mobile coders.
 * Offers fast access to Esc, Tab, Ctrl, pipe, brackets, arrows, and quick terminal snippets.
 */
@Composable
fun DeveloperKeyToolbar(
    modifier: Modifier = Modifier,
    onKeyPress: (String) -> Unit,
    onSnippetRun: (String) -> Unit = {}
) {
    val devKeys = listOf(
        "ESC" to "\u001B",
        "TAB" to "\t",
        "CTRL-C" to "\u0003",
        "CTRL-D" to "\u0004",
        "|" to "|",
        "~" to "~",
        "/" to "/",
        "-" to "-",
        "_" to "_",
        ":" to ":",
        "$" to "$",
        "`" to "`",
        "\\" to "\\",
        "↑" to "\u001B[A",
        "↓" to "\u001B[B",
        "←" to "\u001B[D",
        "→" to "\u001B[C",
    )

    val quickSnippets = listOf(
        "npm run dev" to "npm run dev",
        "git status" to "git status",
        "git diff" to "git diff",
        "ls -la" to "ls -la",
        "clear" to "clear",
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(PocketSurface)
            .border(
                width = 1.dp,
                color = PocketBorder,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            )
            .padding(vertical = 4.dp)
    ) {
        // Dev Keys Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 6.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            devKeys.forEach { (label, value) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .clickable { onKeyPress(value) }
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = PocketTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Quick Snippets Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 6.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            quickSnippets.forEach { (label, command) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .clickable { onSnippetRun(command) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = PocketTextSecondary,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = label,
                            color = PocketTextSecondary,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

/**
 * Compact Thinking & Reasoning Chain Block.
 * Collapses by default once finished into a subtle "✓ Planned approach",
 * without polluting the screen with raw reasoning text unless explicitly tapped.
 */
@Composable
fun ReasoningChainBlock(
    reasoningText: String,
    isStreaming: Boolean = false,
    tokenCount: Int = 0,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(PocketSurfaceElevated)
            .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isStreaming) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(11.dp),
                        strokeWidth = 1.5.dp,
                        color = PocketPrimaryBlue
                    )
                    Text(
                        text = "Thinking…",
                        color = PocketTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = PocketEmerald,
                        modifier = Modifier.size(11.dp)
                    )
                    Text(
                        text = "Planned approach",
                        color = PocketTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (tokenCount > 0) {
                    Text(
                        text = "(${tokenCount}t)",
                        color = PocketTextMuted,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = PocketTextMuted,
                modifier = Modifier.size(13.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 6.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(PocketObsidian)
                        .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = reasoningText.ifBlank { "Analyzing workspace context and planning actions..." },
                        color = PocketTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

/**
 * Compact Activity Timeline.
 * Replaces heavy card-per-tool execution blocks with a slim, collapsible timeline.
 */
@Composable
fun CompactActivityTimeline(
    items: List<ActivityItem>,
    modifier: Modifier = Modifier,
    isLive: Boolean = false,
) {
    if (items.isEmpty()) return

    var expandedGroup by remember { mutableStateOf(isLive) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(PocketSurfaceElevated.copy(alpha = 0.5f))
            .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (!isLive) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedGroup = !expandedGroup }
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = PocketEmerald,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = if (items.size == 1) "1 activity completed" else "${items.size} activities completed",
                        color = PocketTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Icon(
                    imageVector = if (expandedGroup) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = PocketTextMuted,
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        if (isLive || expandedGroup) {
            items.forEach { item ->
                CompactTimelineRow(item = item)
            }
        }
    }
}

@Composable
fun CompactTimelineRow(
    item: ActivityItem,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val isRunning = !item.isComplete
    val isError = item.title.contains("failed", ignoreCase = true) || item.title.contains("error", ignoreCase = true)

    val cleanLabel = formatActivityLabel(item)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(enabled = item.detail.isNotBlank()) { expanded = !expanded }
            .padding(horizontal = 4.dp, vertical = 3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(11.dp),
                        strokeWidth = 1.5.dp,
                        color = PocketEmerald
                    )
                } else if (isError) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = PocketRose,
                        modifier = Modifier.size(11.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = PocketEmerald,
                        modifier = Modifier.size(11.dp)
                    )
                }

                Text(
                    text = cleanLabel,
                    color = if (isRunning) PocketEmerald else PocketTextPrimary,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (item.detail.isNotBlank()) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = PocketTextMuted,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        if (expanded && item.detail.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(PocketObsidian)
                    .border(1.dp, PocketBorder, RoundedCornerShape(4.dp))
                    .padding(6.dp)
            ) {
                Text(
                    text = item.detail,
                    color = PocketTextSecondary,
                    fontSize = 10.5.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

private fun formatActivityLabel(item: ActivityItem): String {
    val title = item.title.trim()
    val detail = item.detail.trim()

    return when {
        title.contains("Think", ignoreCase = true) -> {
            if (!item.isComplete) "Thinking…" else "Think"
        }
        title.contains("Build", ignoreCase = true) -> {
            if (!item.isComplete) "Building project…" else "Build complete"
        }
        title.contains("Git", ignoreCase = true) -> {
            val summary = detail.lineSequence().firstOrNull()?.trim()?.take(40) ?: "operation"
            if (!item.isComplete) "Git $summary" else "Git $summary"
        }
        title.contains("Preview ready", ignoreCase = true) -> "Started preview: ${detail.ifBlank { "localhost" }}"
        title.contains("Bash", ignoreCase = true) || item.isCommand -> {
            val cmd = detail.lineSequence().firstOrNull()?.trim()?.take(45) ?: "command"
            if (!item.isComplete) "Running $cmd" else "Ran $cmd"
        }
        title.contains("Read", ignoreCase = true) -> {
            val file = detail.substringAfterLast('/').take(35)
            if (!item.isComplete) "Reading $file" else "Read $file"
        }
        title.contains("Write", ignoreCase = true) || title.contains("Edit", ignoreCase = true) -> {
            val file = detail.substringAfterLast('/').take(35)
            if (!item.isComplete) "Writing $file" else "Saved $file"
        }
        title.contains("Files changed", ignoreCase = true) -> "Updated $detail"
        else -> {
            val clean = title.removeSuffix(" completed").removePrefix("Running ")
            if (!item.isComplete) "Running $clean" else clean
        }
    }
}

/**
 * Compact Tool Execution Card.
 * Uses a subtle ✓ indicator instead of large green "DONE" pills.
 */
@Composable
fun ToolExecutionCard(
    toolName: String,
    detail: String,
    isComplete: Boolean = true,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    CompactTimelineRow(
        item = ActivityItem(
            title = toolName,
            detail = detail,
            isComplete = isComplete,
            isCommand = toolName.contains("bash", ignoreCase = true) || toolName.contains("command", ignoreCase = true)
        ),
        modifier = modifier
    )
}

/**
 * Minimal, Tactile Solid Primary Action Button.
 * Primary blue action button with crisp text and high accessibility.
 */
@Composable
fun PocketPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: androidx.compose.ui.unit.Dp = 40.dp,
) {
    Box(
        modifier = modifier
            .height(height)
            .liquidBounceClick(onClick = onClick, enabled = enabled)
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (enabled) PocketPrimaryBlue else PocketPrimaryBlue.copy(alpha = 0.35f)
            )
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Minimal, Tactile Secondary Action Button.
 * Clean neutral elevated surface with subtle border and readable text.
 */
@Composable
fun PocketSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: androidx.compose.ui.unit.Dp = 40.dp,
) {
    Box(
        modifier = modifier
            .height(height)
            .liquidBounceClick(onClick = onClick, enabled = enabled)
            .clip(RoundedCornerShape(8.dp))
            .background(PocketSurfaceVariant)
            .border(1.dp, PocketBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PocketTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = PocketTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

