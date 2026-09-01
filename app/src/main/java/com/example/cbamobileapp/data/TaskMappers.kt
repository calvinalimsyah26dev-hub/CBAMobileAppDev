package com.example.cbamobileapp.data

import com.example.cbamobileapp.data.local.TaskEntity
import com.example.cbamobileapp.model.ProductivityTask
import com.example.cbamobileapp.model.TaskPriority

fun TaskEntity.toProductivityTask():
        ProductivityTask {

    val taskPriority = try {
        TaskPriority.valueOf(priority)
    } catch (
        exception: IllegalArgumentException
    ) {
        TaskPriority.MEDIUM
    }

    return ProductivityTask(
        id = id,
        title = title,
        description = description,
        priority = taskPriority,
        estimatedMin = estimatedMinutes,
        isCompleted = isCompleted
    )
}

fun ProductivityTask.toTaskEntity():
        TaskEntity {

    return TaskEntity(
        id = if (id > 0) id else 0,
        title = title,
        description = description,
        priority = priority.name,
        estimatedMinutes = estimatedMin,
        isCompleted = isCompleted
    )
}