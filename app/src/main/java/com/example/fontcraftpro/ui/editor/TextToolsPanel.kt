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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.fontcraftpro.data.model.TextLayer

@Composable
fun TextToolsPanel(
    selected: TextLayer?,
    onTextSizeChange: (Float) -> Unit,
    onStrokeWidthChange: (Float) -> Unit,
    onShadowRadiusChange: (Float) -> Unit,
    onColorChange: (Int) -> Unit
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
