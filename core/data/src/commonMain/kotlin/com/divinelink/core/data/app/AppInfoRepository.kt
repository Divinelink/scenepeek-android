package com.divinelink.core.data.app

interface AppInfoRepository {

  suspend fun fetchLatestAppVersion(): Result<String?>
}
