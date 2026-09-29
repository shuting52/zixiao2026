package com.example.fontcraftpro.ui.editor

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import java.io.File
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fontcraftpro.data.model.BackgroundType
import com.example.fontcraftpro.data.model.TextLayer
import com.example.fontcraftpro.data.model.TextPreset
import com.example.fontcraftpro.data.model.TextShape
import com.example.fontcraftpro.ui.settings.SettingsScreen
import com.example.fontcraftpro.ui.theme.AppThemeMode
import com.example.fontcraftpro.ui.theme.AppThemeSettings
import com.example.fontcraftpro.ui.theme.ThemeBackground

private fun readImportedText(context: Context, uri: Uri): String {
    return try {
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        val text = if (bytes != null) {
            val decoded = bytes.decodeToString()
            decoded.filter { it.isLetterOrDigit() || it.isWhitespace() || it in "-_:.,/!@#$%^&*()+=[]{}<>|\\~`'\"" }
        } else {
            ""
        }

        val normalized = text
            .replace("\u0000", "")
            .replace(Regex("[\\r\\n\\t]+"), " ")
            .replace(Regex("\\s{2,}"), " ")
            .trim()

        if (normalized.isNotBlank()) normalized.take(240) else {
            val fallback = uri.lastPathSegment?.substringAfterLast('/')?.substringBeforeLast('.')
                ?: "导入文字"
            fallback.ifBlank { "导入文字" }
        }
    } catch (_: Exception) {
        val fallback = uri.lastPathSegment?.substringAfterLast('/')?.substringBeforeLast('.')
            ?: "导入文字"
        fallback.ifBlank { "导入文字" }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    themeSettings: AppThemeSettings = AppThemeSettings.defaultGlass,
    onThemeSettingsChange: (AppThemeSettings) -> Unit = {}
) {
    var inputText by remember { mutableStateOf("2026 字体软件") }
    var replaceFromText by remember { mutableStateOf("") }
    var replaceToText by remember { mutableStateOf("") }
    var showStickerSheet by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showFontLibrary by remember { mutableStateOf(false) }
    var showExportPreview by remember { mutableStateOf(false) }
    var exportMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

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

    val sourceImportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri == null) return@rememberLauncherForActivityResult

            val importedText = readImportedText(context, uri)
            val sourceName = uri.lastPathSegment?.substringAfterLast('/')?.substringBeforeLast('.') ?: "文本层"
            val layer = viewModel.layers.filterIsInstance<TextLayer>().firstOrNull { it.text == importedText }
            if (layer == null) {
                viewModel.addText(importedText)
                viewModel.updateSelectedText { it.copy(text = importedText, fontName = sourceName.ifBlank { "default" }) }
                viewModel.applyTextPreset(TextPreset.POSTER)
            }
            exportMessage = "已从 ${sourceName} 提取文本层：${importedText.take(18)}${if (importedText.length > 18) "..." else ""}"
        }
    )

    val fontImportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            if (uri == null) return@rememberLauncherForActivityResult
            val filePath = viewModel.importFontFromUri(uri) ?: return@rememberLauncherForActivityResult
            viewModel.updateSelectedText { it.copy(fontName = "custom", fontPath = filePath, is3D = true, extrudeDepth = 6) }
            exportMessage = "已导入本地字体：${uri.lastPathSegment ?: "字体"}"
            showFontLibrary = true
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
                            OutlinedButton(onClick = { showFontLibrary = true }) { Text("字体库") }
                            OutlinedButton(onClick = { fontImportLauncher.launch(arrayOf("font/ttf", "font/otf", "application/x-font-ttf", "application/octet-stream", "*/*")) }) { Text("导入字体") }
                            OutlinedButton(onClick = {
                                sourceImportLauncher.launch(arrayOf("*/*"))
                            }) { Text("导入PLP/PSD") }
                            OutlinedButton(onClick = { showSettings = true }) { Text("设置") }
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

                        Button(onClick = { showExportPreview = true }) {
                            Text("预览")
                        }

                        Button(onClick = {
                            val path = viewModel.exportCurrentProjectImage()
                            exportMessage = if (path != null) "已导出到: $path" else "导出失败"
                        }) {
                            Text("导出")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { viewModel.duplicateSelectedTextLayer() }) {
                            Text("复制样式")
                        }
                        Button(onClick = { viewModel.applySelectedStyleToAll() }) {
                            Text("全局套用")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = replaceFromText,
                            onValueChange = { replaceFromText = it },
                            label = { Text("替换词") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = replaceToText,
                            onValueChange = { replaceToText = it },
                            label = { Text("新词") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.replaceTextAcrossLayers(replaceFromText, replaceToText)
                            exportMessage = "已批量替换文字：$replaceFromText -> $replaceToText"
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("批量替换")
                    }

                    if (exportMessage.isNotEmpty()) {
                        Text(
                            text = exportMessage,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
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
                            viewModel.updateSelectedText { it.copy(fontName = fontName, fontPath = null) }
                        },
                        onEffectChange = { effect ->
                            viewModel.updateSelectedText { it.copy(effect = effect) }
                        },
                        onShapeChange = { shape ->
                            viewModel.updateSelectedText { it.copy(shape = shape) }
                        },
                        onCurveChange = { curve ->
                            viewModel.updateSelectedText { it.copy(curveOffset = curve) }
                        },
                        onAnimationChange = { animation ->
                            viewModel.updateSelectedText { it.copy(animation = animation) }
                        },
                        on3DModeChange = { enabled ->
                            viewModel.updateSelectedText { it.copy(is3D = enabled, extrudeDepth = if (enabled) maxOf(it.extrudeDepth, 4) else 0) }
                        },
                        onExtrudeDepthChange = { depth ->
                            viewModel.updateSelectedText { it.copy(extrudeDepth = depth, is3D = depth > 0) }
                        },
                        onPresetApply = { preset ->
                            viewModel.applyTextPreset(preset)
                        },
                        onImportFontClick = {
                            fontImportLauncher.launch(arrayOf("font/ttf", "font/otf", "application/x-font-ttf", "application/octet-stream", "*/*"))
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

    if (showExportPreview) {
        ExportPreviewSheet(
            projectSummary = viewModel.exportProjectSummaryJson(),
            layerCount = viewModel.layers.size,
            onExportImage = {
                val path = viewModel.exportCurrentProjectImage()
                exportMessage = if (path != null) "已导出 PNG: $path" else "导出失败"
                showExportPreview = false
            },
            onExportJson = {
                val json = viewModel.exportProjectSummaryJson()
                exportMessage = "已生成项目结构 JSON：${json.take(28)}..."
                showExportPreview = false
            },
            onDismiss = { showExportPreview = false }
        )
    }

    if (showFontLibrary) {
        FontLibrarySheet(
            fonts = viewModel.importedFonts,
            onApply = { font ->
                viewModel.applyImportedFont(font)
                showFontLibrary = false
            },
            onDismiss = { showFontLibrary = false }
        )
    }

    if (showSettings) {
        SettingsScreen(
            onShareClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "我在用 FontCraft Pro 字效编辑器，推荐你也试试！")
                }
                context.startActivity(Intent.createChooser(shareIntent, "分享软件"))
                exportMessage = "已准备分享：将软件链接分享给好友"
                showSettings = false
            },
            onAboutClick = {
                exportMessage = "关于我们：FontCraft Pro 字效工作室"
                showSettings = false
            },
            onPrivacyClick = {
                exportMessage = "隐私政策：仅在本地处理编辑内容，未上传用户数据"
                showSettings = false
            },
            onImportClick = {
                sourceImportLauncher.launch(arrayOf("*/*"))
                showSettings = false
            },
            onGroupClick = {
                val url = "https://qm.qq.com/q/1I8Vg4TnJy"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
                exportMessage = "已打开官方群邀请链接：白嫖圣手:懒得找官群"
                showSettings = false
            },
            onDismiss = { showSettings = false }
        )
    }
}
