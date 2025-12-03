package com.divinelink.core.network.client

import com.divinelink.core.commons.provider.BuildConfigProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OMDbClient(
  engine: HttpClientEngine,
  config: BuildConfigProvider,
) {

  val client: HttpClient = ktorClient(engine, config).config {
    defaultRequest {
      contentType(ContentType.Application.Json)
    }
  }
}
