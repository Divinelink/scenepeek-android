package com.divinelink.core.fixtures.data.app

import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestAppInfoRepository : AppInfoRepository {
  override val updateAvailable: Flow<AppVersion?>
    get() = flowOf(null)

  override fun fetchLatestAppVersion(fetchRemote: Boolean): Flow<Resource<AppVersion?>> {
    return flowOf(Resource.Loading(null))
  }
}
