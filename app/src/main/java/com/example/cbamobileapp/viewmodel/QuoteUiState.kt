package com.example.cbamobileapp.viewmodel

import com.example.cbamobileapp.model.MotivationalQuote

sealed interface QuoteUiState {

    data object Loading :
        QuoteUiState

    data class Success(
        val quote: MotivationalQuote
    ) : QuoteUiState

    data class Error(
        val message: String
    ) : QuoteUiState
}