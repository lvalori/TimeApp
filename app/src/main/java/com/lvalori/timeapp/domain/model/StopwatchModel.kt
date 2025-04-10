package com.lvalori.timeapp.domain.model

data class StopwatchTime(
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val milliseconds: Int = 0
)

data class StopwatchState(
    val time: StopwatchTime = StopwatchTime(),
    val isRunning: Boolean = false
)