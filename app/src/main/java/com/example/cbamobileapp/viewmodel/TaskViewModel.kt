package com.example.cbamobileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cbamobileapp.data.TaskRepository
import com.example.cbamobileapp.model.ProductivityTask
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val tasks: List<ProductivityTask>
        get() = taskRepository.tasks.value

    fun addTask(
        task: ProductivityTask
    ) {
        taskRepository.addTask(task)
    }

    fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    ) {
        taskRepository.updateTaskCompletion(
            taskId = taskId,
            isCompleted = isCompleted
        )
    }
}