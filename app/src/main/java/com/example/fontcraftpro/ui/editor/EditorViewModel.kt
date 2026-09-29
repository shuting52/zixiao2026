package com.example.fontcraftpro.ui.editor

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
import com.example.fontcraftpro.data.model.TextLayer
import com.example.fontcraftpro.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: ProjectRepository
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
}
