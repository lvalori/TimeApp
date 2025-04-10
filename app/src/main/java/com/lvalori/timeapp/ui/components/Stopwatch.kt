package com.lvalori.timeapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.text.NumberFormat

@Composable
fun Stopwatch(
    modifier: Modifier = Modifier
) {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        val startTime = System.currentTimeMillis() - elapsedTime
        while (isRunning) {
            elapsedTime = System.currentTimeMillis() - startTime
            delay(10) // Aggiorniamo ogni 10ms
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Stopwatch",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = formatTime(elapsedTime),
                style = MaterialTheme.typography.displayMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { isRunning = !isRunning }
                ) {
                    Text(if (isRunning) "Stop" else "Start")
                }

                Button(
                    onClick = {
                        isRunning = false
                        elapsedTime = 0L
                    }
                ) {
                    Text("Reset")
                }
            }
        }
    }
}

private fun formatTime(timeMillis: Long): String {
    val hours = timeMillis / (1000 * 60 * 60)
    val minutes = (timeMillis % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (timeMillis % (1000 * 60)) / 1000
    val centiseconds = (timeMillis % 1000) / 10

    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, centiseconds)
}