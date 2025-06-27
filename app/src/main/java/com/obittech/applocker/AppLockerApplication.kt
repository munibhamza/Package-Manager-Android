package com.obittech.applocker

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppLockerApplication : Application() {


    override fun onCreate() {
        super.onCreate()

        // Initialize anything that needs application context
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "app_locker_channel",
            "App Locker Service",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Used for app locker foreground service"
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}