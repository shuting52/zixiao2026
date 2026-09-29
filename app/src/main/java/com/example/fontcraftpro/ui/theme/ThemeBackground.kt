package com.example.fontcraftpro.ui.theme

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage

@Composable
fun ThemeBackground(
    themeSettings: AppThemeSettings,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (themeSettings.mode) {
            AppThemeMode.GLASS -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF6FA6FF).copy(alpha = 0.72f),
                                    Color(0xFF8EDFD3).copy(alpha = 0.46f),
                                    Color(0xFFC8C1FF).copy(alpha = 0.28f),
                                    Color(0xFFB7C0FF).copy(alpha = 0.12f)
                                )
                            )
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(themeSettings.glassTint.copy(alpha = 0.18f))
                        .blur(themeSettings.blurRadius.dp)
                )
            }

            AppThemeMode.LIGHT -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F7FB))
                )
            }

            AppThemeMode.DARK -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF101218))
                )
            }

            AppThemeMode.CUSTOM_IMAGE -> {
                val uri = themeSettings.customImageUri
                if (uri != null) {
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFCDC6FF))
                    )
                }
            }

            AppThemeMode.CUSTOM_VIDEO -> {
                val uri = themeSettings.customVideoUri
                if (uri != null) {
                    AndroidView(
                        factory = { context ->
                            VideoView(context).apply {
                                setVideoURI(uri)
                                setOnPreparedListener { mediaPlayer ->
                                    mediaPlayer.isLooping = true
                                    mediaPlayer.start()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF7DB2FF))
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(themeSettings.overlayColor.copy(alpha = themeSettings.backdropOpacity))
        )

        if (themeSettings.mode == AppThemeMode.GLASS) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.White.copy(alpha = 0.12f),
                                Color.Transparent,
                                Color.White.copy(alpha = 0.08f)
                            )
                        )
                    )
            )
        }
    }
}
