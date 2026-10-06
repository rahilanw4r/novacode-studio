package com.pocketide.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class PocketColorPalette(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceElevated: Color,
    val border: Color,
    val borderGlow: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val primaryBlue: Color,
    val bluePressed: Color,
    val blueSurface: Color,
    val emerald: Color,
    val indigo: Color,
    val cyan: Color,
    val amber: Color,
    val rose: Color,
    val purple: Color,
    val isDark: Boolean,
)

// Professional Developer Dark Palette (Clean, Neutral, Fast)
val PocketDarkPalette = PocketColorPalette(
    background = Color(0xFF1E1E1E),       // Clean neutral dark background
    surface = Color(0xFF181818),          // Deep surface / bars
    surfaceVariant = Color(0xFF282828),   // Card / bubble surface
    surfaceElevated = Color(0xFF2D2D2D),  // Raised surface
    border = Color(0xFF383838),           // Subtle crisp neutral border
    borderGlow = Color(0xFF383838),       // Neutral border (no decorative glow)
    textPrimary = Color(0xFFECECEC),      // Crisp high-contrast readable text
    textSecondary = Color(0xFFB4B4B4),    // Secondary metadata
    textMuted = Color(0xFF808080),        // Muted tertiary text
    primaryBlue = Color(0xFF3B82F6),      // Blue for selection & primary interaction
    bluePressed = Color(0xFF1D4ED8),      // Deep action blue
    blueSurface = Color(0xFF252D3D),      // Subtle blue container
    emerald = Color(0xFF10B981),          // Green ONLY for success / running states
    indigo = Color(0xFF3B82F6),           // Neutralized to primary blue
    cyan = Color(0xFF38BDF8),             // Subtle info cyan
    amber = Color(0xFFF59E0B),            // Warning amber
    rose = Color(0xFFEF4444),             // Red ONLY for errors / destructive actions
    purple = Color(0xFF8B5CF6),           // Accent purple
    isDark = true,
)

// Minimal Developer Light Palette
val PocketLightPalette = PocketColorPalette(
    background = Color(0xFFF6F8FA),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF0F2F5),
    surfaceElevated = Color(0xFFE6EBF1),
    border = Color(0xFFD0D7DE),
    borderGlow = Color(0xFFD0D7DE),
    textPrimary = Color(0xFF1F2328),
    textSecondary = Color(0xFF59636E),
    textMuted = Color(0xFF8C959F),
    primaryBlue = Color(0xFF0969DA),
    bluePressed = Color(0xFF064EAA),
    blueSurface = Color(0xFFEBF5FF),
    emerald = Color(0xFF1A7F37),
    indigo = Color(0xFF0969DA),
    cyan = Color(0xFF0969DA),
    amber = Color(0xFF59636E),
    rose = Color(0xFFCF222E),
    purple = Color(0xFF59636E),
    isDark = false,
)

val LocalPocketColors = staticCompositionLocalOf { PocketDarkPalette }

val PocketObsidian: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.background

val PocketSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.surface

val PocketSurfaceVariant: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.surfaceVariant

val PocketSurfaceElevated: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.surfaceElevated

val PocketBorder: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.border

val PocketBorderGlow: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.borderGlow

val PocketTextPrimary: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.textPrimary

val PocketTextSecondary: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.textSecondary

val PocketTextMuted: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.textMuted

val PocketEmerald: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.emerald

val PocketIndigo: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.indigo

val PocketCyan: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.cyan

val PocketAmber: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.amber

val PocketRose: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.rose

val PocketPurple: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.purple

val PocketPrimaryBlue: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.primaryBlue

val PocketBluePressed: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.bluePressed

val PocketBlueSurface: Color
    @Composable
    @ReadOnlyComposable
    get() = LocalPocketColors.current.blueSurface

val PocketNeonGradient: Brush
    @Composable
    @ReadOnlyComposable
    get() = Brush.linearGradient(listOf(LocalPocketColors.current.primaryBlue, LocalPocketColors.current.primaryBlue))

val PocketCardGradient: Brush
    @Composable
    @ReadOnlyComposable
    get() = Brush.linearGradient(listOf(LocalPocketColors.current.surface, LocalPocketColors.current.surface))

val PocketGlassGradient: Brush
    @Composable
    @ReadOnlyComposable
    get() = Brush.linearGradient(listOf(LocalPocketColors.current.surfaceVariant, LocalPocketColors.current.surfaceVariant))

private val PocketDarkColors = darkColorScheme(
    primary = Color(0xFF10A37F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF2F2F2F),
    onPrimaryContainer = Color(0xFFECECEC),
    secondary = Color(0xFF10A37F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF171717),
    onSecondaryContainer = Color(0xFFECECEC),
    tertiary = Color(0xFFB4B4B4),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF212121),
    onBackground = Color(0xFFECECEC),
    surface = Color(0xFF171717),
    onSurface = Color(0xFFECECEC),
    surfaceVariant = Color(0xFF2F2F2F),
    onSurfaceVariant = Color(0xFFB4B4B4),
    surfaceContainer = Color(0xFF2F2F2F),
    surfaceContainerHigh = Color(0xFF383838),
    outline = Color(0xFF383838),
    outlineVariant = Color(0xFF383838),
    error = Color(0xFFEF4444),
    onError = Color(0xFFFFFFFF),
)

private val PocketLightColors = lightColorScheme(
    primary = Color(0xFF0969DA),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEBF5FF),
    onPrimaryContainer = Color(0xFF054DA7),
    secondary = Color(0xFF1A7F37),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEBF5FF),
    onSecondaryContainer = Color(0xFF054DA7),
    tertiary = Color(0xFF59636E),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF6F8FA),
    onBackground = Color(0xFF1F2328),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F2328),
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = Color(0xFF59636E),
    surfaceContainer = Color(0xFFF0F2F5),
    surfaceContainerHigh = Color(0xFFE6EBF1),
    outline = Color(0xFFD0D7DE),
    outlineVariant = Color(0xFFD0D7DE),
    error = Color(0xFFCF222E),
    onError = Color(0xFFFFFFFF),
)

@Composable
fun PocketTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val palette = if (isDark) PocketDarkPalette else PocketLightPalette
    val colorScheme = if (isDark) PocketDarkColors else PocketLightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !isDark
            insetsController.isAppearanceLightNavigationBars = !isDark
        }
    }

    CompositionLocalProvider(
        LocalPocketColors provides palette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}
