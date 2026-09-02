package com.example.cbamobileapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cbamobileapp.data.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val quoteRepository:
    QuoteRepository
) : ViewModel() {

    var uiState: QuoteUiState by
    mutableStateOf(
        QuoteUiState.Loading
    )
        private set

    init {
        refreshQuote()
    }

    fun refreshQuote() {
        uiState = QuoteUiState.Loading

        viewModelScope.launch {
            uiState = try {
                val quote =
                    quoteRepository
                        .getRandomQuote()

                QuoteUiState.Success(
                    quote = quote
                )
            } catch (
                exception: IOException
            ) {
                QuoteUiState.Error(
                    message =
                        "Check your internet connection."
                )
            } catch (
                exception: Exception
            ) {
                QuoteUiState.Error(
                    message =
                        "Unable to load a quote."
                )
            }
        }
    }
}