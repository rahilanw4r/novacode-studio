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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.pocketide.app.ui.theme.PocketPurple
import com.pocketide.app.ui.theme.PocketRose
import com.pocketide.app.ui.theme.PocketSurface
import com.pocketide.app.ui.theme.PocketSurfaceVariant
import com.pocketide.app.ui.theme.PocketSurfaceElevated
import com.pocketide.app.ui.theme.PocketTextMuted
import com.pocketide.app.ui.theme.PocketTextPrimary
import com.pocketide.app.ui.theme.PocketTextSecondary

/**
 * Minimal tactile surface card with subtle neutral border.
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
            .clip(RoundedCornerShape(12.dp))
            .background(PocketSurfaceVariant)
            .border(
                width = 1.dp,
                color = if (glowEffect) PocketBorderGlow else borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(14.dp)
    ) {
        content()
    }
}

/**
 * Pulsing Status Indicator Badge for live Agent / Runtime states.
 */
@Composable
fun PocketStatusPill(
    statusText: String,
    isRunning: Boolean = false,
    color: Color = PocketEmerald,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by if (isRunning) {
        infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(800),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseAlpha"
        )
    } else {
        remember { mutableStateOf(1.0f) }
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = alpha))
        )
        Text(
            text = statusText,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
            maxLines = 1
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
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .padding(vertical = 4.dp)
    ) {
        // Dev Keys Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 6.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            devKeys.forEach { (label, value) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PocketSurfaceElevated)
                        .border(1.dp, PocketBorder, RoundedCornerShape(6.dp))
                        .clickable { onKeyPress(value) }
                        .padding(horizontal = 9.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = PocketCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
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
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            quickSnippets.forEach { (label, command) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PocketIndigo.copy(alpha = 0.15f))
                        .border(1.dp, PocketIndigo.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
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
                            tint = PocketIndigo,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = label,
                            color = PocketTextPrimary,
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
 * Collapsible Thinking & Reasoning Chain Block (DeepSeek R1 / Claude 3.7 style).
 */
@Composable
fun ReasoningChainBlock(
    reasoningText: String,
    isStreaming: Boolean = false,
    tokenCount: Int = 0,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(isStreaming) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PocketPurple.copy(alpha = 0.08f))
            .border(1.dp, PocketPurple.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            .padding(10.dp)
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = PocketPurple,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (isStreaming) "Thinking process…" else "Reasoning completed",
                    color = PocketPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (tokenCount > 0) {
                    Text(
                        text = "($tokenCount tokens)",
                        color = PocketTextMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = PocketPurple,
                modifier = Modifier.size(18.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(PocketObsidian)
                        .padding(10.dp)
                ) {
                    Text(
                        text = reasoningText.ifBlank { "Analyzing workspace context and planning actions..." },
                        color = PocketTextSecondary,
                        fontSize = 11.5.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

/**
 * Tool Execution Card showing command execution, file modifications, or test results.
 */
@Composable
fun ToolExecutionCard(
    toolName: String,
    detail: String,
    isComplete: Boolean = true,
    isError: Boolean = false,
    modifier: Modifier = Modifier
) {
    val statusColor = when {
        isError -> PocketRose
        !isComplete -> PocketAmber
        else -> PocketEmerald
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(PocketSurfaceElevated)
            .border(1.dp, statusColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = when {
                toolName.contains("bash", ignoreCase = true) -> Icons.Default.Terminal
                toolName.contains("file", ignoreCase = true) -> Icons.Default.Code
                else -> Icons.Default.AutoAwesome
            },
            contentDescription = null,
            tint = statusColor,
            modifier = Modifier.size(16.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = toolName,
                color = PocketTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = detail,
                color = PocketTextSecondary,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                maxLines = 2
            )
        }
        PocketStatusPill(
            statusText = if (isComplete) "DONE" else "RUNNING",
            isRunning = !isComplete,
            color = statusColor
        )
    }
}

/**
 * Minimal, Tactile Solid Primary Action Button.
 * Clean, solid, human-friendly button with no harsh gradients.
 */
@Composable
fun PocketPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: androidx.compose.ui.unit.Dp = 42.dp,
) {
    Box(
        modifier = modifier
            .height(height)
            .liquidBounceClick(onClick = onClick, enabled = enabled)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (enabled) PocketEmerald else PocketEmerald.copy(alpha = 0.35f)
            )
            .padding(horizontal = 10.dp),
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
                    tint = Color(0xFF0F172A),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = Color(0xFF0F172A),
                fontSize = 12.5.sp,
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
 * Clean neutral surface with subtle border and readable text.
 */
@Composable
fun PocketSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    height: androidx.compose.ui.unit.Dp = 42.dp,
) {
    Box(
        modifier = modifier
            .height(height)
            .liquidBounceClick(onClick = onClick, enabled = enabled)
            .clip(RoundedCornerShape(10.dp))
            .background(PocketSurfaceVariant)
            .border(1.dp, PocketBorder, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp),
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
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

