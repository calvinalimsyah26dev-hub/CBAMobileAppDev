package com.example.cbamobileapp.data.cloud

data class FirestoreTaskDocument(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val priority: String = "MEDIUM",
    val estimatedMinutes: Int = 0,
    val completed: Boolean = false,
    val createdAt: Long = 0
)
