package com.obittech.applocker.utils

object Constants {
    private var pinInterface = PinInterface.LOCK_SCREEN
    fun getPinInterface(): String {
        return pinInterface
    }
    fun setPinInterface(value: String) {
        pinInterface = value
    }
}

object PinInterface {
    const val OVERLAY_DIALOG = "overlay_dialog"
    const val LOCK_SCREEN = "lock_screen"
}