package com.example.cbamobileapp.notification

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object ReminderScheduler {

    private const val WORK_NAME =
        "daily_task_reminder"

    private const val PREFERENCES_NAME =
        "reminder_preferences"

    private const val ENABLED_KEY =
        "daily_reminders_enabled"

    fun enableReminders(
        context: Context
    ) {
        setEnabledPreference(
            context = context,
            enabled = true
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(
                NetworkType.CONNECTED
            )
            .build()

        val reminderRequest =
            PeriodicWorkRequestBuilder<TaskReminderWorker>(
                24,
                TimeUnit.HOURS
            )
                .setInitialDelay(
                    calculateDelayUntilNineAm(),
                    TimeUnit.MILLISECONDS
                )
                .setConstraints(constraints)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                reminderRequest
            )
    }

    fun disableReminders(
        context: Context
    ) {
        setEnabledPreference(
            context = context,
            enabled = false
        )

        WorkManager
            .getInstance(context)
            .cancelUniqueWork(WORK_NAME)
    }

    fun areRemindersEnabled(
        context: Context
    ): Boolean {
        return context
            .getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE
            )
            .getBoolean(
                ENABLED_KEY,
                false
            )
    }

    private fun setEnabledPreference(
        context: Context,
        enabled: Boolean
    ) {
        context
            .getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putBoolean(
                ENABLED_KEY,
                enabled
            )
            .apply()
    }

    private fun calculateDelayUntilNineAm(): Long {
        val now = Calendar.getInstance()

        val nextReminder =
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 9)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                if (before(now)) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

        return nextReminder.timeInMillis -
                now.timeInMillis
    }
}