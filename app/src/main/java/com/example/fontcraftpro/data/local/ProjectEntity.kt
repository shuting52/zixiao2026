package com.example.fontcraftpro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val width: Int,
    val height: Int,
    val backgroundJson: String,
    val layersJson: String,
    val updatedAt: Long
)
