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

// Minimal Developer Dark Palette (Slate / Neutral Charcoal)
val PocketDarkPalette = PocketColorPalette(
    background = Color(0xFF0D1117),       // Deep neutral background
    surface = Color(0xFF161B22),          // Primary neutral surface
    surfaceVariant = Color(0xFF21262D),   // Elevated card surface
    surfaceElevated = Color(0xFF21262D),  // Raised neutral surface
    border = Color(0xFF30363D),           // Subtle crisp neutral border
    borderGlow = Color(0xFF30363D),       // Neutral border (no decorative glow)
    textPrimary = Color(0xFFF0F6FC),      // Crisp high-contrast readable text
    textSecondary = Color(0xFF8B949E),    // Muted secondary metadata
    textMuted = Color(0xFF6E7681),        // Tertiary text
    primaryBlue = Color(0xFF2F81F7),      // Blue ONLY for primary actions / active selection
    bluePressed = Color(0xFF1F6FEB),      // Deep action blue
    blueSurface = Color(0xFF121D2F),      // Subtle neutral blue container
    emerald = Color(0xFF2EA043),          // Green ONLY for success / running states
    indigo = Color(0xFF2F81F7),           // Mapped strictly to primary blue
    cyan = Color(0xFF2F81F7),             // Mapped strictly to primary blue
    amber = Color(0xFF8B949E),            // Muted neutral secondary (no bright orange)
    rose = Color(0xFFF85149),             // Red ONLY for errors / destructive actions
    purple = Color(0xFF8B949E),           // Neutralized to secondary text
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
    primary = Color(0xFF2F81F7),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF121D2F),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFF2EA043),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF161B22),
    onSecondaryContainer = Color(0xFFF0F6FC),
    tertiary = Color(0xFF8B949E),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF0D1117),
    onBackground = Color(0xFFF0F6FC),
    surface = Color(0xFF161B22),
    onSurface = Color(0xFFF0F6FC),
    surfaceVariant = Color(0xFF21262D),
    onSurfaceVariant = Color(0xFF8B949E),
    surfaceContainer = Color(0xFF21262D),
    surfaceContainerHigh = Color(0xFF30363D),
    outline = Color(0xFF30363D),
    outlineVariant = Color(0xFF30363D),
    error = Color(0xFFF85149),
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
