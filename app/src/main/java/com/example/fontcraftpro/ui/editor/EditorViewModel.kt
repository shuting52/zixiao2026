package com.example.fontcraftpro.ui.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fontcraftpro.data.model.Background
import com.example.fontcraftpro.data.model.BackgroundType
import com.example.fontcraftpro.data.model.ImageLayer
import com.example.fontcraftpro.data.model.Layer
import com.example.fontcraftpro.data.model.Project
import com.example.fontcraftpro.data.model.TextAlignment
import com.example.fontcraftpro.data.model.TextLayer
import com.example.fontcraftpro.data.model.TextPreset
import com.example.fontcraftpro.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: ProjectRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val layers = mutableStateListOf<Layer>()
    var selectedId by mutableStateOf<String?>(null)
        private set

    var background by mutableStateOf(
        Background(
            type = BackgroundType.SOLID,
            solidColor = 0xFFFFFFFF.toInt()
        )
    )
        private set

    fun setBackground(type: BackgroundType, color1: Int = 0xFFFFFFFF.toInt(), color2: Int = 0xFF2196F3.toInt()) {
        background = Background(
            type = type,
            solidColor = color1,
            startColor = color1,
            endColor = color2
        )
    }

    fun addText(text: String) {
        val item = TextLayer(
            id = UUID.randomUUID().toString(),
            x = 180f,
            y = 200f,
            text = text,
            fontName = "default",
            fontSize = 72f,
            textColor = 0xFFFFFFFF.toInt(),
            strokeColor = 0xFF000000.toInt(),
            strokeWidth = 2f,
            shadowColor = 0x66000000,
            shadowRadius = 8f,
            shadowDx = 3f,
            shadowDy = 3f
        )
        layers.add(item)
        selectedId = item.id
    }

    fun addSticker(emoji: String) {
        val item = ImageLayer(
            id = UUID.randomUUID().toString(),
            x = 220f,
            y = 220f,
            uri = emoji,
            width = 120f,
            height = 120f
        )
        layers.add(item)
        selectedId = item.id
    }

    fun select(id: String?) {
        selectedId = id
    }

    fun moveLayer(id: String, x: Float, y: Float) {
        val idx = layers.indexOfFirst { it.id == id }
        if (idx == -1) return
        val item = layers[idx]
        layers[idx] = when (item) {
            is TextLayer -> item.copy(x = x, y = y)
            is ImageLayer -> item.copy(x = x, y = y)
            else -> item
        }
    }

    fun scaleRotateLayer(id: String, scaleDelta: Float, rotationDelta: Float) {
        val idx = layers.indexOfFirst { it.id == id }
        if (idx == -1) return
        val item = layers[idx]
        layers[idx] = when (item) {
            is TextLayer -> item.copy(
                scale = (item.scale * scaleDelta).coerceIn(0.2f, 5f),
                rotation = item.rotation + rotationDelta
            )
            is ImageLayer -> item.copy(
                scale = (item.scale * scaleDelta).coerceIn(0.2f, 5f),
                rotation = item.rotation + rotationDelta
            )
            else -> item
        }
    }

    fun updateSelectedText(block: (TextLayer) -> TextLayer) {
        val id = selectedId ?: return
        val index = layers.indexOfFirst { it.id == id }
        val item = layers.getOrNull(index) as? TextLayer ?: return
        layers[index] = block(item)
    }

    fun applyTextPreset(preset: TextPreset) {
        val id = selectedId ?: return
        val index = layers.indexOfFirst { it.id == id }
        val item = layers.getOrNull(index) as? TextLayer ?: return

        val styled = when (preset) {
            TextPreset.POSTER -> item.copy(
                fontSize = 88f,
                textColor = 0xFFFFF3C4.toInt(),
                strokeColor = 0xFFB13535.toInt(),
                strokeWidth = 6f,
                shadowRadius = 18f,
                shadowColor = 0xAA000000,
                effect = com.example.fontcraftpro.data.model.TextEffect.OUTER_GLOW,
                animation = com.example.fontcraftpro.data.model.TextAnimation.PULSE,
                preset = preset
            )
            TextPreset.NEON -> item.copy(
                fontSize = 92f,
                textColor = 0xFFB5F1FF.toInt(),
                strokeColor = 0xFF7C4DFF.toInt(),
                strokeWidth = 4f,
                shadowRadius = 22f,
                shadowColor = 0xFF7C4DFF.toInt(),
                effect = com.example.fontcraftpro.data.model.TextEffect.NEON,
                animation = com.example.fontcraftpro.data.model.TextAnimation.FADE,
                preset = preset
            )
            TextPreset.METAL -> item.copy(
                fontSize = 90f,
                textColor = 0xFFE0EAF1.toInt(),
                strokeColor = 0xFF4B5563.toInt(),
                strokeWidth = 3f,
                shadowRadius = 12f,
                shadowColor = 0x66000000,
                effect = com.example.fontcraftpro.data.model.TextEffect.CHROME,
                animation = com.example.fontcraftpro.data.model.TextAnimation.SWING,
                preset = preset
            )
            TextPreset.RETRO -> item.copy(
                fontSize = 78f,
                textColor = 0xFFFFD8A8.toInt(),
                strokeColor = 0xFF7B4B2A.toInt(),
                strokeWidth = 5f,
                shadowRadius = 8f,
                shadowColor = 0x66000000,
                effect = com.example.fontcraftpro.data.model.TextEffect.BEVEL,
                animation = com.example.fontcraftpro.data.model.TextAnimation.NONE,
                preset = preset
            )
            TextPreset.POP -> item.copy(
                fontSize = 94f,
                textColor = 0xFFFF6EC7.toInt(),
                strokeColor = 0xFF4F46E5.toInt(),
                strokeWidth = 6f,
                shadowRadius = 20f,
                shadowColor = 0xFF4F46E5.toInt(),
                effect = com.example.fontcraftpro.data.model.TextEffect.GRADIENT,
                animation = com.example.fontcraftpro.data.model.TextAnimation.PULSE,
                preset = preset
            )
            TextPreset.CUSTOM -> item.copy(preset = preset)
        }

        layers[index] = styled
    }

    fun deleteSelected() {
        selectedId?.let { id ->
            layers.removeIf { it.id == id }
        }
        selectedId = null
    }

    fun bringToFront() {
        val id = selectedId ?: return
        val idx = layers.indexOfFirst { it.id == id }
        if (idx == -1) return
        val item = layers.removeAt(idx)
        layers.add(item.copy(zIndex = layers.size))
    }

    fun sendToBack() {
        val id = selectedId ?: return
        val idx = layers.indexOfFirst { it.id == id }
        if (idx == -1) return
        val item = layers.removeAt(idx)
        layers.add(0, item.copy(zIndex = 0))
    }

    fun saveProject(name: String) {
        viewModelScope.launch {
            val project = Project(
                id = UUID.randomUUID().toString(),
                name = name,
                width = 1080,
                height = 1920,
                background = background,
                layers = layers.toList(),
                updatedAt = System.currentTimeMillis()
            )
            repository.saveProject(project)
        }
    }

    fun exportCurrentProjectImage(fileName: String = "fontcraft_export.png"): String? {
        val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        when (background.type) {
            BackgroundType.SOLID -> canvas.drawColor(background.solidColor)
            BackgroundType.GRADIENT -> {
                val paint = Paint().apply {
                    shader = LinearGradient(
                        0f, 0f, 0f, 1920f,
                        intArrayOf(background.startColor, background.endColor),
                        null,
                        Shader.TileMode.CLAMP
                    )
                }
                canvas.drawRect(0f, 0f, 1080f, 1920f, paint)
            }
            BackgroundType.IMAGE -> canvas.drawColor(0xFFFFFFFF.toInt())
        }

        layers.sortedBy { it.zIndex }.forEach { layer ->
            when (layer) {
                is TextLayer -> {
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        textSize = layer.fontSize
                        color = layer.textColor
                        typeface = when (layer.fontName) {
                            "bold" -> Typeface.DEFAULT_BOLD
                            "serif" -> Typeface.SERIF
                            "mono" -> Typeface.MONOSPACE
                            else -> Typeface.DEFAULT
                        }
                        alpha = (layer.alpha * 255).toInt().coerceIn(0, 255)
                        textAlign = when (layer.textAlignment) {
                            TextAlignment.LEFT -> Paint.Align.LEFT
                            TextAlignment.CENTER -> Paint.Align.CENTER
                            TextAlignment.RIGHT -> Paint.Align.RIGHT
                        }
                        if (layer.shadowRadius > 0f) {
                            setShadowLayer(layer.shadowRadius, layer.shadowDx, layer.shadowDy, layer.shadowColor)
                        }
                    }

                    val metrics = paint.fontMetrics
                    val baseline = -metrics.ascent
                    val xOffset = when (layer.textAlignment) {
                        TextAlignment.LEFT -> 0f
                        TextAlignment.CENTER -> -paint.measureText(layer.text) / 2f
                        TextAlignment.RIGHT -> -paint.measureText(layer.text)
                    }

                    canvas.save()
                    canvas.translate(layer.x, layer.y)
                    canvas.rotate(layer.rotation, 0f, 0f)
                    canvas.scale(layer.scale, layer.scale)
                    if (layer.strokeWidth > 0f && layer.strokeColor != 0x00000000) {
                        val strokePaint = Paint(paint).apply {
                            style = Paint.Style.STROKE
                            strokeWidth = layer.strokeWidth
                            color = layer.strokeColor
                            clearShadowLayer()
                        }
                        canvas.drawText(layer.text, xOffset, baseline, strokePaint)
                    }
                    if (layer.curveOffset != 0f && layer.text.isNotEmpty()) {
                        val radius = maxOf(120f, layer.fontSize * 2.3f)
                        val path = android.graphics.Path().apply {
                            addArc(
                                -radius, -radius, radius, radius,
                                180f + layer.curveOffset,
                                180f - layer.curveOffset
                            )
                        }
                        canvas.drawTextOnPath(layer.text, path, 0f, 0f, paint)
                    } else {
                        canvas.drawText(layer.text, xOffset, baseline, paint)
                    }
                    canvas.restore()
                }
                is ImageLayer -> {
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        alpha = (layer.alpha * 255).toInt().coerceIn(0, 255)
                    }
                    val rect = android.graphics.RectF(
                        layer.x,
                        layer.y,
                        layer.x + layer.width,
                        layer.y + layer.height
                    )
                    canvas.save()
                    canvas.translate(layer.x, layer.y)
                    canvas.rotate(layer.rotation, rect.centerX(), rect.centerY())
                    canvas.scale(layer.scale, layer.scale)
                    paint.shader = LinearGradient(
                        0f, 0f, rect.right, rect.bottom,
                        intArrayOf(
                            android.graphics.Color.argb(255, 120, 111, 255),
                            android.graphics.Color.argb(255, 255, 130, 120)
                        ),
                        null,
                        Shader.TileMode.CLAMP
                    )
                    canvas.drawRoundRect(rect, 18f, 18f, paint)
                    canvas.restore()
                }
            }
        }

        val outputDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "fontcraft")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val file = File(outputDir, fileName)
        return try {
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            file.absolutePath
        } catch (_: Exception) {
            null
        }
    }
}
