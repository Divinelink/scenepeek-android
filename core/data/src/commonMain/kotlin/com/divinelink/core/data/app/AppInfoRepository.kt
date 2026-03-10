package com.divinelink.core.data.app

import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow

interface AppInfoRepository {

  val updateAvailable: Flow<AppVersion?>

  fun fetchLatestAppVersion(fetchRemote: Boolean): Flow<Resource<AppVersion?>>
}
