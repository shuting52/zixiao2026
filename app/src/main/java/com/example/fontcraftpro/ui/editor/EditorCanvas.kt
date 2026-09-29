package com.example.fontcraftpro.ui.editor

import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
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
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = layer.fontSize
        color = layer.textColor
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.LEFT
        if (layer.shadowRadius > 0f) {
            setShadowLayer(layer.shadowRadius, layer.shadowDx, layer.shadowDy, layer.shadowColor)
        }
    }

    val metrics = paint.fontMetrics
    val baseline = -metrics.ascent

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
        canvas.drawText(layer.text, 0f, baseline, strokePaint)
    }

    canvas.drawText(layer.text, 0f, baseline, paint)
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
        is TextLayer -> RectF(
            layer.x,
            layer.y,
            layer.x + layer.fontSize * layer.text.length * 0.6f,
            layer.y + layer.fontSize * 1.2f
        )
        is ImageLayer -> RectF(
            layer.x,
            layer.y,
            layer.x + layer.width,
            layer.y + layer.height
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
