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
    val emerald: Color,
    val indigo: Color,
    val cyan: Color,
    val amber: Color,
    val rose: Color,
    val purple: Color,
    val isDark: Boolean,
)

// Human-toned Obsidian Dark Developer-Tool Palette
val PocketDarkPalette = PocketColorPalette(
    background = Color(0xFF0D1014),       // Background: #0D1014
    surface = Color(0xFF171B22),          // Surface: #171B22
    surfaceVariant = Color(0xFF1D222C),   // Elevated surface: #1D222C
    surfaceElevated = Color(0xFF222834),  // High elevated surface
    border = Color(0xFF2A303C),           // Borders: #2A303C
    borderGlow = Color(0xFF353D4C),
    textPrimary = Color(0xFFF3F5F7),      // Primary text: #F3F5F7
    textSecondary = Color(0xFF9299A6),    // Secondary text: #9299A6
    textMuted = Color(0xFF5E6571),        // Disabled text: #5E6571
    emerald = Color(0xFF18C78A),          // Primary accent: #18C78A
    indigo = Color(0xFF3B82F6),           // Developer Blue
    cyan = Color(0xFF0EA5E9),             // Soft Ocean Blue
    amber = Color(0xFFF59E0B),            // Warm Amber
    rose = Color(0xFFEF4444),             // Calm Rose
    purple = Color(0xFF8B5CF6),           // Calm Violet
    isDark = true,
)

// Human-toned Crisp Light Developer-Tool Palette (VS Code / Android Studio / GitHub Light inspired)
val PocketLightPalette = PocketColorPalette(
    background = Color(0xFFF6F8FA),       // Background: Clean slate off-white #F6F8FA
    surface = Color(0xFFFFFFFF),          // Surface: Clean pure white #FFFFFF
    surfaceVariant = Color(0xFFEEF2F6),   // Elevated surface: #EEF2F6
    surfaceElevated = Color(0xFFE2E7ED),  // High elevated surface / input background
    border = Color(0xFFD0D7DE),           // Borders: Clean 1dp boundary #D0D7DE
    borderGlow = Color(0xFFB8C2CC),
    textPrimary = Color(0xFF1F2328),      // Primary text: #1F2328 (high legibility)
    textSecondary = Color(0xFF59636E),    // Secondary text: #59636E
    textMuted = Color(0xFF8C959F),        // Disabled/muted text: #8C959F
    emerald = Color(0xFF0F9960),          // Primary accent: High-contrast emerald #0F9960
    indigo = Color(0xFF0969DA),           // Developer Blue
    cyan = Color(0xFF057A9E),             // Ocean Blue
    amber = Color(0xFF9A6700),            // Accessible Amber
    rose = Color(0xFFCF222E),             // Accessible Rose
    purple = Color(0xFF8250DF),           // Calm Violet
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

val PocketNeonGradient: Brush
    @Composable
    @ReadOnlyComposable
    get() = Brush.linearGradient(listOf(LocalPocketColors.current.indigo, LocalPocketColors.current.indigo))

val PocketCardGradient: Brush
    @Composable
    @ReadOnlyComposable
    get() = Brush.linearGradient(listOf(LocalPocketColors.current.surface, LocalPocketColors.current.surface))

val PocketGlassGradient: Brush
    @Composable
    @ReadOnlyComposable
    get() = Brush.linearGradient(listOf(LocalPocketColors.current.surfaceVariant, LocalPocketColors.current.surfaceVariant))

private val PocketDarkColors = darkColorScheme(
    primary = Color(0xFF18C78A),
    onPrimary = Color(0xFF0F1218),
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = Color(0xFF3B82F6),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF1E3A8A),
    onSecondaryContainer = Color(0xFFDBEAFE),
    tertiary = Color(0xFF8B5CF6),
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFF0D1014),
    onBackground = Color(0xFFF3F5F7),
    surface = Color(0xFF171B22),
    onSurface = Color(0xFFF3F5F7),
    surfaceVariant = Color(0xFF1D222C),
    onSurfaceVariant = Color(0xFF9299A6),
    surfaceContainer = Color(0xFF1D222C),
    surfaceContainerHigh = Color(0xFF222834),
    outline = Color(0xFF2A303C),
    outlineVariant = Color(0xFF353D4C),
    error = Color(0xFFEF4444),
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
