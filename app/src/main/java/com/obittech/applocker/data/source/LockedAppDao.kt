package com.obittech.applocker.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LockedAppDao {
//    @Query("SELECT * FROM locked_apps WHERE packageName = :pkg")
//    suspend fun getLockedApp(pkg: String): LockedAppEntity?

    @Query("SELECT * FROM locked_apps")
    suspend fun getAllLockedApps(): List<LockedAppEntity>

    @Query("SELECT * FROM locked_apps WHERE packageName = :pkg LIMIT 1")
    suspend fun getLockedAppByPackage(pkg: String): LockedAppEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun lockApp(app: LockedAppEntity)

    @Delete
    suspend fun unlockApp(app: LockedAppEntity)

    @Query("DELETE FROM locked_apps WHERE packageName = :packageName")
    suspend fun deleteByPackageName(packageName: String)
}