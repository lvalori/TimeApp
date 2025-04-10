package com.lvalori.timeapp.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Activity(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime? = null,
    val duration: Long = 0, // durata in secondi
    val isRunning: Boolean = false
)