package com.divinelink.core.data.app

import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.model.config.ConfigMessage
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {

  val updateAvailable: Flow<AppVersion?>
  val updaterOptIn: Flow<Boolean>
  val announcement: Flow<ConfigMessage?>

  fun fetchLatestAppVersion(
    fetchRemote: Boolean,
    force: Boolean = false,
  ): Flow<Resource<AppVersion?>>

  suspend fun fetchRemoteConfig(): Result<Unit>

  suspend fun dismissAnnouncement(uuid: String)

  suspend fun updateUpdaterOptIn(enabled: Boolean)
}
