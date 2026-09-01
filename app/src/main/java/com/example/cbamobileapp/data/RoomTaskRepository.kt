package com.example.cbamobileapp.data

import com.example.cbamobileapp.data.local.TaskDao
import com.example.cbamobileapp.model.ProductivityTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomTaskRepository @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override val tasks:
            Flow<List<ProductivityTask>> =
        taskDao
            .observeAllTasks()
            .map { taskEntities ->
                taskEntities.map { entity ->
                    entity.toProductivityTask()
                }
            }

    override suspend fun addTask(
        task: ProductivityTask
    ) {
        taskDao.insertTask(
            task.toTaskEntity()
        )
    }

    override suspend fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    ) {
        taskDao.updateTaskCompletion(
            taskId = taskId,
            isCompleted = isCompleted
        )
    }
}