package com.example.fontcraftpro.ui.editor

import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import java.io.File
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.fontcraftpro.data.model.Background
import com.example.fontcraftpro.data.model.BackgroundType
import com.example.fontcraftpro.data.model.ImageLayer
import com.example.fontcraftpro.data.model.Layer
import com.example.fontcraftpro.data.model.TextAlignment
import com.example.fontcraftpro.data.model.TextAnimation
import com.example.fontcraftpro.data.model.TextEffect
import com.example.fontcraftpro.data.model.TextLayer

@Composable
fun EditorCanvas(
    background: Background,
    layers: List<Layer>,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    onMove: (String, Float, Float) -> Unit,
    onScaleRotate: (String, Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawBackground(this.drawContext.canvas.nativeCanvas, background)

            layers.sortedBy { it.zIndex }.forEach { layer ->
                drawLayerToCanvas(this.drawContext.canvas.nativeCanvas, layer)
            }

            val selected = layers.firstOrNull { it.id == selectedId }
            selected?.let { drawSelectionBox(this.drawContext.canvas.nativeCanvas, it) }
        }

        layers.sortedByDescending { it.zIndex }.forEach { layer ->
            val isSelected = layer.id == selectedId
            Box(
                modifier = Modifier
                    .offset { IntOffset(layer.x.toInt(), layer.y.toInt()) }
                    .graphicsLayer(
                        scaleX = layer.scale,
                        scaleY = layer.scale,
                        rotationZ = layer.rotation,
                        alpha = layer.alpha
                    )
                    .pointerInput(layer.id) {
                        detectTransformGestures { _, pan, zoom, rotationDelta ->
                            onSelect(layer.id)
                            onMove(layer.id, layer.x + pan.x, layer.y + pan.y)
                            onScaleRotate(layer.id, zoom, rotationDelta)
                        }
                    }
            ) {
                when (layer) {
                    is TextLayer -> {
                        Text(
                            text = layer.text,
                            color = Color(layer.textColor),
                            fontSize = layer.fontSize.sp,
                            modifier = Modifier.graphicsLayer(
                                shadowElevation = if (layer.shadowRadius > 0f) 10f else 0f
                            )
                        )
                    }
                    is ImageLayer -> {
                        Text(text = "🖼️", fontSize = 28.sp)
                    }
                    else -> Unit
                }
            }
        }
    }
}

private fun drawBackground(canvas: android.graphics.Canvas, bg: Background) {
    when (bg.type) {
        BackgroundType.SOLID -> {
            canvas.drawColor(bg.solidColor)
        }
        BackgroundType.GRADIENT -> {
            val paint = Paint().apply {
                shader = LinearGradient(
                    0f, 0f, 0f, 1920f,
                    intArrayOf(bg.startColor, bg.endColor),
                    null,
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawRect(0f, 0f, 1080f, 1920f, paint)
        }
        BackgroundType.IMAGE -> {
            canvas.drawColor(android.graphics.Color.WHITE)
        }
    }
}

private fun drawLayerToCanvas(canvas: android.graphics.Canvas, layer: Layer) {
    when (layer) {
        is TextLayer -> drawTextLayer(canvas, layer)
        is ImageLayer -> drawImageLayer(canvas, layer)
    }
}

private fun drawTextLayer(canvas: android.graphics.Canvas, layer: TextLayer) {
    val customTypeface = when {
        layer.fontPath != null && File(layer.fontPath).exists() -> Typeface.createFromFile(layer.fontPath)
        layer.fontName == "bold" -> Typeface.DEFAULT_BOLD
        layer.fontName == "serif" -> Typeface.SERIF
        layer.fontName == "mono" -> Typeface.MONOSPACE
        else -> Typeface.DEFAULT
    }

    val basePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = layer.fontSize
        typeface = customTypeface
        alpha = (layer.alpha * 255).toInt().coerceIn(0, 255)
        textAlign = when (layer.textAlignment) {
            TextAlignment.LEFT -> Paint.Align.LEFT
            TextAlignment.CENTER -> Paint.Align.CENTER
            TextAlignment.RIGHT -> Paint.Align.RIGHT
        }
    }

    val animationScale = when (layer.animation) {
        TextAnimation.NONE -> 1f
        TextAnimation.PULSE -> 1.05f
        TextAnimation.SWING -> 1f
        TextAnimation.FADE -> 0.96f
    }

    val paint = when (layer.effect) {
        TextEffect.NORMAL -> Paint(basePaint).apply {
            color = layer.textColor
        }
        TextEffect.OUTER_GLOW -> Paint(basePaint).apply {
            color = layer.textColor
            setShadowLayer(18f, 0f, 0f, 0xFF66F2FF.toInt())
        }
        TextEffect.INNER_GLOW -> Paint(basePaint).apply {
            color = layer.textColor
            setShadowLayer(10f, 0f, 0f, 0x44000000)
        }
        TextEffect.BEVEL -> Paint(basePaint).apply {
            shader = LinearGradient(
                0f, 0f, layer.fontSize * layer.text.length, layer.fontSize,
                intArrayOf(
                    0xFFFFF9C4.toInt(),
                    0xFFFFD54F.toInt(),
                    0xFFB26A00.toInt()
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        TextEffect.STROKE -> Paint(basePaint).apply {
            style = Paint.Style.STROKE
            strokeWidth = maxOf(layer.strokeWidth, 4f)
            color = layer.strokeColor.takeIf { it != 0x00000000 } ?: 0xFF7C4DFF.toInt()
        }
        TextEffect.CHROME -> Paint(basePaint).apply {
            shader = LinearGradient(
                0f, 0f, layer.fontSize * layer.text.length, layer.fontSize,
                intArrayOf(
                    0xFFE0F2F1.toInt(),
                    0xFFB0BEC5.toInt(),
                    0xFF90A4AE.toInt(),
                    0xFFE0F7FA.toInt()
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        TextEffect.GOLD -> Paint(basePaint).apply {
            shader = LinearGradient(
                0f, 0f, layer.fontSize * layer.text.length, 0f,
                intArrayOf(
                    0xFFFFE082.toInt(),
                    0xFFFFD54F.toInt(),
                    0xFFFFA726.toInt()
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        TextEffect.NEON -> Paint(basePaint).apply {
            color = layer.textColor
            setShadowLayer(12f, 0f, 0f, 0xFF7C4DFF.toInt())
        }
        TextEffect.GRADIENT -> Paint(basePaint).apply {
            shader = LinearGradient(
                0f, 0f, layer.fontSize * layer.text.length, layer.fontSize,
                intArrayOf(
                    0xFF60A5FA.toInt(),
                    0xFF22D3EE.toInt(),
                    0xFFF472B6.toInt()
                ),
                null,
                Shader.TileMode.CLAMP
            )
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

    if (layer.is3D && layer.extrudeDepth > 0) {
        for (i in 1..layer.extrudeDepth) {
            val extrudePaint = Paint(paint).apply {
                color = 0xFF1D1F2A.toInt()
                alpha = 120
                style = Paint.Style.FILL
            }
            canvas.drawText(layer.text, xOffset + i.toFloat(), baseline + i.toFloat(), extrudePaint)
        }
    }

    if (layer.effect == TextEffect.BEVEL) {
        val shadow = Paint(paint).apply {
            color = 0x66000000.toInt()
            alpha = 120
            setShadowLayer(0f, 3f, 3f, 0x66000000)
        }
        canvas.drawText(layer.text, xOffset + 2f, baseline + 2f, shadow)
    }

    if (layer.effect == TextEffect.INNER_GLOW) {
        val inner = Paint(paint).apply {
            color = 0x33000000
            style = Paint.Style.FILL_AND_STROKE
            setShadowLayer(8f, 0f, 0f, 0x66000000)
        }
        canvas.drawText(layer.text, xOffset, baseline, inner)
    }

    if (layer.shape == com.example.fontcraftpro.data.model.TextShape.OUTLINE) {
        val outline = Paint(paint).apply {
            style = Paint.Style.STROKE
            strokeWidth = maxOf(layer.strokeWidth, 3f)
            color = layer.strokeColor.takeIf { it != 0x00000000 } ?: 0xFFB9A8FF.toInt()
            clearShadowLayer()
        }
        canvas.drawText(layer.text, xOffset, baseline, outline)
    }

    if (layer.strokeWidth > 0f && layer.strokeColor != 0x00000000 && layer.effect != TextEffect.STROKE) {
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
        val pathPaint = Paint(paint)
        canvas.drawTextOnPath(layer.text, path, 0f, 0f, pathPaint)
    } else {
        canvas.drawText(layer.text, xOffset, baseline, paint)
    }

    canvas.restore()
}

private fun drawImageLayer(canvas: android.graphics.Canvas, layer: ImageLayer) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        alpha = (layer.alpha * 255).toInt()
    }

    val rect = RectF(
        layer.x,
        layer.y,
        layer.x + layer.width,
        layer.y + layer.height
    )

    canvas.save()
    canvas.translate(layer.x, layer.y)
    canvas.rotate(layer.rotation, rect.centerX(), rect.centerY())
    canvas.scale(layer.scale, layer.scale)

    val shader = LinearGradient(
        0f, 0f, rect.right, rect.bottom,
        intArrayOf(
            android.graphics.Color.argb(255, 120, 111, 255),
            android.graphics.Color.argb(255, 255, 130, 120)
        ),
        null,
        Shader.TileMode.CLAMP
    )
    paint.shader = shader

    canvas.drawRoundRect(rect, 18f, 18f, paint)
    canvas.restore()
}

private fun drawSelectionBox(canvas: android.graphics.Canvas, layer: Layer) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    val rect = when (layer) {
        is TextLayer -> {
            val width = layer.fontSize * layer.text.length * 0.6f
            val left = when (layer.textAlignment) {
                TextAlignment.LEFT -> 0f
                TextAlignment.CENTER -> -width / 2f
                TextAlignment.RIGHT -> -width
            }
            RectF(
                left,
                0f,
                left + width,
                layer.fontSize * 1.2f
            )
        }
        is ImageLayer -> RectF(
            0f,
            0f,
            layer.width,
            layer.height
        )
        else -> RectF()
    }

    canvas.save()
    canvas.translate(layer.x, layer.y)
    canvas.rotate(layer.rotation, 0f, 0f)
    canvas.scale(layer.scale, layer.scale)
    canvas.drawRect(rect, paint)
    canvas.restore()
}
