package com.divinelink.core.network.client

import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.commons.provider.SecretProvider
import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.accessToken
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.bearerAuth
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthTMDbClient(
  engine: HttpClientEngine,
  encryptedStorage: SavedStateStorage,
  secret: SecretProvider,
  config: BuildConfigProvider,
) {
  val tmdbUrl = secret.tmdbUrl

  val client: HttpClient = ktorClient(engine, config).config {
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

  suspend inline fun <reified T : Any, reified V : Any> put(
    url: String,
    body: T,
  ): V = client.put(url, body)

  fun close() {
    client.close()
  }
}
