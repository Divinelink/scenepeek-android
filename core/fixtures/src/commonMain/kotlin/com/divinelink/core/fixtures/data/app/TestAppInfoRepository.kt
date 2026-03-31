package com.divinelink.core.fixtures.data.app

import com.divinelink.core.data.app.AppInfoRepository
import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class TestAppInfoRepository : AppInfoRepository {
  override val updateAvailable: Flow<AppVersion?>
    get() = flowOf(null)

  override val updaterOptIn: Flow<Boolean>
    get() = flowOf(true)

  override fun fetchLatestAppVersion(
    fetchRemote: Boolean,
    force: Boolean,
  ): Flow<Resource<AppVersion?>> = flowOf(Resource.Loading(null))

  override suspend fun updateUpdaterOptIn(enabled: Boolean) {
    // Implement when needed
  }
}
