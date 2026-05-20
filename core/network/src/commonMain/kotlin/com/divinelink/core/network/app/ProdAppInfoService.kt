package com.divinelink.core.network.app

import com.divinelink.core.network.app.model.ConfigResponse
import com.divinelink.core.network.client.RestClient
import com.divinelink.core.network.client.get
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class ProdAppInfoService(
  val client: HttpClient,
  val restClient: RestClient,
) : AppInfoService {

  override suspend fun fetchLatestAppVersion(url: String): Result<String> = runCatching {
    client.get(urlString = url).bodyAsText()
  }

  override suspend fun fetchConfig(): Result<ConfigResponse> = runCatching {
    restClient.client.get<ConfigResponse>(
      "https://codeberg.org/Divinelink/scenepeek-config/raw/branch/main/config.json",
    )
  }
}
