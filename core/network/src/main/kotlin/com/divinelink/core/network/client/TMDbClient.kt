package com.divinelink.core.network.client

import com.divinelink.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TMDbClient(engine: HttpClientEngine) {
  val tmdbUrl = BuildConfig.TMDB_BASE_URL
  private val authToken = BuildConfig.TMDB_AUTH_TOKEN

  val client: HttpClient = ktorClient(engine).config {
    defaultRequest {
      contentType(ContentType.Application.Json)
      bearerAuth(authToken)
    }
  }

  suspend inline fun <reified T : Any> get(url: String): T = client.get(url)

  suspend inline fun <reified T : Any, reified V : Any> post(
    url: String,
    body: T,
  ): V = client.post(url, body)

  suspend inline fun <reified T : Any, reified V : Any> delete(
    url: String,
    body: T,
  ): V = client.delete(url, body)

  suspend inline fun <reified T : Any> delete(url: String): T = client.delete(url)

  suspend inline fun <reified T : Any, reified V : Any> put(
    url: String,
    body: T,
  ): V = client.put(url, body)

  fun close() {
    client.close()
  }
}
