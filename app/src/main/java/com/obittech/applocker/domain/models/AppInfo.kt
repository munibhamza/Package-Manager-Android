package com.obittech.applocker.domain.models

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val name: String,
    val icon: Drawable?,
    val isLocked: Boolean = false
)