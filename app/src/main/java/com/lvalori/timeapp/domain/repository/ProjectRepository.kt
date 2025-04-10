package com.lvalori.timeapp.domain.repository

import com.lvalori.timeapp.domain.model.Phase
import com.lvalori.timeapp.domain.model.Project
import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface ProjectRepository {
    fun getAllProjects(): Flow<List<Project>>
    fun getProjectById(id: String): Flow<Project?>
    suspend fun insertProject(project: Project)
    suspend fun addTimeSession(
        projectId: String,
        duration: Duration,
        phase: Phase,
        customPhase: String?,
        description: String
    )
    suspend fun getProjectStats(projectId: String): Flow<ProjectStats>
}