package com.divinelink.core.data.app

import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.database.app.dao.AppInfoDao
import com.divinelink.core.database.app.mapper.map
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.network.Resource
import com.divinelink.core.network.app.AppInfoService
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.time.Clock

class ProdAppInfoRepository(
  private val service: AppInfoService,
  private val dao: AppInfoDao,
  private val buildConfigProvider: BuildConfigProvider,
  private val clock: Clock,
) : AppInfoRepository {

  private val _updateAvailable: MutableStateFlow<Boolean> = MutableStateFlow(false)

  override fun fetchLatestAppVersion(): Flow<Resource<AppVersion?>> = networkBoundResource(
    query = {
      flowOf(
        dao.fetchAppVersion()?.map(
          clock = clock,
          defaultVersion = buildConfigProvider.versionName,
        ),
      )
    },
    fetch = {
      buildConfigProvider.versionCheckerUrl?.let { url ->
        service
          .fetchLatestAppVersion(url = url)
          .map { response ->
            val regex = Regex("""\[\["(\d+\.\d+[\d.]*)"]]""")
            regex.find(response)?.groupValues?.lastOrNull()
          }
      } ?: Result.success(null)
    },
    saveFetchResult = {
      val latestVersion = it.getOrNull()

      latestVersion?.let {
        dao.updateAppVersion(
          currentVersion = buildConfigProvider.versionName,
          latestVersion = latestVersion,
          currentEpochSeconds = clock.currentEpochSeconds(),
        )

        _updateAvailable.emit(
          buildConfigProvider.versionName < latestVersion,
        )
      }
    },
    shouldFetch = { it?.canSearchForUpdate == null || it.canSearchForUpdate },
  )

  override val updateAvailable: Flow<Boolean> = _updateAvailable
}
