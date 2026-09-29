package com.example.fontcraftpro.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Background(
    val type: BackgroundType = BackgroundType.SOLID,
    val solidColor: Int = 0xFFFFFFFF.toInt(),
    val startColor: Int = 0xFF2196F3.toInt(),
    val endColor: Int = 0xFF9C27B0.toInt(),
    val imageUri: String? = null
)

enum class BackgroundType {
    SOLID,
    GRADIENT,
    IMAGE
}
