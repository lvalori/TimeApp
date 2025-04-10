package com.lvalori.timeapp.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NewProject : Screen("new_project")
    object ExistingProjects : Screen("existing_projects")
    object ProjectDetail : Screen("project/{projectId}") {
        fun createRoute(projectId: String) = "project/$projectId"
    }
}