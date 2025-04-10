package com.lvalori.timeapp.data.dao

import androidx.room.*
import com.lvalori.timeapp.data.entity.TimeSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeSessionDao {
    @Query("SELECT * FROM time_sessions WHERE projectId = :projectId")
    fun getSessionsByProjectId(projectId: String): Flow<List<TimeSessionEntity>>

    @Query("SELECT COUNT(*) FROM time_sessions WHERE projectId = :projectId")
    fun getSessionCount(projectId: String): Flow<Int>

    @Query("""
        SELECT SUM(CAST((julianday(endTime) - julianday(startTime)) * 24 * 60 * 60 AS INTEGER))
        FROM time_sessions
        WHERE projectId = :projectId
    """)
    fun getTotalProjectTime(projectId: String): Flow<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: TimeSessionEntity)
}