package com.obittech.applocker.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.obittech.applocker.R
import com.obittech.applocker.utils.AccessibilityChecker


class AccessibilityWatchdogService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var checkRunnable: Runnable

    override fun onCreate() {
        super.onCreate()
        startForegroundServiceWithNotification()

        checkRunnable = Runnable {
            val isEnabled = AccessibilityChecker.isAccessibilityServiceEnabled(this, AppLockAccessibilityService::class.java)

            if (!isEnabled) {
                showServiceDisabledNotification()
            }

            handler.postDelayed(checkRunnable, 10_000) // check every 10 seconds
        }

        handler.post(checkRunnable)
    }

    private fun startForegroundServiceWithNotification() {
        val channelId = "watchdog_channel"
        val channelName = "Service Monitor"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(chan)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("App Locker Active")
            .setContentText("Monitoring accessibility service status.")
            .setSmallIcon(R.drawable.ic_lock)
            .build()

        startForeground(101, notification)
    }

    private fun showServiceDisabledNotification() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, "watchdog_channel")
            .setContentTitle("App Locker Disabled")
            .setContentText("Tap to re-enable accessibility service")
            .setSmallIcon(R.drawable.ic_warning)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(102, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}