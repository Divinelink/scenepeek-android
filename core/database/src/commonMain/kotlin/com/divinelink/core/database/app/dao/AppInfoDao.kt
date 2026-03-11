package com.divinelink.core.database.app.dao

import com.divinelink.core.database.AppVersionEntity
import kotlinx.coroutines.flow.Flow

interface AppInfoDao {

  fun fetchAppVersion(): Flow<AppVersionEntity?>

  fun updateAppVersion(
    currentVersion: String,
    latestVersion: String,
    currentEpochSeconds: String,
  )
}
