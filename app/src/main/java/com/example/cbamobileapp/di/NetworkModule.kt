package com.example.cbamobileapp.di

import com.example.cbamobileapp.data.remote.QuoteApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit():
            Retrofit {

        return Retrofit.Builder()
            .baseUrl(
                "https://dummyjson.com/"
            )
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideQuoteApiService(
        retrofit: Retrofit
    ): QuoteApiService {

        return retrofit.create(
            QuoteApiService::class.java
        )
    }
}