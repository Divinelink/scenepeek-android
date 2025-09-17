package com.divinelink.core.network.client

import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.selectedJellyseerrCredentials
import com.divinelink.core.datastore.auth.selectedJellyseerrHostAddress
import com.divinelink.core.model.exception.AppException
import com.divinelink.core.network.jellyseerr.model.JellyseerrLoginRequestBodyApi
import com.divinelink.core.network.jellyseerr.model.toRequestBodyApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url

class JellyseerrRestClient(
  engine: HttpClientEngine,
  private val savedStateStorage: SavedStateStorage,
) {

  companion object {
    const val AUTH_ENDPOINT = "/api/v1/auth"
  }

  val hostAddress: String?
    get() = savedStateStorage.selectedJellyseerrHostAddress

  val client: HttpClient = ktorClient(engine)
    .config {
      install(HttpCookies) {
        storage = PersistentCookieStorage(storage = savedStateStorage)
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
    val account = savedStateStorage.selectedJellyseerrCredentials
      ?: throw AppException.Unauthorized("Invalid jellyseerr authentication. Please log in again.")

    val body = account.authMethod.toRequestBodyApi(
      username = account.account,
      password = account.password,
    )

    post<JellyseerrLoginRequestBodyApi, Unit>(
      url = "${account.address}$AUTH_ENDPOINT/${account.authMethod.endpoint}",
      body = body,
    )
  }

  suspend inline fun <reified T : Any> get(url: String): T = client.get(url)

  suspend inline fun <reified T : Any, reified V : Any> post(
    url: String,
    body: T,
  ): V = client.post(url, body)

  suspend inline fun <reified T : Any> delete(url: String) = client.delete<T>(url)

  fun close() {
    client.close()
  }

  private fun Url.isAuthEndpoint(): Boolean = encodedPath.contains(AUTH_ENDPOINT)
}
