package com.example.fontcraftpro.ui.theme

import android.net.Uri
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
    GLASS,
    LIGHT,
    DARK,
    CUSTOM_IMAGE,
    CUSTOM_VIDEO
}

data class AppThemeSettings(
    val mode: AppThemeMode = AppThemeMode.GLASS,
    val customImageUri: Uri? = null,
    val customVideoUri: Uri? = null,
    val blurRadius: Float = 18f,
    val glassTint: Color = Color(0x66FFFFFF),
    val backdropOpacity: Float = 0.18f,
    val overlayColor: Color = Color(0x1A000000)
) {
    companion object {
        val defaultGlass = AppThemeSettings(
            mode = AppThemeMode.GLASS,
            blurRadius = 18f,
            glassTint = Color(0x66FFFFFF),
            backdropOpacity = 0.18f
        )
    }
}
