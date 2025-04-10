package com.lvalori.timeapp.domain.model

import java.time.LocalDateTime
import java.time.ZoneOffset

data class TimeModel(
    val dateTime: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC),
    val username: String = ""
)