package com.example.cbamobileapp.di

import com.example.cbamobileapp.repository.AuthRepository
import com.example.cbamobileapp.repository.FirebaseAuthRepository
import com.example.cbamobileapp.data.NetworkQuoteRepository
import com.example.cbamobileapp.data.QuoteRepository
import com.example.cbamobileapp.data.FirestoreTaskRepository
import com.example.cbamobileapp.data.TaskRepository
import com.example.cbamobileapp.data.AiCoachRepository
import com.example.cbamobileapp.data.FirebaseAiCoachRepository
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
        implementation: FirestoreTaskRepository
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(
        implementation: NetworkQuoteRepository
    ): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        implementation: FirebaseAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAiCoachRepository(
        implementation: FirebaseAiCoachRepository
    ): AiCoachRepository
}