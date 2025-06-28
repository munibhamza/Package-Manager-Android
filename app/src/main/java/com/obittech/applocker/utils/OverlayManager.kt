package com.obittech.applocker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import com.obittech.applocker.ui.screens.lockScreen.LockPinOverlayView

@SuppressLint("StaticFieldLeak")
object OverlayManager {
    private var overlayView: View? = null
    private var windowManager: WindowManager? = null
    @SuppressLint("ObsoleteSdkInt")
    fun showOverlay(context: Context, onSuccess: () -> Unit) {
        if (overlayView != null) return
        if (!Settings.canDrawOverlays(context)) return

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.CENTER

        val composeView = ComposeView(context).apply {
            setContent {
                LockPinOverlayView(
                    correctPin = "1234",
                    onPinSuccess = {
                        hideOverlay()
                        onSuccess()
                    }
                )
            }
        }

        overlayView = composeView
        windowManager?.addView(composeView, params)
    }

    fun hideOverlay() {
        overlayView?.let {
            windowManager?.removeViewImmediate(it)
            overlayView = null
        }
    }

    fun isOverlayVisible(): Boolean {
        return overlayView != null
    }
}