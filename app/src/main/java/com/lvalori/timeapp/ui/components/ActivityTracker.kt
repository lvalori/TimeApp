package com.lvalori.timeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lvalori.timeapp.domain.model.Activity
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.Duration
import java.time.format.DateTimeFormatter

@Composable
fun ActivityTracker(
    modifier: Modifier = Modifier
) {
    var activities by remember { mutableStateOf(listOf<Activity>()) }
    var currentActivity by remember { mutableStateOf<Activity?>(null) }
    var activityName by remember { mutableStateOf("") }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Activity Tracker",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = activityName,
                onValueChange = { activityName = it },
                label = { Text("Activity Name") },
                modifier = Modifier.fillMaxWidth(),
                enabled = currentActivity == null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (currentActivity == null && activityName.isNotBlank()) {
                            // Start new activity
                            val newActivity = Activity(
                                name = activityName,
                                startTime = LocalDateTime.now(ZoneOffset.UTC),
                                isRunning = true
                            )
                            currentActivity = newActivity
                            activities = activities + newActivity
                        } else if (currentActivity != null) {
                            // Stop current activity
                            val now = LocalDateTime.now(ZoneOffset.UTC)
                            val endedActivity = currentActivity!!.copy(
                                endTime = now,
                                isRunning = false,
                                duration = Duration.between(
                                    currentActivity!!.startTime,
                                    now
                                ).seconds
                            )
                            activities = activities.map {
                                if (it.id == endedActivity.id) endedActivity else it
                            }
                            currentActivity = null
                            activityName = ""
                        }
                    },
                    enabled = activityName.isNotBlank() || currentActivity != null,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (currentActivity == null) "Start" else "Stop")
                }

                if (currentActivity == null) {
                    Button(
                        onClick = { activityName = "" },
                        enabled = activityName.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Clear")
                    }
                }
            }

            if (activities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Recent Activities",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(activities.reversed()) { activity ->
                        ActivityListItem(activity = activity)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityListItem(
    activity: Activity,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = activity.name,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Started: ${activity.startTime.format(formatter)}",
                style = MaterialTheme.typography.bodyMedium
            )

            if (activity.endTime != null) {
                Text(
                    text = "Ended: ${activity.endTime.format(formatter)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Duration: ${formatDuration(activity.duration)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Running...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun formatDuration(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}