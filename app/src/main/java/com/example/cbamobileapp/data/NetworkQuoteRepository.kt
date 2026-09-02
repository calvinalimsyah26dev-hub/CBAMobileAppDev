package com.example.cbamobileapp.data

import com.example.cbamobileapp.data.remote.QuoteApiService
import com.example.cbamobileapp.model.MotivationalQuote
import javax.inject.Inject

class NetworkQuoteRepository @Inject constructor(
    private val quoteApiService: QuoteApiService
) : QuoteRepository {

    override suspend fun getRandomQuote():
            MotivationalQuote {

        val response =
            quoteApiService.getRandomQuote()

        return MotivationalQuote(
            text = response.quote,
            author = response.author
        )
    }
}