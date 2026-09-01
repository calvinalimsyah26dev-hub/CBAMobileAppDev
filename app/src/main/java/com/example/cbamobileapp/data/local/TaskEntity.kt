package com.example.cbamobileapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks"
)
data class TaskEntity(
    @PrimaryKey(
        autoGenerate = true
    )
    val id: Long = 0,

    val title: String,
    val description: String,
    val priority: String,
    val estimatedMinutes: Int,
    val isCompleted: Boolean
)