package com.obittech.applocker

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import com.obittech.applocker.domain.models.AppInfo
import com.obittech.applocker.ui.screens.appsList.AppListScreen
import com.obittech.applocker.ui.screens.components.DrawerContent
import com.obittech.applocker.services.AccessibilityWatchdogService
import com.obittech.applocker.services.AppLockAccessibilityService
import com.obittech.applocker.ui.screens.settings.SettingsScreen
import com.obittech.applocker.ui.theme.AppLockerTheme
import com.obittech.applocker.utils.AccessibilityPromptDialog
import com.obittech.applocker.utils.isAccessibilityServiceEnabled
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        getCurrentLauncher()
//        startWatchDogService()

        val appsList = queryAllPackages()
//        val appsList = queryResolvedInfos(this@MainActivity)
        setContent {
            AppLockerTheme {

                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()


                val showAccessibilityPermissionDialog = remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    val isEnabled = isAccessibilityServiceEnabled(
                        this@MainActivity,
                        AppLockAccessibilityService::class.java
                    )
                    if (!isEnabled) {
                        showAccessibilityPermissionDialog.value = true
                    }
                }

                AccessibilityPromptDialog(
                    showDialog = showAccessibilityPermissionDialog.value,
                    onDismiss = { showAccessibilityPermissionDialog.value = false },
                    onOpenSettings = {
                        showAccessibilityPermissionDialog.value = false
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        this@MainActivity.startActivity(intent)
                    }
                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        DrawerContent(){onDestinationClicked ->
                            scope.launch { drawerState.close() }
                        }
                    }
                ) {
                    AppListScreen(
                        openDrawer = {
                            scope.launch { drawerState.open() }
                        }
                    )
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

//        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        //        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
//        Log.d("QUERY_ALL_PACKAGES  -> Packages", "count: ${packages.size}")

        //QUERY FILTERED APPS LAUNCHABLE INTENTS
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }

//        packages.forEach { pkg ->
//            Log.d("QUERY_ALL_PACKAGES  -> Package", "pkg_NAME: ${pkg.packageName}, app_Label: ${pkg.applicationInfo?.loadLabel(packageManager).toString()}, app_Name${pkg.applicationInfo?.name}")
//        }
//        return packages.map { pkg ->
//            val appName = pkg.applicationInfo?.loadLabel(packageManager).toString()
//            val packageName = pkg.applicationInfo?.packageName?:"N/A"
//            val icon = pkg.applicationInfo?.loadIcon(packageManager)
////            Log.d("QUERY_ALL_PACKAGES -> Apps  ", "App Name: $appName, Package Name: $packageName, Category:  ${pkg.category}")
//            AppInfo(packageName = packageName, name = appName, icon = icon)
//        }

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
