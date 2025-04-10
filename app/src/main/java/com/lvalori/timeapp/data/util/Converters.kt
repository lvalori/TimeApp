package com.lvalori.timeapp.data.util

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.Duration

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromSeconds(seconds: Long?): Duration? {
        return seconds?.let { Duration.ofSeconds(it) }
    }

    @TypeConverter
    fun durationToSeconds(duration: Duration?): Long? {
        return duration?.seconds
    }
}