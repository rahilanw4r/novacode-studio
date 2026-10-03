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

// NovaCode Studio signature color palette
val NovaObsidian = Color(0xFF090D16)
val NovaSurface = Color(0xFF0F1626)
val NovaSurfaceVariant = Color(0xFF162035)
val NovaSurfaceElevated = Color(0xFF1C2842)
val NovaBorder = Color(0xFF263554)
val NovaBorderGlow = Color(0xFF3B507D)

val NovaIndigo = Color(0xFF6366F1)
val NovaIndigoGlow = Color(0xFF818CF8)
val NovaCyan = Color(0xFF06B6D4)
val NovaCyanGlow = Color(0xFF22D3EE)
val NovaEmerald = Color(0xFF10B981)
val NovaAmber = Color(0xFFF59E0B)
val NovaRose = Color(0xFFF43F5E)
val NovaPurple = Color(0xFFA855F7)

val NovaTextPrimary = Color(0xFFF1F5F9)
val NovaTextSecondary = Color(0xFF94A3B8)
val NovaTextMuted = Color(0xFF64748B)

// High-tech Gradients
val NovaNeonGradient = Brush.linearGradient(
    listOf(NovaIndigo, NovaCyan)
)
val NovaCardGradient = Brush.linearGradient(
    listOf(Color(0xFF131A2D), Color(0xFF0E1423))
)
val NovaGlassGradient = Brush.linearGradient(
    listOf(Color(0x226366F1), Color(0x1106B6D4))
)

private val NovaDarkColors = darkColorScheme(
    primary = NovaIndigo,
    onPrimary = Color(0xFF0F1626),
    primaryContainer = Color(0xFF2A3358),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = NovaCyan,
    onSecondary = Color(0xFF042F2E),
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFFCCFBF1),
    tertiary = NovaPurple,
    onTertiary = Color(0xFF3B0764),
    background = NovaObsidian,
    onBackground = NovaTextPrimary,
    surface = NovaSurface,
    onSurface = NovaTextPrimary,
    surfaceVariant = NovaSurfaceVariant,
    onSurfaceVariant = NovaTextSecondary,
    outline = NovaBorder,
    outlineVariant = NovaBorderGlow,
    error = NovaRose,
    onError = Color(0xFF4C0519),
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
