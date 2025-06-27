package com.obittech.applocker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.obittech.applocker.data.source.LockedAppDao
import com.obittech.applocker.data.source.LockedAppEntity

@Database(entities = [LockedAppEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun lockedAppDao(): LockedAppDao

}