package com.example.cbamobileapp.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority
import javax.inject.Inject

class InMemoryTaskRepository @Inject constructor() :
    TaskRepository {

    private val _tasks = mutableStateOf(
        listOf(
            ProductivityTask(
                id = 1,
                title = "Complete Android project",
                description = "Finish the Week 7 repository",
                priority = TaskPriority.HIGH,
                estimatedMin = 60
            ),
            ProductivityTask(
                id = 2,
                title = "Review Kotlin",
                description = "Practise interfaces and implementations",
                priority = TaskPriority.MEDIUM,
                estimatedMin = 30
            ),
            ProductivityTask(
                id = 3,
                title = "Plan tomorrow",
                description = "Choose three important tasks",
                priority = TaskPriority.LOW,
                estimatedMin = 10
            )
        )
    )

    override val tasks:
            State<List<ProductivityTask>>
        get() = _tasks

    override fun addTask(
        task: ProductivityTask
    ) {
        _tasks.value = _tasks.value + task
    }

    override fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    ) {
        _tasks.value = _tasks.value.map { task ->
            if (task.id == taskId) {
                task.copy(
                    isCompleted = isCompleted
                )
            } else {
                task
            }
        }
    }
}