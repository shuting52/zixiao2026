package com.example.fontcraftpro.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val name: String,
    val width: Int,
    val height: Int,
    val background: Background,
    val layers: List<Layer>,
    val updatedAt: Long
)
