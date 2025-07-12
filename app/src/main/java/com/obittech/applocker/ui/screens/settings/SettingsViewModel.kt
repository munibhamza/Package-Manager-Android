package com.obittech.applocker.ui.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obittech.applocker.datastore.AppPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: AppPreferencesManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = SettingsUiState(
                useOverlay = preferences.useOverlayFlow.firstOrNull() ?: false,
                lockOnUnlock = preferences.lockOnUnlockFlow.firstOrNull() ?: true,
                pin = preferences.appPinFlow.firstOrNull() ?: "",
                onboardingComplete = preferences.onboardingCompleteFlow.firstOrNull() ?: true
            )
        }
    }

    fun setOverlayUsage(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setUseOverlay(enabled)
        }
    }

    fun setLockOnUnlock(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setLockOnUnlock(enabled)
        }
    }

    fun setOnboardingComplete(enabled: Boolean) {
        viewModelScope.launch {
            preferences.setOnboardingComplete(enabled)
        }
    }

    fun setPin(pin: String) {
        viewModelScope.launch {
            preferences.setAppPin(pin)
        }
    }
}

// SettingsUiState.kt
data class SettingsUiState(
    var useOverlay: Boolean = false,
    val lockOnUnlock: Boolean = true,
    val onboardingComplete: Boolean = true,
    val pin: String = ""
)