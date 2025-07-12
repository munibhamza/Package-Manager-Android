package com.obittech.applocker.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.obittech.applocker.data.LockedAppRepository
import com.obittech.applocker.datastore.AppPreferencesManager
import com.obittech.applocker.ui.screens.lockScreen.LockScreenActivity
import com.obittech.applocker.utils.IntentKeys
import com.obittech.applocker.utils.unlockmanager.UnlockSessionManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class AppLockAccessibilityService : AccessibilityService(),CoroutineScope {

    private val tag = "AppLockAccessibilityService"
    private lateinit var repository: LockedAppRepository
    private lateinit var unlockManager: UnlockSessionManager
    private lateinit var preferences: AppPreferencesManager

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    private val serviceScope = CoroutineScope(Dispatchers.Default + job)

    private var lastUnlockedApp: String? = null
    var lockOnUnlock: Boolean?=true


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
        preferences = entryPoint.preferencesManager()

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
        executeLockedLogic(pkgName)

    }

    private fun executeLockedLogic(pkgName: String){
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
            val useOverlay = preferences.useOverlayFlow.firstOrNull() ?: false

            val lockedApp = repository.getLockedApp(pkgName)
            val isTemporarilyUnlocked = unlockManager.isTemporarilyUnlocked(pkgName)
            if (lockedApp != null && !isTemporarilyUnlocked) {
                Log.d(tag, "$pkgName is locked with pin.. launching pin screen")

                // 5. App is locked, prompt for PIN
                if (useOverlay) showOverlayPinDialog(pkgName, expectedPin = lockedApp.pin) else launchLockScreenActivity(pkgName)

            }
        }
    }

    private fun CoroutineScope.launchLockScreenActivity(pkgName: String) {
        val intent = Intent(this@AppLockAccessibilityService, LockScreenActivity::class.java).apply {
            putExtra(IntentKeys.TARGET_PACKAGE, pkgName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
    }


    private fun showOverlayPinDialog(pkgName: String, expectedPin: String){
        if (Settings.canDrawOverlays(this@AppLockAccessibilityService)) {
            val intent = Intent(this@AppLockAccessibilityService, OverlayService::class.java).apply {
                putExtra(IntentKeys.TARGET_PACKAGE, packageName)
                putExtra(IntentKeys.TARGET_PIN, expectedPin)
            }
            this@AppLockAccessibilityService.startService(intent)
        }
    }

    private val screenOffReceiver = object : BroadcastReceiver() {



        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                // Clear temporary unlocks here
                CoroutineScope(Dispatchers.IO).launch {
                    lockOnUnlock = preferences.lockOnUnlockFlow.firstOrNull() ?: true
                }
                if (lockOnUnlock == true){
                    unlockManager.clearAll()
                }
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

