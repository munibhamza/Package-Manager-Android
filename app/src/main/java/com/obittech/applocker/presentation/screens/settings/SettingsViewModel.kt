package com.obittech.applocker.presentation.screens.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.prefs.Preferences
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
//            dataStore.data.collect { prefs ->
//                _uiState.value = SettingsUiState(
//                    useOverlay = prefs[SettingsKeys.USE_OVERLAY] ?: false,
//                    lockOnUnlock = prefs[SettingsKeys.LOCK_ON_UNLOCK] ?: true
//                )
//            }
        }
    }

    fun setOverlayUsage(enabled: Boolean) {
        viewModelScope.launch {
//            dataStore.edit { it[SettingsKeys.USE_OVERLAY] = enabled }
        }
    }

    fun setLockOnUnlock(enabled: Boolean) {
        viewModelScope.launch {
//            dataStore.edit { it[SettingsKeys.LOCK_ON_UNLOCK] = enabled }
        }
    }
}

// SettingsUiState.kt
data class SettingsUiState(
    val useOverlay: Boolean = false,
    val lockOnUnlock: Boolean = true
)

// SettingsDataStoreModule.kt


// SettingsKeys.kt
object SettingsKeys {
    val USE_OVERLAY = booleanPreferencesKey("use_overlay")
    val LOCK_ON_UNLOCK = booleanPreferencesKey("lock_on_unlock")
}
