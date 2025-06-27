package com.obittech.applocker.presentation.screens.lockScreen

object LockRequestManager {
    var pendingAppPackage: String? = null
    var unlockCallback: (() -> Unit)? = null
}