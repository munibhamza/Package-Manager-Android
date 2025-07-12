package com.obittech.applocker.datastore


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    // Getters
    val appPinFlow: Flow<String> = dataStore.data.map { it[PreferenceKeys.APP_PIN] ?: "" }
    val useOverlayFlow: Flow<Boolean> = dataStore.data.map { it[PreferenceKeys.USE_OVERLAY] ?: false }
    val lockOnUnlockFlow: Flow<Boolean> = dataStore.data.map { it[PreferenceKeys.LOCK_ON_UNLOCK] ?: true }
    val overlayNeverAskFlow: Flow<Boolean> = dataStore.data.map { it[PreferenceKeys.OVERLAY_NEVER_ASK] ?: false }
    val overlayRemindLaterFlow: Flow<Boolean> = dataStore.data.map { it[PreferenceKeys.OVERLAY_REMIND_LATER] ?: true }
    val lastUnlockTimeFlow: Flow<Long> = dataStore.data.map { it[PreferenceKeys.LAST_UNLOCK_TIME] ?: 0L }

    // Setters
    suspend fun setAppPin(pin: String) = edit { it[PreferenceKeys.APP_PIN] = pin }
    suspend fun setUseOverlay(enabled: Boolean) = edit { it[PreferenceKeys.USE_OVERLAY] = enabled }
    suspend fun setLockOnUnlock(enabled: Boolean) = edit { it[PreferenceKeys.LOCK_ON_UNLOCK] = enabled }
    suspend fun setOverlayNeverAsk(value: Boolean) = edit { it[PreferenceKeys.OVERLAY_NEVER_ASK] = value }
    suspend fun setOverlayRemindLater(value: Boolean) = edit { it[PreferenceKeys.OVERLAY_REMIND_LATER] = value }
    suspend fun setLastUnlockTime(timeMillis: Long) = edit { it[PreferenceKeys.LAST_UNLOCK_TIME] = timeMillis }

    private suspend fun edit(transform: suspend (MutablePreferences) -> Unit) {
        dataStore.edit(transform)
    }

    val onboardingCompleteFlow: Flow<Boolean?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { prefs -> prefs[PreferenceKeys.ONBOARDING_COMPLETE] ?: false }

    suspend fun setOnboardingComplete(value: Boolean) = edit {
        it[PreferenceKeys.ONBOARDING_COMPLETE] = value
    }
}