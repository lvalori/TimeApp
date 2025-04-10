package com.lvalori.timeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvalori.timeapp.domain.model.Phase
import com.lvalori.timeapp.domain.model.Project
import com.lvalori.timeapp.domain.model.ProjectStats
import com.lvalori.timeapp.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    val projects: StateFlow<List<Project>> = projectRepository
        .getAllProjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getProject(id: String): StateFlow<Project?> = projectRepository
        .getProjectById(id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun getProjectStats(id: String): StateFlow<ProjectStats> = projectRepository
        .getProjectStats(id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProjectStats(Duration.ZERO, 0, Duration.ZERO)
        )

    fun addTimeSession(
        projectId: String,
        duration: Duration,
        phase: Phase,
        customPhase: String?,
        description: String
    ) {
        viewModelScope.launch {
            projectRepository.addTimeSession(
                projectId = projectId,
                duration = duration,
                phase = phase,
                customPhase = customPhase,
                description = description
            )
        }
    }

    fun createProject(name: String, imagePath: String?) {
        viewModelScope.launch {
            projectRepository.createProject(name, imagePath)
        }
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            projectRepository.deleteProject(id)
        }
    }
}