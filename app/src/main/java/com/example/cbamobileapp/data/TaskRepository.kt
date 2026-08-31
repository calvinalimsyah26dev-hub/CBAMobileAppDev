package com.example.cbamobileapp.data

import androidx.compose.runtime.State
import com.example.cbamobileapp.model.ProductivityTask

interface TaskRepository {
    val tasks: State<List<ProductivityTask>>

    fun addTask(
        task: ProductivityTask
    )

    fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    )
}