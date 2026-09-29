package com.example.fontcraftpro.ui.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fontcraftpro.data.model.BackgroundType
import com.example.fontcraftpro.data.model.TextLayer
import com.example.fontcraftpro.ui.theme.AppThemeMode
import com.example.fontcraftpro.ui.theme.AppThemeSettings
import com.example.fontcraftpro.ui.theme.ThemeBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    themeSettings: AppThemeSettings = AppThemeSettings.defaultGlass,
    onThemeSettingsChange: (AppThemeSettings) -> Unit = {}
) {
    var inputText by remember { mutableStateOf("2026 字体软件") }
    var showStickerSheet by remember { mutableStateOf(false) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                onThemeSettingsChange(
                    themeSettings.copy(
                        mode = AppThemeMode.CUSTOM_IMAGE,
                        customImageUri = uri
                    )
                )
            }
        }
    )

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                onThemeSettingsChange(
                    themeSettings.copy(
                        mode = AppThemeMode.CUSTOM_VIDEO,
                        customVideoUri = uri
                    )
                )
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ThemeBackground(themeSettings = themeSettings)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("FontCraft Pro") },
                    colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent.copy(alpha = 0.15f)
                    ),
                    actions = {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = {
                                onThemeSettingsChange(AppThemeSettings.defaultGlass)
                            }) { Text("磨砂") }
                            OutlinedButton(onClick = {
                                onThemeSettingsChange(themeSettings.copy(mode = AppThemeMode.LIGHT))
                            }) { Text("浅色") }
                            OutlinedButton(onClick = {
                                onThemeSettingsChange(themeSettings.copy(mode = AppThemeMode.DARK))
                            }) { Text("深色") }
                            OutlinedButton(onClick = { imageLauncher.launch("image/*") }) { Text("图片") }
                            OutlinedButton(onClick = { videoLauncher.launch("video/*") }) { Text("视频") }
                        }
                    }
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.Transparent)
            ) {
                EditorCanvas(
                    background = viewModel.background,
                    layers = viewModel.layers,
                    selectedId = viewModel.selectedId,
                    onSelect = { id -> viewModel.select(id) },
                    onMove = { id, x, y -> viewModel.moveLayer(id, x, y) },
                    onScaleRotate = { id, scaleDelta, rotationDelta ->
                        viewModel.scaleRotateLayer(id, scaleDelta, rotationDelta)
                    },
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        label = { Text("输入文字") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.addText(inputText)
                                inputText = ""
                            }
                        }) {
                            Text("添加文字")
                        }

                        Button(onClick = { viewModel.deleteSelected() }) {
                            Text("删除")
                        }

                        Button(onClick = { viewModel.saveProject("工程 1") }) {
                            Text("保存")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { viewModel.setBackground(BackgroundType.SOLID) }) {
                            Text("纯色")
                        }
                        Button(onClick = {
                            viewModel.setBackground(
                                BackgroundType.GRADIENT,
                                0xFF2196F3.toInt(),
                                0xFF9C27B0.toInt()
                            )
                        }) {
                            Text("渐变")
                        }
                        Button(onClick = { showStickerSheet = true }) {
                            Text("贴纸")
                        }
                    }

                    val selected = viewModel.layers.firstOrNull { it.id == viewModel.selectedId } as? TextLayer
                    TextToolsPanel(
                        selected = selected,
                        onTextSizeChange = { value ->
                            viewModel.updateSelectedText { it.copy(fontSize = value) }
                        },
                        onStrokeWidthChange = { value ->
                            viewModel.updateSelectedText { it.copy(strokeWidth = value) }
                        },
                        onShadowRadiusChange = { value ->
                            viewModel.updateSelectedText { it.copy(shadowRadius = value) }
                        },
                        onColorChange = { color ->
                            viewModel.updateSelectedText { it.copy(textColor = color) }
                        },
                        onAlphaChange = { value ->
                            viewModel.updateSelectedText { it.copy(alpha = value) }
                        },
                        onAlignmentChange = { alignment ->
                            viewModel.updateSelectedText { it.copy(textAlignment = alignment) }
                        },
                        onFontChange = { fontName ->
                            viewModel.updateSelectedText { it.copy(fontName = fontName) }
                        }
                    )

                    LayerPanel(
                        layers = viewModel.layers,
                        selectedId = viewModel.selectedId,
                        onSelect = { viewModel.select(it) },
                        onBringToFront = { viewModel.bringToFront() },
                        onSendToBack = { viewModel.sendToBack() },
                        onDelete = { viewModel.deleteSelected() }
                    )
                }
            }
        }
    }

    if (showStickerSheet) {
        StickerPickerSheet(
            onPick = { emoji ->
                viewModel.addSticker(emoji)
                showStickerSheet = false
            },
            onDismiss = { showStickerSheet = false }
        )
    }
}
