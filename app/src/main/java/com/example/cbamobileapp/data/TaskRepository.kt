package com.example.cbamobileapp.data

import com.example.cbamobileapp.model.ProductivityTask
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    val tasks: Flow<List<ProductivityTask>>

    suspend fun addTask(
        task: ProductivityTask
    )

    suspend fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    )
}