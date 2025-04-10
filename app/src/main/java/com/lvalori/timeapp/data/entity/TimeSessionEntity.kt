package com.lvalori.timeapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Duration
import java.time.LocalDateTime

@Entity(
    tableName = "time_sessions",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TimeSessionEntity(
    @PrimaryKey
    val id: String,
    val projectId: String,
    val duration: Duration,
    val phase: String,
    val customPhase: String?,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)