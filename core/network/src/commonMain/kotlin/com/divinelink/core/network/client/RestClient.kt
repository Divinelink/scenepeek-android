package com.divinelink.core.network.client

import com.divinelink.core.commons.provider.BuildConfigProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine

class RestClient(
  engine: HttpClientEngine,
  config: BuildConfigProvider,
) {
  val client: HttpClient = ktorClient(engine, config)
}
