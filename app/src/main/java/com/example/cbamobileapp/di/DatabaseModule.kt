package com.example.cbamobileapp.di

import android.content.Context
import androidx.room.Room
import com.example.cbamobileapp.data.local.AppDatabase
import com.example.cbamobileapp.data.local.TaskDao
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
            "productivity_coach.db"
        ).build()
    }

    @Provides
    fun provideTaskDao(
        database: AppDatabase
    ): TaskDao {
        return database.taskDao()
    }
}