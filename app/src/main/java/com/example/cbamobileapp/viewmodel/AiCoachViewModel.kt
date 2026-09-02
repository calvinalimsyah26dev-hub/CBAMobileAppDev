package com.example.cbamobileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cbamobileapp.data.AiCoachRepository
import com.example.cbamobileapp.model.ProductivityTask
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AiCoachViewModel @Inject constructor(
    private val repository: AiCoachRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AiCoachUiState>(
            AiCoachUiState.Ready
        )

    val uiState: StateFlow<AiCoachUiState> =
        _uiState.asStateFlow()

    private var previousQuestion: String = ""

    fun askCoach(
        question: String,
        tasks: List<ProductivityTask>
    ) {
        val cleanedQuestion = question.trim()

        if (cleanedQuestion.isBlank()) {
            _uiState.value =
                AiCoachUiState.Error(
                    "Please enter a question."
                )

            return
        }

        previousQuestion = cleanedQuestion

        viewModelScope.launch {
            _uiState.value =
                AiCoachUiState.Loading

            _uiState.value = try {
                val activeTasks =
                    tasks.filterNot { task ->
                        task.isCompleted
                    }

                val answer =
                    repository.askCoach(
                        question = cleanedQuestion,
                        tasks = activeTasks
                    )

                AiCoachUiState.Success(
                    response = answer
                )
            } catch (exception: IOException) {
                AiCoachUiState.Error(
                    "Check your internet connection."
                )
            } catch (exception: Exception) {
                AiCoachUiState.Error(
                    message =
                        exception.message
                            ?: "The AI coach is unavailable."
                )
            }
        }
    }

    fun retry(
        tasks: List<ProductivityTask>
    ) {
        if (previousQuestion.isNotBlank()) {
            askCoach(
                question = previousQuestion,
                tasks = tasks
            )
        }
    }

    fun reset() {
        _uiState.value =
            AiCoachUiState.Ready
    }
}