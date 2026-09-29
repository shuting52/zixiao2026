package com.example.fontcraftpro.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Layer {
    abstract val id: String
    abstract val x: Float
    abstract val y: Float
    abstract val scale: Float
    abstract val rotation: Float
    abstract val alpha: Float
    abstract val zIndex: Int
}
