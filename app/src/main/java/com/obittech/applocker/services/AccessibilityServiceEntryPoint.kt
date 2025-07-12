package com.obittech.applocker.services

import com.obittech.applocker.data.LockedAppRepository
import com.obittech.applocker.datastore.AppPreferencesManager
import com.obittech.applocker.utils.unlockmanager.UnlockSessionManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AccessibilityServiceEntryPoint {
    fun unlockManager(): UnlockSessionManager
    fun lockedAppRepository(): LockedAppRepository
    fun preferencesManager(): AppPreferencesManager
}