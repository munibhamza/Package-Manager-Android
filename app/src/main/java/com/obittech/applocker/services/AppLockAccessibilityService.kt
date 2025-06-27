package com.obittech.applocker.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.obittech.applocker.MainActivity
import com.obittech.applocker.data.LockedAppRepository
import com.obittech.applocker.presentation.screens.lockScreen.LockRequestManager
import com.obittech.applocker.presentation.screens.lockScreen.LockScreenActivity
import com.obittech.applocker.utils.LockedAppsCache
import com.obittech.applocker.utils.OverlayManager
import com.obittech.applocker.utils.isSystemApp
import com.obittech.applocker.utils.unlockmanager.UnlockSessionManager
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class AppLockAccessibilityService : AccessibilityService(),CoroutineScope {

    private lateinit var repository: LockedAppRepository
    private lateinit var unlockManager: UnlockSessionManager
    private val tag = "AppLockAccessibilityService"

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val serviceScope = CoroutineScope(Dispatchers.Default + job)

    private var lastUnlockedApp: String? = null


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onServiceConnected() {
        Log.d(tag, "Accessibility service connected")

        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenOffReceiver, filter)
        val unlockSuccessfilter = IntentFilter("com.obittech.applocker.UNLOCK_SUCCESS")
        registerReceiver(unlockBroadcastReceiver, unlockSuccessfilter, RECEIVER_NOT_EXPORTED)

        val entryPoint = EntryPointAccessors.fromApplication(
            this@AppLockAccessibilityService,
            AccessibilityServiceEntryPoint::class.java
        )
        unlockManager = entryPoint.unlockManager()
        repository = entryPoint.lockedAppRepository()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            packageNames = null // Listen to all packages
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
            flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS or
                    AccessibilityServiceInfo.DEFAULT or
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
        }
        this.serviceInfo = info

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.d(tag, "Accessibility event received... EVENT: ${event?.eventType}")
        if (event == null ) return

        val pkgName = event.packageName?.toString() ?: return
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                if (unlockManager.isTemporarilyUnlocked(pkgName)) {
                    unlockManager.markAppInForeground(pkgName)
                }
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                // Optional: can track last known app for fallback
            }
        }

        // Handle locked logic
        checkAndShowPinIfLocked(pkgName)

    }

    private fun checkAndShowPinIfLocked(pkgName: String) {
        if (unlockManager.isTemporarilyUnlocked(pkgName)) return

        // Check if app switch occurred
        if (pkgName != lastUnlockedApp) {
            lastUnlockedApp?.let { oldPkg ->
                if (unlockManager.isTemporarilyUnlocked(oldPkg)) {
                    unlockManager.markAppInBackground(oldPkg, serviceScope)
                }
            }

            if (unlockManager.isTemporarilyUnlocked(pkgName)) {
                unlockManager.markAppInForeground(pkgName)
                lastUnlockedApp = pkgName
            }
        }

        // 4. Check Room DB asynchronously
        CoroutineScope(Dispatchers.IO).launch {
            Log.d(tag, "checking room DB for $pkgName")
            val lockedApp = repository.getLockedApp(pkgName)
            Log.d(tag, "lockedApp: ${lockedApp?.packageName}")
            val isTemporarilyUnlocked = unlockManager.isTemporarilyUnlocked(pkgName)

            if (lockedApp != null && !isTemporarilyUnlocked) {
                Log.d(tag, "$pkgName is locked with pin.. launching pin screen")
                // 5. App is locked, prompt for PIN
                val intent = Intent(this@AppLockAccessibilityService, LockScreenActivity::class.java).apply {
                    putExtra("packageName", pkgName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }
        }
    }

    private val screenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                // Clear temporary unlocks here
                unlockManager.clearAll()
            }
        }
    }

    private val unlockBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val pkg = intent?.getStringExtra("PACKAGE_NAME") ?: return

            // Launch the app now
//            launchApp(pkg)
        }
    }

    override fun onInterrupt() {
        Log.w(tag, "Accessibility service interrupted")
    }

    override fun onDestroy() {
        unregisterReceiver(screenOffReceiver)
        unregisterReceiver(unlockBroadcastReceiver)
        job.cancel()
        super.onDestroy()
    }

    private fun takeIntruderSelfie() {
//        val intent = Intent(this, IntruderCameraActivity::class.java).apply {
//            putExtra("package_name", lastPackageName)
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//        startActivity(intent)
    }
}

