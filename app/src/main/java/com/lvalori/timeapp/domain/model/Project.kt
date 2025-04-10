package com.lvalori.timeapp.domain.model

import java.time.LocalDateTime

data class Project(
    val id: String,
    val name: String,
    val imagePath: String?,
    val createdAt: LocalDateTime
)