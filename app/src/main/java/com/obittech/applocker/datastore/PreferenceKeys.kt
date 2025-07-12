package com.obittech.applocker.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val APP_PIN = stringPreferencesKey("app_pin")
    val USE_OVERLAY = booleanPreferencesKey("use_overlay")
    val LOCK_ON_UNLOCK = booleanPreferencesKey("lock_on_unlock")
    val OVERLAY_NEVER_ASK = booleanPreferencesKey("overlay_never_ask")
    val OVERLAY_REMIND_LATER = booleanPreferencesKey("overlay_remind_later")
    val LAST_UNLOCK_TIME = longPreferencesKey("last_unlock_time")
    val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
}