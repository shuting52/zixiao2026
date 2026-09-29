package com.example.fontcraftpro.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.fontcraftpro.data.model.TextAlignment
import com.example.fontcraftpro.data.model.TextEffect
import com.example.fontcraftpro.data.model.TextLayer

@Composable
fun TextToolsPanel(
    selected: TextLayer?,
    onTextSizeChange: (Float) -> Unit,
    onStrokeWidthChange: (Float) -> Unit,
    onShadowRadiusChange: (Float) -> Unit,
    onColorChange: (Int) -> Unit,
    onAlphaChange: (Float) -> Unit,
    onAlignmentChange: (TextAlignment) -> Unit,
    onFontChange: (String) -> Unit,
    onEffectChange: (TextEffect) -> Unit,
    onCurveChange: (Float) -> Unit,
    onAnimationChange: (com.example.fontcraftpro.data.model.TextAnimation) -> Unit,
    onPresetApply: (com.example.fontcraftpro.data.model.TextPreset) -> Unit
) {
    if (selected == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text("文字大小: ${selected.fontSize.toInt()}", style = MaterialTheme.typography.labelMedium)
        Slider(
            value = selected.fontSize,
            onValueChange = onTextSizeChange,
            valueRange = 20f..180f
        )

        Text("描边: ${selected.strokeWidth.toInt()}", style = MaterialTheme.typography.labelMedium)
        Slider(
            value = selected.strokeWidth,
            onValueChange = onStrokeWidthChange,
            valueRange = 0f..32f
        )

        Text("阴影: ${selected.shadowRadius.toInt()}", style = MaterialTheme.typography.labelMedium)
        Slider(
            value = selected.shadowRadius,
            onValueChange = onShadowRadiusChange,
            valueRange = 0f..30f
        )

        Text("透明度: ${(selected.alpha * 100).toInt()}%", style = MaterialTheme.typography.labelMedium)
        Slider(
            value = selected.alpha,
            onValueChange = onAlphaChange,
            valueRange = 0.2f..1f
        )

        Text("对齐", style = MaterialTheme.typography.labelMedium)
        Row {
            listOf(TextAlignment.LEFT, TextAlignment.CENTER, TextAlignment.RIGHT).forEach { alignment ->
                val label = when (alignment) {
                    TextAlignment.LEFT -> "左"
                    TextAlignment.CENTER -> "中"
                    TextAlignment.RIGHT -> "右"
                }

                OutlinedButton(
                    onClick = { onAlignmentChange(alignment) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(label)
                }
            }
        }

        Text("字体", style = MaterialTheme.typography.labelMedium)
        Row {
            listOf(
                "default" to "默认",
                "bold" to "粗体",
                "serif" to "衬线",
                "mono" to "等宽"
            ).forEach { (fontName, label) ->
                OutlinedButton(
                    onClick = { onFontChange(fontName) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(label)
                }
            }
        }

        Text("字效", style = MaterialTheme.typography.labelMedium)
        Row {
            listOf(
                TextEffect.NORMAL to "普通",
                TextEffect.OUTER_GLOW to "外发光",
                TextEffect.INNER_GLOW to "内发光",
                TextEffect.BEVEL to "浮雕",
                TextEffect.STROKE to "描边",
                TextEffect.CHROME to "铬金",
                TextEffect.GOLD to "金色",
                TextEffect.NEON to "霓虹",
                TextEffect.GRADIENT to "渐变"
            ).forEach { (effect, label) ->
                OutlinedButton(
                    onClick = { onEffectChange(effect) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(label)
                }
            }
        }

        Text("弧形", style = MaterialTheme.typography.labelMedium)
        Row {
            listOf(0f to "直线", 32f to "上弧", -32f to "下弧").forEach { (curve, label) ->
                OutlinedButton(
                    onClick = { onCurveChange(curve) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(label)
                }
            }
        }

        Text("动画", style = MaterialTheme.typography.labelMedium)
        Row {
            listOf(
                com.example.fontcraftpro.data.model.TextAnimation.NONE to "无",
                com.example.fontcraftpro.data.model.TextAnimation.PULSE to "脉冲",
                com.example.fontcraftpro.data.model.TextAnimation.SWING to "摆动",
                com.example.fontcraftpro.data.model.TextAnimation.FADE to "淡入"
            ).forEach { (animation, label) ->
                OutlinedButton(
                    onClick = { onAnimationChange(animation) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(label)
                }
            }
        }

        Text("模板", style = MaterialTheme.typography.labelMedium)
        Row {
            listOf(
                com.example.fontcraftpro.data.model.TextPreset.POSTER to "海报",
                com.example.fontcraftpro.data.model.TextPreset.NEON to "霓虹",
                com.example.fontcraftpro.data.model.TextPreset.METAL to "金属",
                com.example.fontcraftpro.data.model.TextPreset.RETRO to "复古",
                com.example.fontcraftpro.data.model.TextPreset.POP to "流行"
            ).forEach { (preset, label) ->
                OutlinedButton(
                    onClick = { onPresetApply(preset) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(label)
                }
            }
        }

        Row(modifier = Modifier.padding(top = 8.dp)) {
            listOf(
                Color.White,
                Color.Black,
                Color.Red,
                Color.Blue,
                Color.Green,
                Color.Yellow
            ).forEach { color ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(24.dp)
                        .background(color, CircleShape)
                        .clickable { onColorChange(color.toArgb()) }
                )
            }
        }
    }
}
