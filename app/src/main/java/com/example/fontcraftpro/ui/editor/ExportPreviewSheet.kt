package com.example.fontcraftpro.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExportPreviewSheet(
    projectSummary: String,
    layerCount: Int,
    onExportImage: () -> Unit,
    onExportJson: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.35f))
            .fillMaxWidth()
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF181D2B).copy(alpha = 0.96f))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("导出预览", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text("图层数量：$layerCount", color = Color.White.copy(alpha = 0.8f))
                Text(
                    text = projectSummary.take(180) + if (projectSummary.length > 180) "..." else "",
                    color = Color.White.copy(alpha = 0.72f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onExportImage, modifier = Modifier.weight(1f)) {
                        Text("导出 PNG")
                    }
                    Button(onClick = onExportJson, modifier = Modifier.weight(1f)) {
                        Text("导出 JSON")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "关闭",
                        color = Color(0xFF9BB7FF),
                        modifier = Modifier.clickable { onDismiss() }
                    )
                }
            }
        }
    }
}
