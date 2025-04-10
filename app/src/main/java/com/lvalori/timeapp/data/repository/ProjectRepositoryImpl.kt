package com.lvalori.timeapp.data.repository

import com.lvalori.timeapp.data.dao.ProjectDao
import com.lvalori.timeapp.data.dao.TimeSessionDao
import com.lvalori.timeapp.data.entity.ProjectEntity
import com.lvalori.timeapp.data.entity.TimeSessionEntity
import com.lvalori.timeapp.domain.model.Phase
import com.lvalori.timeapp.domain.model.Project
import com.lvalori.timeapp.domain.model.ProjectStats
import com.lvalori.timeapp.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.*
import java.time.Duration
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val timeSessionDao: TimeSessionDao
) : ProjectRepository {

    override fun getAllProjects(): Flow<List<Project>> =
        projectDao.getAllProjects().map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getProjectById(id: String): Flow<Project?> =
        projectDao.getProjectById(id).map { it?.toDomain() }

    override suspend fun insertProject(project: Project) {
        projectDao.insertProject(
            ProjectEntity(
                id = project.id,
                name = project.name,
                imagePath = project.imagePath,
                createdAt = project.createdAt
            )
        )
    }

    override suspend fun addTimeSession(
        projectId: String,
        duration: Duration,
        phase: Phase,
        customPhase: String?,
        description: String
    ) {
        timeSessionDao.insertSession(
            TimeSessionEntity(
                id = UUID.randomUUID().toString(),
                projectId = projectId,
                duration = duration,
                phase = phase.name,
                customPhase = customPhase,
                description = description,
                startTime = LocalDateTime.now(),
                endTime = LocalDateTime.now().plus(duration)
            )
        )
    }

    override fun getProjectStats(projectId: String): Flow<ProjectStats> =
        combine(
            timeSessionDao.getTotalProjectTime(projectId),
            timeSessionDao.getSessionCount(projectId)
        ) { totalTime, count ->
            ProjectStats(
                totalTime = Duration.ofSeconds(totalTime),
                sessionCount = count,
                averageSessionTime = if (count > 0) Duration.ofSeconds(totalTime / count) else Duration.ZERO
            )
        }
}