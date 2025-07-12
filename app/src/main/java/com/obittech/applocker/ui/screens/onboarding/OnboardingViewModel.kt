package com.obittech.applocker.ui.screens.onboarding

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obittech.applocker.datastore.AppPreferencesManager
import com.obittech.applocker.services.AppLockAccessibilityService
import com.obittech.applocker.ui.screens.components.CircleStepStatus
import com.obittech.applocker.utils.AccessibilityChecker.isAccessibilityServiceEnabled
import com.obittech.applocker.utils.OverlayPermissionChecker.isOverlayPermissionGranted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val context: Application,
    private val preferences: AppPreferencesManager
) : ViewModel() {

    private val totalSteps = 5 // or make it dynamic
    private val _stepStatuses = mutableStateListOf<CircleStepStatus>().apply {
        repeat(totalSteps) { add(CircleStepStatus.PENDING) }
    }
    val stepStatuses: List<CircleStepStatus> get() = _stepStatuses
    val stepLabels = listOf("Permissions", "Overlay", "Password", "Settings", "Finish")

    private val _shouldShowAllow = mutableStateOf(false)
    val shouldShowAllow: State<Boolean> = _shouldShowAllow

    private val _permissionError = mutableStateOf(false)
    val permissionError: State<Boolean> = _permissionError
    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    private val _isNextButtonEnabled = mutableStateOf(true)
    val isNextButtonEnabled: State<Boolean> = _isNextButtonEnabled

    private val _appPin = mutableStateOf("")
    val appPin: State<String> = _appPin

    fun evaluateStepPermissions() {
        val index = _stepIndex.value
        when (index) {
            0 -> {
                val allowed = isAccessibilityServiceEnabled(context, AppLockAccessibilityService::class.java)
                _shouldShowAllow.value = !allowed
                _isNextButtonEnabled.value = allowed
                if (!allowed) triggerPermissionError(ErrorType.Accessibility)
            }
            1 -> {
                val allowed = !isOverlayPermissionGranted(context)
                _shouldShowAllow.value = !allowed
                _isNextButtonEnabled.value = allowed
                if (!allowed) triggerPermissionError(ErrorType.Overlay)
            }
            else -> {
                _shouldShowAllow.value = false
                _isNextButtonEnabled.value = true
                _permissionError.value = false
            }
        }
    }

    fun triggerPermissionError(type: ErrorType) {
        when (type) {
            ErrorType.Notification -> _errorMessage.value = "Notification permission is required."
            ErrorType.Accessibility -> _errorMessage.value = "Accessibility access is required. Or you can skip this step else way."
            ErrorType.AllowPermission -> _errorMessage.value = "Permission is allowed.. or you can skip this step else way."
            ErrorType.INVALID_PIN -> _errorMessage.value = "Pin must not be empty & minimum 4 digits."
            ErrorType.Overlay -> _errorMessage.value = "Overlay permission is required. You can skip this step else way."
        }
        _permissionError.value = true
        viewModelScope.launch {
            delay(3000)
            _permissionError.value = false
        }
    }

    fun onNextClicked(
        requestPermissionLauncher: (() -> Unit)? = null,
        onFinished: () -> Unit
    ) {
        val index = _stepIndex.value

        if (index == 0 && !isAccessibilityServiceEnabled(context, AppLockAccessibilityService::class.java)) {
            triggerPermissionError(ErrorType.Accessibility)
            requestPermissionLauncher?.invoke()
            return
        }

        if (index == 1 && !isOverlayPermissionGranted(context)) {
            triggerPermissionError(ErrorType.Overlay)
            requestPermissionLauncher?.invoke()
            return
        }
        if (index ==2 && (appPin.value.length<3)){
            triggerPermissionError(ErrorType.INVALID_PIN)
            return
        }else if (index == 2 && appPin.value.length>=3){
            onAppPinConfirmed()
        }

        // All good → mark step and continue
        markStepCompleted(index)

        if (index == stepLabels.lastIndex) {
            completeOnboarding(onFinished)
        } else {
            goToNextStep()
        }
    }

    fun markStepCompleted(index: Int) {
        if (index in _stepStatuses.indices) {
            _stepStatuses[index] = CircleStepStatus.COMPLETED
        }
    }

    fun markStepSkipped(index: Int) {
        if (index in _stepStatuses.indices) {
            _stepStatuses[index] = CircleStepStatus.SKIPPED
        }
    }

    // Holds current onboarding step index
    private val _stepIndex = mutableStateOf(0)
    val stepIndex: State<Int> get() = _stepIndex

    private val _previousStep = mutableStateOf(0)

    fun jumpToStep(index: Int) {
        if (index in stepLabels.indices) {
            _stepIndex.value = index
        }
    }

    fun goToNextStep() {
        _previousStep.value = _stepIndex.value
        _stepIndex.value++
    }

    fun goToPreviousStep() {
        if (_stepIndex.value > 0) {
            _previousStep.value = _stepIndex.value
            _stepIndex.value--
        }
    }

    fun completeOnboarding(onCompleted: () -> Unit) {
        viewModelScope.launch {
            preferences.setOnboardingComplete(true)
            onCompleted()
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

    fun onAppPinEntered(pin: String) {
        _appPin.value = pin
    }

    fun onAppPinConfirmed() {
        viewModelScope.launch {
            preferences.setAppPin(appPin.value)
            _appPin.value = ""
        }
    }


}

enum class OnboardingStepStatus {
    PENDING,
    COMPLETED,
    SKIPPED
}

enum class ErrorType {
    Notification, Accessibility, Overlay, AllowPermission, INVALID_PIN
}
