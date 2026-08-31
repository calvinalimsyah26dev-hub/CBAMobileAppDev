package com.example.cbamobileapp.model

data class ProductivityTask(
    val id: Long,
    val title: String,
    val description: String,
    val priority: TaskPriority,
    val estimatedMin: Int,
    val isCompleted: Boolean = false
)
