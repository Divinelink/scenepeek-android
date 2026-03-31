package com.divinelink.core.data.app

import com.divinelink.core.commons.extensions.isNewerThan
import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.database.app.dao.AppInfoDao
import com.divinelink.core.database.app.mapper.map
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.app.AppVersion
import com.divinelink.core.network.Resource
import com.divinelink.core.network.app.AppInfoService
import com.divinelink.core.network.networkBoundResource
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class ProdAppInfoRepository(
  private val service: AppInfoService,
  private val dao: AppInfoDao,
  private val buildConfigProvider: BuildConfigProvider,
  private val clock: Clock,
  private val installSourceDetector: InstallSourceDetector,
  private val preferenceStorage: PreferenceStorage,
) : AppInfoRepository {

  private val _updateAvailable: MutableStateFlow<AppVersion?> = MutableStateFlow(null)

  override val updaterOptIn: Flow<Boolean>
    get() = preferenceStorage.updaterOptIn

  override fun fetchLatestAppVersion(
    fetchRemote: Boolean,
    force: Boolean,
  ): Flow<Resource<AppVersion?>> {
    val installSource = installSourceDetector.getInstallSource()

    return networkBoundResource(
      query = {
        dao
          .fetchAppVersion()
          .map { entity ->
            entity?.map(
              clock = clock,
              defaultVersion = buildConfigProvider.versionName,
              installSource = installSource,
            )
          }
      },
      fetch = {
        Napier.d { "fetching version: that's right!" }
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
        val latestVersion = it?.getOrNull()?.trimStart { char -> char == 'v' }

        latestVersion?.let {
          dao.updateAppVersion(
            currentVersion = buildConfigProvider.versionName,
            latestVersion = latestVersion,
            currentEpochSeconds = clock.currentEpochSeconds(),
          )

          val appVersion = AppVersion(
            currentVersion = buildConfigProvider.versionName,
            latestVersion = latestVersion,
            installSource = installSource,
            lastCheck = clock.currentEpochSeconds(),
            canSearchForUpdate = false,
          )

          if (latestVersion.isNewerThan(appVersion.currentVersion)) {
            _updateAvailable.emit(appVersion)
          }
        }
      },
      shouldFetch = {
        force || ((it?.canSearchForUpdate == null || it.canSearchForUpdate) && fetchRemote)
      },
    )
  }

  override val updateAvailable: Flow<AppVersion?> = _updateAvailable

  override suspend fun updateUpdaterOptIn(enabled: Boolean) {
    preferenceStorage.setUpdaterOptIn(enabled)
  }
}
