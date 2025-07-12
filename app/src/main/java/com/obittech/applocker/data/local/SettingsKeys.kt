package com.obittech.applocker.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    val PIN = stringPreferencesKey("app_pin")
    val USE_OVERLAY = booleanPreferencesKey("use_overlay")
    val LOCK_ON_UNLOCK = booleanPreferencesKey("lock_on_unlock")
    val OVERLAY_NEVER_ASK = booleanPreferencesKey("overlay_never_ask")
    val OVERLAY_REMIND_LATER = booleanPreferencesKey("overlay_remind_later")
}