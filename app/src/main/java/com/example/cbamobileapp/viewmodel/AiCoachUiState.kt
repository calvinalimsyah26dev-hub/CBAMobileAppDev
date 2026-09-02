package com.example.cbamobileapp.viewmodel

sealed interface AiCoachUiState {

    data object Ready : AiCoachUiState

    data object Loading : AiCoachUiState

    data class Success(
        val response: String
    ) : AiCoachUiState

    data class Error(
        val message: String
    ) : AiCoachUiState
}