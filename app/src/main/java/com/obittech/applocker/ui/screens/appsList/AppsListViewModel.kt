package com.obittech.applocker.ui.screens.appsList

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.obittech.applocker.data.LockedAppRepository
import com.obittech.applocker.data.source.LockedAppEntity
import com.obittech.applocker.domain.models.AppInfo
import com.obittech.applocker.security.PasswordHasher
import com.obittech.applocker.services.AppLockAccessibilityService
import com.obittech.applocker.ui.screens.onboarding.ErrorType
import com.obittech.applocker.utils.AccessibilityChecker
import com.obittech.applocker.utils.AccessibilityChecker.isAccessibilityServiceEnabled
import com.obittech.applocker.utils.OverlayPermissionChecker.isOverlayPermissionGranted
import com.obittech.applocker.utils.setAppHidden
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsListViewModel @Inject constructor(
    private val lockedAppRepository: LockedAppRepository,
    @ApplicationContext private val context: Context
):ViewModel() {

    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent: SharedFlow<String> = _snackbarEvent.asSharedFlow()

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _showAccessibilityPrompt = mutableStateOf(false)
    val showAccessibilityPrompt: State<Boolean> = _showAccessibilityPrompt

    init {
        loadApps()
        _showAccessibilityPrompt.value = !isAccessibilityServiceEnabled(context, AppLockAccessibilityService::class.java)
    }

    fun setShowAccessibilityPrompt(value: Boolean) {
        _showAccessibilityPrompt.value = value
    }

    fun isAccessibilityServiceEnabled(): Boolean {
        return !AccessibilityChecker.isAccessibilityServiceEnabled(context, AppLockAccessibilityService::class.java)
    }

    fun loadApps() {
        viewModelScope.launch {
            val lockedApps = lockedAppRepository.getAllLockedApps()
            val lockedPackages = lockedApps.map { it.packageName }.toSet()

            val packageManager = context.packageManager
            val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }

            _apps.value = installedApps.map {
                AppInfo(
                    packageName = it.packageName,
                    name = context.packageManager.getApplicationLabel(it).toString(),
                    icon = context.packageManager.getApplicationIcon(it),
                    isLocked = lockedPackages.contains(it.packageName)
                )
            }
        }
    }


    suspend fun getAllAppsWithLockStatus(context: Context): List<AppInfo> {
        val packageManager = context.packageManager
//        val installedPackages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

//        //QUERY FILTERED APPS LAUNCHABLE INTENTS
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }


        // Step 1: Fetch all locked apps from DB
        val lockedApps = lockedAppRepository.getAllLockedApps()
        val lockedPackages = lockedApps.map { it.packageName }.toSet()

        // Step 2: Map installed apps to AppInfo
        return installedApps.map { app ->
            AppInfo(
                packageName = app.packageName,
                name = packageManager.getApplicationLabel(app).toString(),
                icon = packageManager.getApplicationIcon(app),
                isLocked = lockedPackages.contains(app.packageName)
            )
        }
    }

//    private val _uiState = MutableStateFlow(AppsListUiState())
//    val uiState: StateFlow<AppsListUiState> = _uiState.asStateFlow()

    fun lockApp(pin:String, app: AppInfo){
        viewModelScope.launch {
            lockedAppRepository.lockApp(LockedAppEntity(packageName = app.packageName, pin = pin))
            _snackbarEvent.emit("${app.name} is locked with a PIN")
            loadApps()
        }
    }

    fun unlockApp(pin:String, app: AppInfo){
        viewModelScope.launch {
            lockedAppRepository.unlockApp(LockedAppEntity(app.packageName,pin))
            _snackbarEvent.emit("${app.name} is unlocked with a PIN")
            loadApps()
        }
    }

    fun onLockToggle(app: AppInfo){
        viewModelScope.launch {
            if (app.isLocked) {
                lockedAppRepository.deleteByPackageName(app.packageName)
            } else {
                // You can set a default pin here or open PIN setup screen
                val pinHash = PasswordHasher.hashPin("1234") // Example only!
                lockedAppRepository.lockApp(
                    LockedAppEntity(packageName = app.packageName, pin = pinHash)
                )
            }

            // Refresh the list after DB update
            loadApps()
        }
    }

    fun onAppClick(app: AppInfo) {
        //Set Pin Dialog
    }


    fun hideApp(context: Context, packageName: String) {
        viewModelScope.launch {
            setAppHidden(context, packageName, true)
        }
    }

    fun unhideApp(context: Context, packageName: String) {
        viewModelScope.launch {
            setAppHidden(context, packageName, false)
        }
    }

}