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
  private val installSourceDetector: InstallSourceDetector,
) : AppInfoRepository {

  private val _updateAvailable: MutableStateFlow<AppVersion?> = MutableStateFlow(null)

  override fun fetchLatestAppVersion(fetchRemote: Boolean): Flow<Resource<AppVersion?>> {
    val installSource = installSourceDetector.getInstallSource()

    return networkBoundResource(
      query = {
        flowOf(
          dao.fetchAppVersion()?.map(
            clock = clock,
            defaultVersion = buildConfigProvider.versionName,
            installSource = installSource,
          ),
        )
      },
      fetch = {
        installSource.versionCheckUrl?.let { versionCheckUrl ->
          service
            .fetchLatestAppVersion(url = versionCheckUrl)
            .map { response ->
              val regex = Regex(installSource.versionRegex)
              regex.find(response)?.groupValues?.lastOrNull()
            }
        }
      },
      saveFetchResult = {
        val latestVersion = it?.getOrNull()

        latestVersion?.let {
          dao.updateAppVersion(
            currentVersion = buildConfigProvider.versionName,
            latestVersion = latestVersion.trimStart { char -> char == 'v' },
            currentEpochSeconds = clock.currentEpochSeconds(),
          )

          val appVersion = AppVersion(
            currentVersion = buildConfigProvider.versionName,
            latestVersion = latestVersion,
            installSource = installSource,
            canSearchForUpdate = false,
          )

          _updateAvailable.emit(appVersion)
        }
      },
      shouldFetch = {
        (it?.canSearchForUpdate == null || it.canSearchForUpdate) && fetchRemote
      },
    )
  }

  override val updateAvailable: Flow<AppVersion?> = _updateAvailable
}
