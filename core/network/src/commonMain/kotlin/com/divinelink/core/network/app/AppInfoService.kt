package com.divinelink.core.network.app

interface AppInfoService {
  suspend fun fetchLatestAppVersion(url: String): Result<String>
}
