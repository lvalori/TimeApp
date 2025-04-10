package com.lvalori.timeapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val imagePath: String?,
    val createdAt: LocalDateTime
) {
    fun toDomain() = com.lvalori.timeapp.domain.model.Project(
        id = id,
        name = name,
        imagePath = imagePath,
        createdAt = createdAt
    )
}