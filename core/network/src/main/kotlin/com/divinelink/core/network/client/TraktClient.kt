package com.divinelink.core.network.client

import com.divinelink.core.commons.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktClient(engine: HttpClientEngine) {

  val client: HttpClient = ktorClient(engine).config {
    defaultRequest {
      contentType(ContentType.Application.Json)
      headers {
        set("trakt-api-key", BuildConfig.TRAKT_API_KEY)
        set("trakt-api-version", "2")
      }
    }
  }
}
