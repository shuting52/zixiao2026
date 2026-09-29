package com.example.fontcraftpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.fontcraftpro.ui.editor.EditorScreen
import com.example.fontcraftpro.ui.theme.AppThemeSettings
import com.example.fontcraftpro.ui.theme.FontCraftTheme
import com.example.fontcraftpro.ui.theme.ThemeBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var themeSettings by rememberSaveable { mutableStateOf(AppThemeSettings.defaultGlass) }

            FontCraftTheme(themeSettings = themeSettings) {
                Box(modifier = Modifier.fillMaxSize()) {
                    ThemeBackground(themeSettings = themeSettings)
                    EditorScreen(
                        themeSettings = themeSettings,
                        onThemeSettingsChange = { themeSettings = it }
                    )
                }
            }
        }
    }
}
