package com.divinelink.core.network.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OMDbClient(engine: HttpClientEngine) {

  val client: HttpClient = ktorClient(engine).config {
    defaultRequest {
      contentType(ContentType.Application.Json)
    }
  }
}
