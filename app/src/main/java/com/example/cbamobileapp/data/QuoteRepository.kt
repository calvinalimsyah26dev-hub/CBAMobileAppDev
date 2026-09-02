package com.example.cbamobileapp.data

import com.example.cbamobileapp.model.MotivationalQuote

interface QuoteRepository {

    suspend fun getRandomQuote():
            MotivationalQuote
}