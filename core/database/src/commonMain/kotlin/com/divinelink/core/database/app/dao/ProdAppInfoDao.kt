package com.divinelink.core.database.app.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.extensions.epochSecondsToLocalDateTime
import com.divinelink.core.database.AppVersionEntity
import com.divinelink.core.database.Database
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class ProdAppInfoDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
) : AppInfoDao {
  private val backupQueries = database.backupEntityQueries

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

  override fun dismissMessage(uuid: String) {
    database.configMessagesEntityQueries.dismissMessage(uuid)
  }

  override fun isDismissed(uuid: String): Flow<Boolean> = database
    .configMessagesEntityQueries
    .isDismisssed(uuid)
    .asFlow()
    .mapToOneOrDefault(context = dispatcher.io, defaultValue = false)
    .distinctUntilChanged()

  override fun getBackupState(): Flow<Pair<LocalDateTime?, Boolean>> = backupQueries
    .fetchBackupState()
    .asFlow()
    .mapToOneOrNull(dispatcher.io)
    .map { entity ->
      val lastBackup = entity?.lastBackupAt?.epochSecondsToLocalDateTime()
      val hasError = entity?.error != null

      lastBackup to hasError
    }
    .distinctUntilChanged()

  override fun setLastBackupAt(currentEpochSeconds: String) = database.transaction {
    backupQueries.updateLastBackupAt(currentEpochSeconds)
  }

  override fun setBackupError() = database.transaction {
    backupQueries.setError()
  }
}
