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
fun FontLibrarySheet(
    fonts: List<ImportedFont>,
    onApply: (ImportedFont) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.34f))
            .fillMaxWidth()
            .padding(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF171C2C).copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("字体库", style = MaterialTheme.typography.titleMedium, color = Color.White)

                if (fonts.isEmpty()) {
                    Text("暂无本地字体，先导入一个字体文件即可。", color = Color.White.copy(alpha = 0.7f))
                } else {
                    fonts.forEach { font ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White.copy(alpha = 0.04f), RoundedCornerShape(12.dp))
                                .clickable { onApply(font) }
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(font.name, color = Color.White)
                                Text("预览：字效设计", color = Color.White.copy(alpha = 0.7f))
                            }
                            Text("应用", color = Color(0xFF9BB7FF))
                        }
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
