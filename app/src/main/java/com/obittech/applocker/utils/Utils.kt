package com.obittech.applocker.utils

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

//fun isAccessibilityServiceEnabled(context: Context, serviceClass: Class<out AccessibilityService>): Boolean {
//    val expectedComponent = ComponentName(context, serviceClass)
//    val enabledServices = Settings.Secure.getString(
//        context.contentResolver,
//        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
//    ) ?: return false
//
//    return enabledServices.split(":").any {
//        ComponentName.unflattenFromString(it) == expectedComponent
//    }
//}

object LockedAppsCache {
    private val lockedAppsMap = mutableMapOf<String, String>()

    fun setLockedApps(map: Map<String, String>) {
        synchronized(this) {
            lockedAppsMap.clear()
            lockedAppsMap.putAll(map)
        }
    }

    fun isAppLocked(packageName: String): Boolean {
        return lockedAppsMap.containsKey(packageName)
    }

    fun verifyPin(packageName: String, pin: String): Boolean {
        return lockedAppsMap[packageName] == pin
    }
}

fun setAppHidden(context: Context, packageName: String, hide: Boolean) {
    val pm = context.packageManager
    val launchIntent = pm.getLaunchIntentForPackage(packageName) ?: return
    val componentName = launchIntent.component ?: return

    val newState = if (hide)
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    else
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED

    pm.setComponentEnabledSetting(
        componentName,
        newState,
        PackageManager.DONT_KILL_APP
    )
}


fun isSystemApp(context: Context, pkg: String): Boolean {
    return try {
        val pm = context.packageManager
        val appInfo = pm.getApplicationInfo(pkg, 0)
        (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0 ||
                (appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

object IntentKeys {
    const val TARGET_PACKAGE = "TARGET_PACKAGE"
    const val TARGET_PIN = "TARGET_PIN"
}

fun isApi33Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
