package com.obittech.applocker.datastore.di

import com.obittech.applocker.datastore.AppPreferencesManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppPreferencesEntryPoint {
    fun preferencesManager(): AppPreferencesManager
}