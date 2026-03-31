package com.divinelink.core.data.app

import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {

  val updateAvailable: Flow<AppVersion?>
  val updaterOptIn: Flow<Boolean>

  fun fetchLatestAppVersion(
    fetchRemote: Boolean,
    force: Boolean = false,
  ): Flow<Resource<AppVersion?>>

  suspend fun updateUpdaterOptIn(enabled: Boolean)
}
