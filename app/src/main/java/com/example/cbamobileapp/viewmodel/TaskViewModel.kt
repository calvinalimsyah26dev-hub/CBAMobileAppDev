package com.example.cbamobileapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority

class TaskViewModel : ViewModel(){
    /*
     * The task list is now owned by the ViewModel.
     *
     * Compose will update any screen that reads this property
     * when its value changes.
     */
    var tasks by mutableStateOf(
        listOf(
            ProductivityTask(
                id = 1,
                title = "Complete Android project",
                description = "Finish the Week 6 navigation",
                priority = TaskPriority.HIGH,
                estimatedMin = 60
            ),
            ProductivityTask(
                id = 2,
                title = "Review Kotlin",
                description = "Practise data classes and collections",
                priority = TaskPriority.MEDIUM,
                estimatedMin = 30
            ),
            ProductivityTask(
                id = 3,
                title = "Plan tomorrow",
                description = "Choose the three most important tasks",
                priority = TaskPriority.LOW,
                estimatedMin = 10
            )
        )
    )
        private set

    fun addTask(
        task: ProductivityTask
    ) {
        tasks = tasks + task
    }

    fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    ) {
        tasks = tasks.map { task ->
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