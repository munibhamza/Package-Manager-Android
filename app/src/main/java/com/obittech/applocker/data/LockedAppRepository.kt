package com.obittech.applocker.data

import com.obittech.applocker.data.source.LockedAppDao
import com.obittech.applocker.data.source.LockedAppEntity
import javax.inject.Inject


class LockedAppRepository @Inject constructor(
    private val lockedAppDao: LockedAppDao
)  {

    suspend fun getAllLockedApps(): List<LockedAppEntity> {
        return lockedAppDao.getAllLockedApps()
    }

    suspend fun getLockedApp(pkg:String): LockedAppEntity?{
        return lockedAppDao.getLockedAppByPackage(pkg)
    }

    suspend fun lockApp(app: LockedAppEntity){
        lockedAppDao.lockApp(app)
    }

    suspend fun unlockApp(app: LockedAppEntity){
        lockedAppDao.unlockApp(app)
    }

    suspend fun deleteByPackageName(packageName: String) {
        lockedAppDao.deleteByPackageName(packageName)
    }

}