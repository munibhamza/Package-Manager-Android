package com.obittech.applocker.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.material3.MaterialTheme
import com.obittech.applocker.datastore.AppPreferencesManager
import com.obittech.applocker.ui.screens.screenoverlay.OverlayPinDialog
import com.obittech.applocker.utils.IntentKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : Service() {

    @Inject lateinit var preferences: AppPreferencesManager
    private lateinit var windowManager: WindowManager
    private var composeView: ComposeView? = null
    private var targetPackage: String? = null
    private var expPIN: String? = null


    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        targetPackage = intent?.getStringExtra(IntentKeys.TARGET_PACKAGE)
        expPIN = intent?.getStringExtra(IntentKeys.TARGET_PIN)
        showPinOverlay()
        return START_STICKY
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showPinOverlay(){
        @Suppress("DEPRECATION") val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        val composePinView = ComposeView(this).apply {
            setContent {
                MaterialTheme {

                    OverlayPinDialog(
                        onSubmit = { enteredPin ->
                            if (enteredPin == expPIN) {
                                CoroutineScope(Dispatchers.IO).launch{
                                    preferences.setLastUnlockTime(System.currentTimeMillis())
                                }

                                stopSelf()
                            } else {
                                Toast.makeText(context, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onCancel = { stopSelf() }
                    )
                }
            }
        }

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        composeView = composePinView
        windowManager.addView(composeView, params)
    }

    private fun removePinOverlay(){
        if (::windowManager.isInitialized){
            composeView?.let {
                windowManager.removeView(it)
                composeView = null
            }
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        removePinOverlay()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
