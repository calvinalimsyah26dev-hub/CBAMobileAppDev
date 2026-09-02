package com.example.cbamobileapp

import android.app.Application
import com.example.cbamobileapp.notification.NotificationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CbaMobileApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationHelper
            .createNotificationChannel(this)
    }
}