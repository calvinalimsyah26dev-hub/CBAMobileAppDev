package com.example.cbamobileapp.di

import com.example.cbamobileapp.data.InMemoryTaskRepository
import com.example.cbamobileapp.data.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        implementation: InMemoryTaskRepository
    ): TaskRepository
}