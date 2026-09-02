package com.example.cbamobileapp.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.cbamobileapp.MainActivity
import com.example.cbamobileapp.R

object NotificationHelper {

    const val CHANNEL_ID = "task_reminders"

    private const val CHANNEL_NAME = "Task reminders"
    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(
        context: Context
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description =
                "Daily reminders about unfinished productivity tasks"
        }

        val notificationManager =
            context.getSystemService(
                NotificationManager::class.java
            )

        notificationManager.createNotificationChannel(
            channel
        )
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showTaskReminder(
        context: Context,
        incompleteTaskCount: Int
    ) {
        if (!canShowNotifications(context)) {
            return
        }

        val message =
            if (incompleteTaskCount == 1) {
                "You have 1 unfinished task."
            } else {
                "You have $incompleteTaskCount unfinished tasks."
            }

        val openAppIntent = Intent(
            context,
            MainActivity::class.java
        )

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("AI Productivity Coach")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "$message Open the app and complete " +
                                "one small step today."
                    )
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat
            .from(context)
            .notify(
                NOTIFICATION_ID,
                notification
            )
    }

    fun canShowNotifications(
        context: Context
    ): Boolean {
        val permissionGranted =
            Build.VERSION.SDK_INT <
                    Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

        return permissionGranted &&
                NotificationManagerCompat
                    .from(context)
                    .areNotificationsEnabled()
    }
}