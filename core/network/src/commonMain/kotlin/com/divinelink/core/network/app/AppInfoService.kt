package com.divinelink.core.network.app

import com.divinelink.core.network.app.model.ConfigResponse

interface AppInfoService {
  suspend fun fetchLatestAppVersion(url: String): Result<String>
  suspend fun fetchConfig(): Result<ConfigResponse>
}
