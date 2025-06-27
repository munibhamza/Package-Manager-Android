package com.obittech.applocker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.prefs.Preferences
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object SettingsDataStoreModule {
//    @Provides
//    @Singleton
//    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
//        return PreferenceDataStoreFactory.create(
//            produceFile = { context.preferencesDataStoreFile("app_settings") }
//        )
//    }
//}