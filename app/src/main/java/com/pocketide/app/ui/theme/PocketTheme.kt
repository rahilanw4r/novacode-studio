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

// Restrained Professional Dark Developer-Tool Palette
val PocketDarkPalette = PocketColorPalette(
    background = Color(0xFF111315),       // Background: #111315
    surface = Color(0xFF191C20),          // Surface: #191C20
    surfaceVariant = Color(0xFF20242A),   // Elevated: #20242A
    surfaceElevated = Color(0xFF20242A),  // Elevated: #20242A
    border = Color(0xFF2B3037),           // Border: #2B3037
    borderGlow = Color(0xFF353D4C),
    textPrimary = Color(0xFFF1F3F5),      // Primary text: #F1F3F5
    textSecondary = Color(0xFFA4A9B1),    // Secondary text: #A4A9B1
    textMuted = Color(0xFF747A83),        // Tertiary text: #747A83
    primaryBlue = Color(0xFF4F8CFF),      // Primary blue: #4F8CFF
    bluePressed = Color(0xFF2E5FAF),      // Blue pressed: #2E5FAF
    blueSurface = Color(0xFF1D2A40),      // Blue surface: #1D2A40
    emerald = Color(0xFF3FA66B),          // Success: #3FA66B
    indigo = Color(0xFF4F8CFF),           // Interaction blue: #4F8CFF
    cyan = Color(0xFF4F8CFF),             // Primary action blue: #4F8CFF
    amber = Color(0xFFD69A3A),            // Warning: #D69A3A
    rose = Color(0xFFD85C5C),             // Error: #D85C5C
    purple = Color(0xFF8B5CF6),           // Secondary accent
    isDark = true,
)

// Clean Light Developer-Tool Palette
val PocketLightPalette = PocketColorPalette(
    background = Color(0xFFF6F8FA),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEEF2F6),
    surfaceElevated = Color(0xFFE2E7ED),
    border = Color(0xFFD0D7DE),
    borderGlow = Color(0xFFB8C2CC),
    textPrimary = Color(0xFF1F2328),
    textSecondary = Color(0xFF59636E),
    textMuted = Color(0xFF8C959F),
    primaryBlue = Color(0xFF0969DA),
    bluePressed = Color(0xFF064EAA),
    blueSurface = Color(0xFFDDF4FF),
    emerald = Color(0xFF0F9960),
    indigo = Color(0xFF0969DA),
    cyan = Color(0xFF0969DA),
    amber = Color(0xFF9A6700),
    rose = Color(0xFFCF222E),
    purple = Color(0xFF8250DF),
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
    primary = Color(0xFF4F8CFF),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF1D2A40),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFF3FA66B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF1D2A40),
    onSecondaryContainer = Color(0xFFD1E4FF),
    tertiary = Color(0xFF8B5CF6),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF111315),
    onBackground = Color(0xFFF1F3F5),
    surface = Color(0xFF191C20),
    onSurface = Color(0xFFF1F3F5),
    surfaceVariant = Color(0xFF20242A),
    onSurfaceVariant = Color(0xFFA4A9B1),
    surfaceContainer = Color(0xFF20242A),
    surfaceContainerHigh = Color(0xFF2B3037),
    outline = Color(0xFF2B3037),
    outlineVariant = Color(0xFF353D4C),
    error = Color(0xFFD85C5C),
    onError = Color(0xFFFFFFFF),
)

private val PocketLightColors = lightColorScheme(
    primary = Color(0xFF0F9960),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE6F9F0),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF0969DA),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDDF4FF),
    onSecondaryContainer = Color(0xFF054DA7),
    tertiary = Color(0xFF8250DF),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFF6F8FA),
    onBackground = Color(0xFF1F2328),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1F2328),
    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = Color(0xFF59636E),
    surfaceContainer = Color(0xFFEEF2F6),
    surfaceContainerHigh = Color(0xFFE2E7ED),
    outline = Color(0xFFD0D7DE),
    outlineVariant = Color(0xFFB8C2CC),
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
