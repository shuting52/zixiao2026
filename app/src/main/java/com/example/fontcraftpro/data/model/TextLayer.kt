package com.example.fontcraftpro.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class TextAlignment {
    LEFT,
    CENTER,
    RIGHT
}

@Serializable
enum class TextEffect {
    NORMAL,
    OUTER_GLOW,
    INNER_GLOW,
    BEVEL,
    STROKE,
    CHROME,
    GOLD,
    NEON,
    GRADIENT
}

@Serializable
enum class TextShape {
    FLAT,
    OUTLINE,
    ROUNDED
}

@Serializable
enum class TextAnimation {
    NONE,
    PULSE,
    SWING,
    FADE
}

@Serializable
enum class TextPreset {
    CUSTOM,
    POSTER,
    NEON,
    METAL,
    RETRO,
    POP
}

@Serializable
data class TextLayer(
    override val id: String,
    override val x: Float,
    override val y: Float,
    val text: String,
    val fontName: String = "default",
    val fontPath: String? = null,
    val fontSize: Float = 72f,
    val textColor: Int = 0xFFFFFFFF.toInt(),
    val strokeColor: Int = 0x00000000,
    val strokeWidth: Float = 0f,
    val shadowRadius: Float = 0f,
    val shadowDx: Float = 0f,
    val shadowDy: Float = 0f,
    val shadowColor: Int = 0x00000000,
    val textAlignment: TextAlignment = TextAlignment.LEFT,
    val effect: TextEffect = TextEffect.NORMAL,
    val shape: TextShape = TextShape.FLAT,
    val curveOffset: Float = 0f,
    val animation: TextAnimation = TextAnimation.NONE,
    val is3D: Boolean = false,
    val extrudeDepth: Int = 0,
    val preset: TextPreset = TextPreset.CUSTOM,
    override val scale: Float = 1f,
    override val rotation: Float = 0f,
    override val alpha: Float = 1f,
    override val zIndex: Int = 0
) : Layer()
