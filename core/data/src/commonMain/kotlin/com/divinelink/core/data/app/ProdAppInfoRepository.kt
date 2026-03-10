package com.divinelink.core.data.app

import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.network.app.AppInfoService

class ProdAppInfoRepository(
  private val service: AppInfoService,
  private val buildConfigProvider: BuildConfigProvider,
) : AppInfoRepository {

  override suspend fun fetchLatestAppVersion(): Result<String?> {
    return buildConfigProvider.versionCheckerUrl?.let { url ->
      service
        .fetchLatestAppVersion(url = url)
        .map { response ->
          val regex = Regex("""\[\["(\d+\.\d+[\d.]*)"]]""")
          regex.find(response)?.groupValues?.lastOrNull()
        }
    } ?: Result.success(null)
  }
}
