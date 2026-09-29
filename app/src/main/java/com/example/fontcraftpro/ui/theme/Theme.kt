package com.example.fontcraftpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Purple80 = Color(0xFFD0BCFF)
private val PurpleGrey80 = Color(0xFFCCC2DC)
private val Pink80 = Color(0xFFEFB8C8)
private val Purple40 = Color(0xFF6650a4)
private val PurpleGrey40 = Color(0xFF625b71)
private val Pink40 = Color(0xFF7D5260)
private val DarkBG = Color(0xFF101418)
private val DarkSurface = Color(0xFF1D2330)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkBG,
    surface = DarkSurface,
    onPrimary = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color(0xFFF7F8FC),
    surface = Color(0xFFFFFFFF),
    onBackground = Color(0xFF111827),
    onSurface = Color(0xFF111827)
)

private val GlassColorScheme = lightColorScheme(
    primary = Color(0xFF7FA8FF),
    secondary = Color(0xFF9BE7D8),
    tertiary = Color(0xFFC6B7FF),
    background = Color(0x66000000),
    surface = Color(0x55FFFFFF),
    onPrimary = Color.White,
    onSurface = Color.White,
    onBackground = Color.White
)

@Composable
fun FontCraftTheme(
    themeSettings: AppThemeSettings = AppThemeSettings.defaultGlass,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val resolvedMode = when (themeSettings.mode) {
        AppThemeMode.GLASS -> GlassColorScheme
        AppThemeMode.LIGHT -> LightColorScheme
        AppThemeMode.DARK -> DarkColorScheme
        AppThemeMode.CUSTOM_IMAGE -> if (darkTheme) DarkColorScheme else GlassColorScheme
        AppThemeMode.CUSTOM_VIDEO -> if (darkTheme) DarkColorScheme else GlassColorScheme
    }

    MaterialTheme(
        colorScheme = resolvedMode,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
