package com.divinelink.core.network.client

import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.exception.JellyseerrInvalidCredentials
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
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import kotlinx.coroutines.flow.first

class JellyseerrRestClient(
  engine: HttpClientEngine,
  private val encryptedStorage: EncryptedStorage,
  private val datastore: PreferenceStorage,
) {

  companion object {
    const val AUTH_ENDPOINT = "/api/v1/auth/"
  }

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
          val isAuthEndpoint = response.request.url.isAuthEndpoint()

          when {
            response.status == HttpStatusCode.Unauthorized && !isAuthEndpoint -> true
            else -> false
          }
        }
      }
    }
    .apply {
      receivePipeline.intercept(HttpReceivePipeline.Before) {
        val isAuthEndpoint = subject.request.url.isAuthEndpoint()

        if (subject.status == HttpStatusCode.Unauthorized && !isAuthEndpoint) {
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
      throw JellyseerrInvalidCredentials()
    }

    val loginMethod = JellyseerrLoginMethod.from(signInMethod)
      ?: throw JellyseerrInvalidCredentials()

    val body = loginMethod.toRequestBodyApi(account, password)

    post<JellyseerrLoginRequestBodyApi, JellyfinLoginResponseApi>(
      url = "$address$AUTH_ENDPOINT${loginMethod.endpoint}",
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

  private fun Url.isAuthEndpoint(): Boolean = encodedPath.contains(AUTH_ENDPOINT)
}
