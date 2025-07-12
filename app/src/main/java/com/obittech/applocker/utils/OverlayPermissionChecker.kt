package com.obittech.applocker.utils

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.net.toUri

object OverlayPermissionChecker {
    fun isOverlayPermissionGranted(context: Context): Boolean {
        return Settings.canDrawOverlays(context)
    }
    fun requestOverlayPermission(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            "package:${context.packageName}".toUri()
        )
        try {
            context.startActivity(intent)
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(context, "Unable to open overlay settings", Toast.LENGTH_SHORT).show()
        }
    }
}