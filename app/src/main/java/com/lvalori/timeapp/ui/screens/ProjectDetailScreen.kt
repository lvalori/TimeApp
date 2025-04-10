package com.lvalori.timeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lvalori.timeapp.domain.model.Phase
import com.lvalori.timeapp.domain.model.Project
import com.lvalori.timeapp.domain.model.ProjectStats
import com.lvalori.timeapp.ui.components.PhaseSelectionDialog
import com.lvalori.timeapp.ui.icons.CustomIcons.Average
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(
    project: Project?,
    stats: ProjectStats,
    onNavigateBack: () -> Unit,
    onTimeTracked: (duration: Duration, phase: Phase, customPhase: String?, description: String) -> Unit
) {
    var isTracking by remember { mutableStateOf(false) }
    var trackingStartTime by remember { mutableStateOf<LocalDateTime?>(null) }
    var elapsedTime by remember { mutableStateOf<Duration>(Duration.ZERO) }
    var showPhaseDialog by remember { mutableStateOf(false) }
    var currentDuration by remember { mutableStateOf<Duration?>(null) }

    LaunchedEffect(isTracking) {
        while (isTracking) {
            delay(1000)
            trackingStartTime?.let { startTime ->
                elapsedTime = Duration.between(startTime, LocalDateTime.now())
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(project?.name ?: "Dettaglio Progetto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (project != null) {
                ProjectHeader(project)

                if (showPhaseDialog && currentDuration != null) {
                    PhaseSelectionDialog(
                        onDismiss = {
                            showPhaseDialog = false
                            currentDuration = null
                        },
                        onConfirm = { phase, customPhase, description ->
                            currentDuration?.let { duration ->
                                onTimeTracked(duration, phase, customPhase, description)
                            }
                            showPhaseDialog = false
                            currentDuration = null
                        }
                    )
                }

                TimeTracker(
                    isTracking = isTracking,
                    elapsedTime = elapsedTime,
                    onStartTracking = {
                        isTracking = true
                        trackingStartTime = LocalDateTime.now()
                        elapsedTime = Duration.ZERO
                    },
                    onStopTracking = {
                        isTracking = false
                        trackingStartTime?.let { startTime ->
                            val duration = Duration.between(startTime, LocalDateTime.now())
                            currentDuration = duration
                            showPhaseDialog = true
                        }
                        trackingStartTime = null
                    }
                )

                ProjectStats(stats)

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun TimeTracker(
    isTracking: Boolean,
    elapsedTime: Duration,
    onStartTracking: () -> Unit,
    onStopTracking: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatDuration(elapsedTime),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = if (!isTracking) onStartTracking else onStopTracking,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!isTracking) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Inizia")
                } else {
                    Icon(Icons.Default.Stop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ferma")
                }
            }
        }
    }
}

@Composable
private fun ProjectStats(stats: ProjectStats) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Statistiche",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            StatsRow(
                icon = Icons.Default.Timer,
                label = "Tempo totale",
                value = formatDuration(stats.totalTime)
            )

            StatsRow(
                icon = Icons.Default.List,
                label = "Sessioni completate",
                value = stats.sessionCount.toString()
            )

            if (stats.sessionCount > 0) {
                StatsRow(
                    icon = Average,
                    label = "Media per sessione",
                    value = formatDuration(stats.averageSessionTime)
                )
            }
        }
    }
}

@Composable
private fun StatsRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ProjectHeader(
    project: Project
) {
    Column {
        AsyncImage(
            model = project.imagePath,
            contentDescription = "Project image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = "Creato il ${project.createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun formatDuration(duration: Duration): String {
    val totalSeconds = duration.seconds
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}