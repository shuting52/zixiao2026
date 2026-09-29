package com.example.fontcraftpro.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.fontcraftpro.data.model.Layer
import com.example.fontcraftpro.data.model.TextLayer

@Composable
fun LayerPanel(
    layers: List<Layer>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    onBringToFront: () -> Unit,
    onSendToBack: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("图层")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("前置", modifier = Modifier.clickable { onBringToFront() })
                Text("后置", modifier = Modifier.clickable { onSendToBack() })
                Text("删除", modifier = Modifier.clickable { onDelete() })
            }
        }

        LazyColumn {
            items(layers) { layer ->
                val selected = layer.id == selectedId
                val label = when (layer) {
                    is TextLayer -> "文字：${layer.text.take(12)}"
                    else -> "图片"
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            else Color.Transparent
                        )
                        .clickable { onSelect(layer.id) }
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
