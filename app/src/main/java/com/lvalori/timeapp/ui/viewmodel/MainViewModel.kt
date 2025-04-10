package com.lvalori.timeapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lvalori.timeapp.domain.model.Phase
import com.lvalori.timeapp.domain.model.Project
import com.lvalori.timeapp.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.UUID
import javax.inject.Inject

data class MainUiState(
    val projects: List<Project> = emptyList(),
    val selectedProject: Project? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
    }

    private fun loadProjects() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                projectRepository.getAllProjects().collect { projects ->
                    _uiState.value = _uiState.value.copy(
                        projects = projects,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun selectProject(projectId: String) {
        viewModelScope.launch {
            projectRepository.getProjectById(projectId).collect { project ->
                _uiState.value = _uiState.value.copy(selectedProject = project)
            }
        }
    }

    fun addProject(name: String, imagePath: String?) {
        viewModelScope.launch {
            val newProject = Project(
                id = UUID.randomUUID().toString(),
                name = name,
                imagePath = imagePath,
                createdAt = java.time.LocalDateTime.now()
            )
            projectRepository.insertProject(newProject)
        }
    }

    fun updateTimeSession(
        duration: Duration,
        phase: Phase,
        customPhase: String?,
        description: String
    ) {
        viewModelScope.launch {
            _uiState.value.selectedProject?.let { project ->
                projectRepository.addTimeSession(
                    projectId = project.id,
                    duration = duration,
                    phase = phase,
                    customPhase = customPhase,
                    description = description
                )
            }
        }
    }
}