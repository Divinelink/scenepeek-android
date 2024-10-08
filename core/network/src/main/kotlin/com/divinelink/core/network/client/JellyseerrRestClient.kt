package com.divinelink.core.network.client

import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.exception.JellyseerrUnauthorizedException
import com.divinelink.core.model.jellyseerr.JellyseerrLoginMethod
import com.divinelink.core.network.jellyseerr.model.JellyfinLoginResponseApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrLoginRequestBodyApi
import com.divinelink.core.network.jellyseerr.model.toRequestBodyApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first

class JellyseerrRestClient(
  engine: HttpClientEngine,
  private val encryptedStorage: EncryptedStorage,
  private val datastore: PreferenceStorage,
) {

  val client: HttpClient = ktorClient(engine)
    .config {
      install(HttpCookies) {
        storage = PersistentCookieStorage(encryptedStorage)
      }

      HttpResponseValidator {
        validateResponse { response ->
          if (response.status == HttpStatusCode.Unauthorized) {
            throw JellyseerrUnauthorizedException()
          }
        }
      }

      install(HttpRequestRetry) {
        retryIf(maxRetries = 1) { _, response ->
          when {
            response.status != HttpStatusCode.Unauthorized -> false
            else -> true
          }
        }
      }
    }
    .apply {
      receivePipeline.intercept(HttpReceivePipeline.Before) {
        if (subject.status == HttpStatusCode.Unauthorized) {
          reAuthenticate()
        }
      }
    }

  private suspend fun reAuthenticate() {
    val account = datastore.jellyseerrAccount.first()
    val password = encryptedStorage.jellyseerrPassword
    val address = datastore.jellyseerrAddress.first()
    val signInMethod = datastore.jellyseerrSignInMethod.first()

    if (account == null || password == null || address == null || signInMethod == null) {
      throw JellyseerrUnauthorizedException()
    }

    val loginMethod = JellyseerrLoginMethod.from(signInMethod)
      ?: throw JellyseerrUnauthorizedException()

    val body = loginMethod.toRequestBodyApi(account, password)

    post<JellyseerrLoginRequestBodyApi, JellyfinLoginResponseApi>(
      url = "$address/api/v1/auth/${loginMethod.endpoint}",
      body = body,
    )
  }

  suspend inline fun <reified T : Any> get(url: String): T = client.get(url)

  suspend inline fun <reified T : Any, reified V : Any> post(
    url: String,
    body: T,
  ): V = client.post(url, body)

  fun close() {
    client.close()
  }
}
