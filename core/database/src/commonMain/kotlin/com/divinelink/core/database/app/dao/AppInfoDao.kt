package com.divinelink.core.database.app.dao

import com.divinelink.core.database.AppVersionEntity

interface AppInfoDao {

  fun fetchAppVersion(): AppVersionEntity?

  fun updateAppVersion(
    currentVersion: String,
    latestVersion: String,
    currentEpochSeconds: String,
  )
}
