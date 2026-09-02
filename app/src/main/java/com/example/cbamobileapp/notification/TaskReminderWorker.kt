package com.example.cbamobileapp.notification

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TaskReminderWorker(
    appContext: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(
    appContext,
    workerParameters
) {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        val firebaseAuth =
            FirebaseAuth.getInstance()

        val userId =
            firebaseAuth.currentUser?.uid
                ?: return Result.success()

        return try {
            val unfinishedTasks =
                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(userId)
                    .collection("tasks")
                    .whereEqualTo(
                        "completed",
                        false
                    )
                    .get()
                    .await()

            val taskCount =
                unfinishedTasks.documents.size

            if (taskCount > 0) {
                NotificationHelper.showTaskReminder(
                    context = applicationContext,
                    incompleteTaskCount = taskCount
                )
            }

            Result.success()
        } catch (exception: Exception) {
            Result.retry()
        }
    }
}