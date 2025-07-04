package com.divinelink.core.network.client

import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthTMDbClient(
  engine: HttpClientEngine,
  encryptedStorage: EncryptedStorage,
) {
  val tmdbUrl = BuildConfig.TMDB_BASE_URL

  val client: HttpClient = ktorClient(engine).config {
    defaultRequest {
      contentType(ContentType.Application.Json)
      encryptedStorage.accessToken?.let { accessToken ->
        bearerAuth(accessToken)
      }
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

  suspend fun put(
    url: String,
    body: String,
  ): HttpResponse = client.put(url, body)

  fun close() {
    client.close()
  }
}
