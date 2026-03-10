package com.divinelink.core.data.app

import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {

  val updateAvailable: Flow<Boolean>

  fun fetchLatestAppVersion(): Flow<Resource<AppVersion?>>
}
