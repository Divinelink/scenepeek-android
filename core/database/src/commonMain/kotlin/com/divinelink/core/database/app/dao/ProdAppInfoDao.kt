package com.divinelink.core.database.app.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.AppVersionEntity
import com.divinelink.core.database.Database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

class ProdAppInfoDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
) : AppInfoDao {

  override fun fetchAppVersion(): Flow<AppVersionEntity?> = database
    .appVersionEntityQueries
    .fetchAppVersion()
    .asFlow()
    .mapToOneOrNull(
      context = dispatcher.io,
    )
    .distinctUntilChanged()

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
