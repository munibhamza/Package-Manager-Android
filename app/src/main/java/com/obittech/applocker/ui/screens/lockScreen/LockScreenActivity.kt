package com.obittech.applocker.ui.screens.lockScreen

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.obittech.applocker.data.LockedAppRepository
import com.obittech.applocker.security.PasswordHasher
import com.obittech.applocker.ui.theme.AppLockerTheme
import com.obittech.applocker.utils.IntentKeys
import com.obittech.applocker.utils.unlockmanager.UnlockSessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockScreenActivity : ComponentActivity() {

    @Inject lateinit var repository: LockedAppRepository
    @Inject lateinit var unlockManager: UnlockSessionManager
    private var targetPackageName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        targetPackageName = intent.getStringExtra(IntentKeys.TARGET_PACKAGE) ?: run {
            finish()
            return
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.let {
                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        setFinishOnTouchOutside(false)

        setContent{
            AppLockerTheme {
                LockScreenUI(
                    onPinSubmit = { enteredPin ->
                        handlePinVerification(enteredPin)
                    },
                    onCancelled = {
//                    val activityManager =
//                        getSystemService(ACTIVITY_SERVICE) as ActivityManager
//                    activityManager.killBackgroundProcesses(targetPackageName)
                        finishAffinity()
                    }
                )
            }

        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(enabled = true){
            override fun handleOnBackPressed() {
                //Do nothing
            }
        })
    }



    private fun handlePinVerification(enteredPin: String) {
        lifecycleScope.launch {
            val lockedApp = repository.getLockedApp(targetPackageName)

            if (lockedApp != null && PasswordHasher.verifyPin(enteredPin, lockedApp.pin)) {
                unlockManager.unlockApp(targetPackageName)
                val launchIntent = packageManager.getLaunchIntentForPackage(targetPackageName)
                startActivity(launchIntent)
                finishAffinity()
            } else {
                Toast.makeText(this@LockScreenActivity, "Incorrect PIN", Toast.LENGTH_SHORT).show()
            }
        }
    }
}