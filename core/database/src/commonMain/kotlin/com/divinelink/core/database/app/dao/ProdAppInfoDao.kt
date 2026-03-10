package com.divinelink.core.database.app.dao

import com.divinelink.core.database.AppVersionEntity
import com.divinelink.core.database.Database

class ProdAppInfoDao(
  private val database: Database,
) : AppInfoDao {

  override fun fetchAppVersion(): AppVersionEntity? = database
    .appVersionEntityQueries
    .fetchAppVersion()
    .executeAsOneOrNull()

  override fun updateAppVersion(
    currentVersion: String,
    latestVersion: String,
    currentEpochSeconds: String,
  ) = database.transaction {
    database.appVersionEntityQueries.updateAppVersion(
      currentVersion = currentVersion,
      latestVersion = latestVersion,
      lastCheck = currentEpochSeconds,
    )
  }
}
