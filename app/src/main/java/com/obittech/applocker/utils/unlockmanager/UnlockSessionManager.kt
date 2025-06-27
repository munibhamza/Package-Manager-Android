package com.obittech.applocker.utils.unlockmanager

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnlockSessionManager @Inject constructor() {

//    private val unlockedApps = mutableMapOf<String, Long>()
//    private val handlerMap = mutableMapOf<String, Handler>()
//    private val timeoutMillis = 5 * 60 * 1000L // 5 minutes

//    fun unlock(packageName: String) {
//        unlockedApps[packageName] = System.currentTimeMillis()
//        scheduleClear(packageName)
//    }
//
//    fun isTemporarilyUnlocked(packageName: String): Boolean {
//        return unlockedApps.containsKey(packageName)
//    }
//
//    fun remove(packageName: String) {
//        unlockedApps.remove(packageName)
//        handlerMap[packageName]?.removeCallbacksAndMessages(null)
//        handlerMap.remove(packageName)
//    }
//
//    fun clearAll() {
//        unlockedApps.clear()
//        handlerMap.values.forEach { it.removeCallbacksAndMessages(null) }
//        handlerMap.clear()
//    }
//
//    private fun scheduleClear(packageName: String) {
//        val handler = Handler(Looper.getMainLooper())
//        val runnable = Runnable {
//            remove(packageName)
//        }
//        handler.postDelayed(runnable, timeoutMillis)
//        handlerMap[packageName] = handler
//    }

    private val unlockSet = mutableSetOf<String>()
    private val lockTimers = mutableMapOf<String, Job>()


    fun isTemporarilyUnlocked(packageName: String): Boolean {
        return unlockSet.contains(packageName)
    }

    fun unlockApp(packageName: String) {
        unlockSet.add(packageName)
        cancelLockTimer(packageName)
    }

    fun markAppInBackground(packageName: String, scope: CoroutineScope) {
        cancelLockTimer(packageName)

        // Schedule re-lock after 5 min of inactivity
        val job = scope.launch {
            delay(5 * 60 * 1000)
            unlockSet.remove(packageName)
            lockTimers.remove(packageName)
        }
        lockTimers[packageName] = job
    }

    fun markAppInForeground(packageName: String) {
        cancelLockTimer(packageName)
    }

    fun clearAll() {
        unlockSet.clear()
        lockTimers.values.forEach { it.cancel() }
        lockTimers.clear()
    }

    private fun cancelLockTimer(packageName: String) {
        lockTimers[packageName]?.cancel()
        lockTimers.remove(packageName)
    }
}