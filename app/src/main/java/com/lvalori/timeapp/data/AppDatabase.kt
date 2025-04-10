package com.lvalori.timeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lvalori.timeapp.data.dao.ProjectDao
import com.lvalori.timeapp.data.entity.ProjectEntity
import com.lvalori.timeapp.data.entity.TimeSessionEntity
import com.lvalori.timeapp.data.util.Converters

@Database(
    entities = [
        ProjectEntity::class,
        TimeSessionEntity::class
    ],
    version = 2, // Aumentato da 1 a 2
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "timeapp_database"
                )
                    .fallbackToDestructiveMigration() // Questo permetterà di ricreare il database se la versione cambia
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}