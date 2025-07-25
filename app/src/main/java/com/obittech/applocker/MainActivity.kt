package com.obittech.applocker

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.obittech.applocker.datastore.AppPreferencesManager
import com.obittech.applocker.domain.models.AppInfo
import com.obittech.applocker.ui.screens.appsList.AppListScreen
import com.obittech.applocker.ui.screens.components.DrawerContent
import com.obittech.applocker.services.AccessibilityWatchdogService
import com.obittech.applocker.services.AppLockAccessibilityService
import com.obittech.applocker.ui.NavigationRoutes
import com.obittech.applocker.ui.navigation.AppNavigator
import com.obittech.applocker.ui.screens.about.AboutScreen
import com.obittech.applocker.ui.screens.onboarding.OnboardingScreen
import com.obittech.applocker.ui.screens.settings.SettingsScreen
import com.obittech.applocker.ui.screens.splash.SplashScreen
import com.obittech.applocker.ui.theme.AppLockerTheme
import com.obittech.applocker.utils.AccessibilityChecker
import com.obittech.applocker.utils.AccessibilityPromptDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var preferences: AppPreferencesManager
    @Inject lateinit var appNavigator: AppNavigator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        getCurrentLauncher()
//        startWatchDogService()


//        val installed_apps = queryInstalledApps()
//        val installed_packages = queryInstalledPackages()
//        val installed_launchable_apps = queryAllLaunchableApps()
//        val appsList_resolvedInfos = queryResolvedInfos(this@MainActivity)

        setContent {

            val navController = rememberNavController()
            appNavigator.setNavController(navController)

            val drawerState = rememberDrawerState(DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            var isReady by remember { mutableStateOf(false) }

            val onboardingComplete by preferences.onboardingCompleteFlow
                .collectAsState(initial = null)
            println("Onboarding value = $onboardingComplete")
            // ✅ Simulate splash loading before deciding navigation
            LaunchedEffect(onboardingComplete) {
                if (onboardingComplete != null) {
                    delay(1000) // still show splash briefly
                    isReady = true

                    navController.navigate(
                        if (onboardingComplete == true) NavigationRoutes.HOME else NavigationRoutes.ONBOARDING
                    ) {
                        popUpTo(NavigationRoutes.SPLASH) { inclusive = true }
                    }
                }
            }


            AppLockerTheme {

//                val showAccessibilityPermissionDialog = remember { mutableStateOf(false) }
//                LaunchedEffect(Unit) {
//                    val isEnabled = isAccessibilityServiceEnabled(
//                        this@MainActivity,
//                        AppLockAccessibilityService::class.java
//                    )
//                    if (!isEnabled) {
//                        showAccessibilityPermissionDialog.value = true
//                    }
//                }
//
//                AccessibilityPromptDialog(
//                    showDialog = showAccessibilityPermissionDialog.value,
//                    onDismiss = { showAccessibilityPermissionDialog.value = false },
//                    onOpenSettings = {
//                        showAccessibilityPermissionDialog.value = false
//                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        this@MainActivity.startActivity(intent)
//                    }
//                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(){onDestinationClicked ->
                            scope.launch { drawerState.close() }
                            when(onDestinationClicked){
                                NavigationRoutes.SETTINGS -> appNavigator.navigateToSettings()
                                NavigationRoutes.ABOUT -> appNavigator.navigateToAbout()
                            }
                        }
                    }
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = NavigationRoutes.SPLASH
                    ) {
                        composable(NavigationRoutes.SPLASH) { SplashScreen() }

                        composable(NavigationRoutes.ONBOARDING) {
                            OnboardingScreen(
                                onFinished = {
                                    appNavigator.navigateToHome()
                                }
                            )
                        }

                        composable(NavigationRoutes.HOME) {
                            AppListScreen(
                                openDrawer = {
                                    scope.launch { drawerState.open() }
                                }
                            )
                        }

                        composable(NavigationRoutes.SETTINGS) {
                            SettingsScreen()
                        }

                        composable(NavigationRoutes.ABOUT) {
                            AboutScreen()
                        }
                    }


//                    AppListScreen(
//                        openDrawer = {
//                            scope.launch { drawerState.open() }
//                        }
//                    )
                }
            }
        }
    }

    private fun startWatchDogService() {
        val serviceIntent = Intent(this, AccessibilityWatchdogService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }


    private fun getCurrentLauncher() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        val currentLauncherName = resolveInfo!!.activityInfo.packageName
        Log.d("Current Launcher: ", currentLauncherName)
    }

    private fun queryAllPackages():List<AppInfo> {
        //QUERY FILTERED APPS LAUNCHABLE INTENTS
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }

        return apps.map { app ->
            val appName = app.loadLabel(packageManager).toString()
            val packageName = app.packageName
            val icon = app.loadIcon(packageManager)
            AppInfo(
                packageName = packageName,
                name = appName,
                icon = icon
            )
        }

    }

    private fun queryInstalledApps():List<AppInfo> {
        //returns all apps including system
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        Log.d("QUERY_INSTALLED_APPS", "count: ${apps.size}")

        return apps.map { app ->
            val appName = app.loadLabel(packageManager).toString()
            val packageName = app.packageName
            val icon = app.loadIcon(packageManager)
            Log.d("QUERY_INSTALLED_APPS", "App Name: ${app.loadLabel(packageManager)}, Package Name: ${app.packageName}")
            AppInfo(
                packageName = packageName,
                name = appName,
                icon = icon
            )
        }
    }
    private fun queryInstalledPackages(): List<AppInfo>{
        //returns all packages including system
        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        Log.d("QUERY_INSTALLED_PACKAGES", "count: ${packages.size}")
        return packages.map { pkg ->
            val appName = pkg.applicationInfo?.loadLabel(packageManager).toString()
            val packageName = pkg.applicationInfo?.packageName?:"N/A"
            val icon = pkg.applicationInfo?.loadIcon(packageManager)
            Log.d("QUERY_INSTALLED_PACKAGES -> Apps  ", "App Name: $appName, Package Name: $packageName")
            AppInfo(packageName = packageName, name = appName, icon = icon)
        }
    }
    private fun queryAllLaunchableApps():List<AppInfo> {
        //returns apps with launchable intent
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }

        return apps.map { app ->
            val appName = app.loadLabel(packageManager).toString()
            val packageName = app.packageName
            val icon = app.loadIcon(packageManager)
            AppInfo(
                packageName = packageName,
                name = appName,
                icon = icon
            )
        }

    }
    private fun queryResolvedInfos(context: Context):List<AppInfo>{
        //returns installed apps with launchable intent
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            pm.queryIntentActivities(mainIntent, 0)
        }
        Log.d("QUERY_RESOLVED_INFOS", "list_count: ${resolvedInfos.size}")
        return resolvedInfos.map { it ->

            val resources =  pm.getResourcesForApplication(it.activityInfo.applicationInfo)
            val appName = if (it.activityInfo.labelRes != 0) {
                // getting proper label from resources
                resources.getString(it.activityInfo.labelRes)
            } else {
                // getting it out of app info - equivalent to context.packageManager.getApplicationInfo
                it.activityInfo.applicationInfo.loadLabel(pm).toString()
            }
            val pkgName = it.activityInfo.packageName
            val iconDrawable = it.activityInfo.loadIcon(pm)

            Log.d("QUERY_RESOLVED_INFOS", "AppName: $appName, PackageName: $pkgName")

            AppInfo(
                name = appName,
                packageName = pkgName,
                icon = iconDrawable,
            )

        }
    }
}
