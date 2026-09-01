package com.example.cbamobileapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query(
        """
        SELECT *
        FROM tasks
        ORDER BY id DESC
        """
    )
    fun observeAllTasks(): Flow<List<TaskEntity>>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertTask(
        task: TaskEntity
    )

    @Query(
        """
        UPDATE tasks
        SET isCompleted = :isCompleted
        WHERE id = :taskId
        """
    )
    suspend fun updateTaskCompletion(
        taskId: Long,
        isCompleted: Boolean
    )
}