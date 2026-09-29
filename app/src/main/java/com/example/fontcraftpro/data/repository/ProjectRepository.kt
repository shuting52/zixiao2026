package com.example.fontcraftpro.data.repository

import com.example.fontcraftpro.data.local.ProjectDao
import com.example.fontcraftpro.data.local.ProjectEntity
import com.example.fontcraftpro.data.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun getProjects(): Flow<List<ProjectEntity>> = projectDao.getAll()

    suspend fun saveProject(project: Project) {
        projectDao.insert(
            ProjectEntity(
                id = project.id,
                name = project.name,
                width = project.width,
                height = project.height,
                backgroundJson = json.encodeToString(project.background),
                layersJson = json.encodeToString(project.layers),
                updatedAt = project.updatedAt
            )
        )
    }

    suspend fun getProjectById(id: String): Project? {
        val entity = projectDao.getById(id) ?: return null
        return Project(
            id = entity.id,
            name = entity.name,
            width = entity.width,
            height = entity.height,
            background = json.decodeFromString(entity.backgroundJson),
            layers = json.decodeFromString(entity.layersJson),
            updatedAt = entity.updatedAt
        )
    }
}
