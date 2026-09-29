package com.example.fontcraftpro.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageLayer(
    override val id: String,
    override val x: Float,
    override val y: Float,
    val uri: String,
    val width: Float,
    val height: Float,
    override val scale: Float = 1f,
    override val rotation: Float = 0f,
    override val alpha: Float = 1f,
    override val zIndex: Int = 0
) : Layer()
