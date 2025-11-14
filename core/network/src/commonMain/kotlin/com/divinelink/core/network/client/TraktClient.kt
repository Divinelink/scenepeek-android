package com.divinelink.core.network.client

import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.commons.provider.SecretProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktClient(
  engine: HttpClientEngine,
  secret: SecretProvider,
  config: BuildConfigProvider,
) {

  val client: HttpClient = ktorClient(engine, config).config {
    defaultRequest {
      contentType(ContentType.Application.Json)
      headers {
        set("trakt-api-key", secret.traktApiKey)
        set("trakt-api-version", "2")
      }
    }
  }
}
