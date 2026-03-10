package com.divinelink.core.network.app

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class ProdAppInfoService(
  val client: HttpClient,
) : AppInfoService {

  override suspend fun fetchLatestAppVersion(url: String): Result<String> = runCatching {
    client.get(urlString = url).bodyAsText()
  }
}
