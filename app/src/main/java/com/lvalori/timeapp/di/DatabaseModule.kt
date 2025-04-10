package com.lvalori.timeapp.di

import android.content.Context
import androidx.room.Room
import com.lvalori.timeapp.data.AppDatabase
import com.lvalori.timeapp.data.dao.ProjectDao
import com.lvalori.timeapp.data.dao.TimeSessionDao
import com.lvalori.timeapp.data.repository.ProjectRepositoryImpl
import com.lvalori.timeapp.domain.repository.ProjectRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "time_app_database"
        ).build()
    }

    @Provides
    fun provideProjectDao(database: AppDatabase): ProjectDao = database.projectDao()

    @Provides
    fun provideTimeSessionDao(database: AppDatabase): TimeSessionDao = database.timeSessionDao()

    @Provides
    @Singleton
    fun provideProjectRepository(
        projectDao: ProjectDao,
        timeSessionDao: TimeSessionDao
    ): ProjectRepository = ProjectRepositoryImpl(projectDao, timeSessionDao)
}