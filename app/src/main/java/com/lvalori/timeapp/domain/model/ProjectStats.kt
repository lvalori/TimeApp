package com.lvalori.timeapp.domain.model

import java.time.Duration

data class ProjectStats(
    val totalTime: Duration,
    val sessionCount: Int,
    val averageSessionTime: Duration
)