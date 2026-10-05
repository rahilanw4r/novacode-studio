package com.novacode.studio.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacode.studio.BuildConfig
import com.novacode.studio.R
import com.novacode.studio.ui.theme.NovaBorder
import com.novacode.studio.ui.theme.NovaCyan
import com.novacode.studio.ui.theme.NovaEmerald
import com.novacode.studio.ui.theme.NovaIndigo
import com.novacode.studio.ui.theme.NovaObsidian
import com.novacode.studio.ui.theme.NovaPurple
import com.novacode.studio.ui.theme.NovaSurface
import com.novacode.studio.ui.theme.NovaSurfaceElevated
import com.novacode.studio.ui.theme.NovaTextMuted
import com.novacode.studio.ui.theme.NovaTextPrimary
import com.novacode.studio.ui.theme.NovaTextSecondary
import kotlinx.coroutines.delay

@Composable
fun NovaSplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = remember { Animatable(0.72f) }
    val alpha = remember { Animatable(0f) }
    val progress = remember { Animatable(0f) }
    var bootStep by remember { mutableStateOf("Initializing Core Engine...") }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowScale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.28f,
        targetValue = 0.55f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val dotPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotPulse"
    )

    LaunchedEffect(Unit) {
        // Entrance animation
        scale.animateTo(1f, animationSpec = tween(650, easing = FastOutSlowInEasing))
        alpha.animateTo(1f, animationSpec = tween(450))

        // Sequential initialization
        bootStep = "Loading Linux ARM64 userspace kernel..."
        progress.animateTo(0.28f, animationSpec = tween(380))
        delay(220)

        bootStep = "Verifying PRoot isolated sandbox environment..."
        progress.animateTo(0.62f, animationSpec = tween(420))
        delay(260)

        bootStep = "Connecting Multi-Agent Copilot orchestrator..."
        progress.animateTo(0.90f, animationSpec = tween(360))
        delay(220)

        bootStep = "Pocket IDE ready"
        progress.animateTo(1f, animationSpec = tween(220))
        delay(380)

        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NovaObsidian),
        contentAlignment = Alignment.Center
    ) {
        // Multi-Layer Ambient Background Glow
        Box(
            modifier = Modifier
                .size(320.dp)
                .scale(glowScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            NovaIndigo.copy(alpha = glowAlpha * 0.40f),
                            NovaCyan.copy(alpha = glowAlpha * 0.25f),
                            NovaPurple.copy(alpha = glowAlpha * 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Elegant Squircle Logo Card
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(116.dp)
                    .scale(scale.value)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF161F33),
                                Color(0xFF0C1019)
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(NovaIndigo, NovaCyan, NovaEmerald)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    )
            ) {
                // Official High-Resolution App Logo
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Pocket IDE Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(88.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // App Brand Typography
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.alpha(alpha.value)
            ) {
                Text(
                    text = "POCKET",
                    color = NovaTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "IDE",
                    color = NovaCyan,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 4.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your AI-powered development workspace, anywhere.",
                color = NovaTextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.3.sp,
                fontFamily = FontFamily.Default,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(modifier = Modifier.height(44.dp))

            // Sleek Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF141C2E))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.value)
                        .height(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(NovaIndigo, NovaCyan, NovaEmerald)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Boot Status with Animated Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .scale(dotPulse)
                        .clip(CircleShape)
                        .background(NovaCyan)
                )
                Text(
                    text = bootStep,
                    color = NovaTextSecondary,
                    fontSize = 11.5.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Bottom System Tag & Version
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NovaSurface)
                    .border(0.8.dp, NovaBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 9.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "v${BuildConfig.VERSION_NAME}",
                    color = NovaEmerald,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Text(
                text = "ARM64 Autonomous Workspace",
                color = NovaTextMuted,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
