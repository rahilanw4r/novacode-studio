package com.novacode.studio.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// NovaCode Studio Human Tone & Minimal Palette
val NovaObsidian = Color(0xFF0F1218)
val NovaSurface = Color(0xFF161A23)
val NovaSurfaceVariant = Color(0xFF1D222E)
val NovaSurfaceElevated = Color(0xFF242A38)
val NovaBorder = Color(0xFF2D3342)
val NovaBorderGlow = Color(0xFF3B4357)

val NovaEmerald = Color(0xFF10B981) // Clean Android Logo Green
val NovaIndigo = Color(0xFF3B82F6)  // Clean Developer Blue
val NovaCyan = Color(0xFF0EA5E9)    // Soft Ocean Blue
val NovaAmber = Color(0xFFF59E0B)   // Warm Amber
val NovaRose = Color(0xFFEF4444)    // Calm Rose
val NovaPurple = Color(0xFF8B5CF6)  // Calm Violet

val NovaTextPrimary = Color(0xFFF3F4F6)
val NovaTextSecondary = Color(0xFF9CA3AF)
val NovaTextMuted = Color(0xFF6B7280)

// Minimal, clean surface gradients (no neon or harsh color shifts)
val NovaNeonGradient = Brush.linearGradient(
    listOf(NovaIndigo, NovaIndigo)
)
val NovaCardGradient = Brush.linearGradient(
    listOf(NovaSurface, NovaSurface)
)
val NovaGlassGradient = Brush.linearGradient(
    listOf(NovaSurfaceVariant, NovaSurfaceVariant)
)

private val NovaDarkColors = darkColorScheme(
    primary = NovaEmerald,
    onPrimary = Color(0xFF0F1218),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = NovaIndigo,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF1E3A8A),
    onSecondaryContainer = Color(0xFFDBEAFE),
    tertiary = NovaPurple,
    onTertiary = Color(0xFFFFFFFF),
    background = NovaObsidian,
    onBackground = NovaTextPrimary,
    surface = NovaSurface,
    onSurface = NovaTextPrimary,
    surfaceVariant = NovaSurfaceVariant,
    onSurfaceVariant = NovaTextSecondary,
    outline = NovaBorder,
    outlineVariant = NovaBorderGlow,
    error = NovaRose,
    onError = Color(0xFFFFFFFF),
)

private val NovaLightColors = lightColorScheme(
    primary = Color(0xFF4F46E5),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEEF2FF),
    onPrimaryContainer = Color(0xFF312E81),
    secondary = Color(0xFF0891B2),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFECFEFF),
    onSecondaryContainer = Color(0xFF164E63),
    tertiary = Color(0xFF9333EA),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = Color(0xFFE11D48),
    onError = Color(0xFFFFFFFF),
)

@Composable
fun NovaTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }

    MaterialTheme(
        colorScheme = if (isDark) NovaDarkColors else NovaLightColors,
        content = content,
    )
}
