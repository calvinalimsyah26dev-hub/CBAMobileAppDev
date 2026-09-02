package com.example.cbamobileapp.data.cloud

import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority

fun FirestoreTaskDocument.toProductivityTask(): ProductivityTask {
    val taskPriority = try {
        TaskPriority.valueOf(priority)
    } catch (exception: IllegalArgumentException) {
        TaskPriority.MEDIUM
    }

    return ProductivityTask(
        id = id,
        title = title,
        description = description,
        priority = taskPriority,
        estimatedMin = estimatedMinutes,
        isCompleted = completed
    )
}

fun ProductivityTask.toFirestoreTaskDocument(
    generatedId: Long
): FirestoreTaskDocument {
    return FirestoreTaskDocument(
        id = generatedId,
        title = title,
        description = description,
        priority = priority.name,
        estimatedMinutes = estimatedMin,
        completed = isCompleted,
        createdAt = generatedId
    )
}