package com.obittech.applocker.ui.screens.lockScreen

object LockRequestManager {
    var pendingAppPackage: String? = null
    var unlockCallback: (() -> Unit)? = null
}