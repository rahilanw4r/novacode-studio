package com.novacode.studio.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaBorderGlow
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaPurple
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaSurfaceVariant
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary

/**
 * Apple-style Liquid Chromatic Aura Border.
 * Renders a continuously rotating fluid chromatic light wave around a shape.
 */
@Composable
fun Modifier.liquidAuraBorder(
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(16.dp),
    strokeWidth: Dp = 2.dp,
    intensity: Float = 0.85f,
): Modifier {
    if (!enabled) return this.border(1.dp, NovaBorder, shape)

    val infiniteTransition = rememberInfiniteTransition(label = "LiquidAuraTransition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "LiquidRotation"
    )

    val auraColors = listOf(
        NovaEmerald.copy(alpha = 0.9f * intensity),
        NovaCyan.copy(alpha = 0.85f * intensity),
        NovaIndigo.copy(alpha = 0.9f * intensity),
        NovaPurple.copy(alpha = 0.7f * intensity),
        NovaEmerald.copy(alpha = 0.9f * intensity),
    )

    return this
        .clip(shape)
        .drawWithContent {
            drawContent()
            rotate(rotation) {
                drawCircle(
                    brush = Brush.sweepGradient(auraColors),
                    radius = size.maxDimension,
                    style = Stroke(width = strokeWidth.toPx()),
                    blendMode = BlendMode.SrcOver
                )
            }
        }
        .border(1.dp, NovaBorder.copy(alpha = 0.6f), shape)
}

/**
 * Apple-style tactile bouncy spring clickable modifier.
 */
@Composable
fun Modifier.liquidBounceClick(
    onClick: () -> Unit,
    enabled: Boolean = true,
): Modifier {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "LiquidBounceScale"
    )

    return this
        .scale(scale)
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClick = onClick
        )
}

/**
 * Quick AI Action definition for Liquid Assistant.
 */
data class LiquidAiAction(
    val title: String,
    val prompt: String,
    val icon: ImageVector,
    val accentColor: Color,
)

/**
 * Apple Intelligence Style Liquid AI Assistant Bar.
 * Provides instant contextual prompts with fluid spring animations.
 */
@Composable
fun LiquidAiAssistantBar(
    onTriggerAction: (prompt: String) -> Unit,
    modifier: Modifier = Modifier,
    isThinking: Boolean = false,
) {
    val actions = remember {
        listOf(
            LiquidAiAction(
                title = "Auto-Fix",
                prompt = "Review this codebase, detect any syntax or runtime bugs, and provide the exact fix.",
                icon = Icons.Default.Bolt,
                accentColor = NovaEmerald
            ),
            LiquidAiAction(
                title = "Explain Logic",
                prompt = "Break down how this project operates step-by-step in clear, simple terms.",
                icon = Icons.Default.Psychology,
                accentColor = NovaIndigo
            ),
            LiquidAiAction(
                title = "Optimize",
                prompt = "Profile this code for performance bottlenecks, algorithmic complexity, and memory leaks.",
                icon = Icons.Default.Speed,
                accentColor = NovaCyan
            ),
            LiquidAiAction(
                title = "Security Audit",
                prompt = "Inspect this codebase for security vulnerabilities, API token leaks, and sandbox risks.",
                icon = Icons.Default.Security,
                accentColor = NovaPurple
            ),
            LiquidAiAction(
                title = "Write Tests",
                prompt = "Generate comprehensive automated unit tests covering all core functions and edge cases.",
                icon = Icons.Default.Code,
                accentColor = NovaEmerald
            )
        )
    }

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NovaSurfaceVariant)
            .border(1.dp, if (isThinking) NovaEmerald else NovaBorder, RoundedCornerShape(16.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header Row
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
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isThinking) NovaEmerald.copy(alpha = 0.25f) else NovaIndigo.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (isThinking) NovaEmerald else NovaIndigo,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Column {
                    Text(
                        text = if (isThinking) "Liquid AI · Processing…" else "Liquid AI Assistant",
                        color = NovaTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (isThinking) "Generating solution on ARM64 runtime" else "Tap a quick action to trigger autonomous copilot",
                        color = NovaTextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Text(
                text = if (expanded) "Hide" else "Actions",
                color = NovaEmerald,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NovaEmerald.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Action Pills Carousel
        AnimatedVisibility(
            visible = expanded || isThinking,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                actions.forEach { action ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(NovaSurfaceElevated)
                            .border(1.dp, NovaBorder, RoundedCornerShape(10.dp))
                            .liquidBounceClick(onClick = {
                                onTriggerAction(action.prompt)
                            })
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = null,
                                tint = action.accentColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = action.title,
                                color = NovaTextPrimary,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
