package com.lvalori.timeapp.data.repository

import com.lvalori.timeapp.data.dao.ProjectDao
import com.lvalori.timeapp.data.entity.ProjectEntity
import com.lvalori.timeapp.data.entity.TimeSessionEntity
import com.lvalori.timeapp.domain.model.Project
import com.lvalori.timeapp.domain.model.ProjectStats
import kotlinx.coroutines.flow.*
import java.time.Duration

class ProjectRepository(private val projectDao: ProjectDao) {
    fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAllProjects().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    suspend fun getProjectById(projectId: String): Project? {
        return projectDao.getProjectById(projectId)?.toDomainModel()
    }

    fun getProjectStats(projectId: String): Flow<ProjectStats> {
        return combine(
            projectDao.getProjectTimeSessions(projectId),
            projectDao.getTotalProjectTime(projectId)
        ) { sessions, totalSeconds ->
            ProjectStats(
                totalTime = Duration.ofSeconds(totalSeconds ?: 0),
                sessionCount = sessions.size,
                averageSessionTime = if (sessions.isNotEmpty())
                    Duration.ofSeconds((totalSeconds ?: 0) / sessions.size)
                else Duration.ZERO
            )
        }
    }

    suspend fun insertProject(project: Project) {
        projectDao.insertProject(project.toEntity())
    }

    suspend fun insertTimeSession(session: TimeSessionEntity) {
        projectDao.insertTimeSession(session)
    }

    suspend fun deleteProject(project: Project) {
        projectDao.deleteProject(project.toEntity())
    }

    private fun ProjectEntity.toDomainModel(): Project {
        return Project(
            id = id,
            name = name,
            imagePath = imagePath,
            createdAt = createdAt
        )
    }

    private fun Project.toEntity(): ProjectEntity {
        return ProjectEntity(
            id = id,
            name = name,
            imagePath = imagePath,
            createdAt = createdAt
        )
    }
}