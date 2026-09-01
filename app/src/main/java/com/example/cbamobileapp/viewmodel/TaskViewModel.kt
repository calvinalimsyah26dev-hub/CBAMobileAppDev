package com.example.cbamobileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cbamobileapp.data.TaskRepository
import com.example.cbamobileapp.model.ProductivityTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val tasks: StateFlow<List<ProductivityTask>> =
        taskRepository.tasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5_000
            ),
            initialValue = emptyList()
        )

    fun addTask(
        task: ProductivityTask
    ) {
        viewModelScope.launch {
            taskRepository.addTask(task)
        }
    }

    fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {
            taskRepository.updateTaskCompletion(
                taskId = taskId,
                isCompleted = isCompleted
            )
        }
    }
}