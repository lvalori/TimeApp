package com.lvalori.timeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lvalori.timeapp.ui.navigation.AppNavigation
import com.lvalori.timeapp.ui.theme.TimeAppTheme
import com.lvalori.timeapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

                    AppNavigation(
                        uiState = uiState.value,
                        onProjectSelected = viewModel::selectProject,
                        onAddProject = viewModel::addProject,
                        onUpdateTimeSession = viewModel::updateTimeSession
                    )
                }
            }
        }
    }
}