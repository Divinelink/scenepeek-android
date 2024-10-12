package com.divinelink.core.network.client

import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.datastore.EncryptedStorage
import com.divinelink.core.datastore.PreferenceStorage
import com.divinelink.core.model.exception.JellyseerrInvalidCredentials
import com.divinelink.core.model.jellyseerr.JellyseerrAuthMethod
import com.divinelink.core.network.jellyseerr.model.JellyseerrRequestMediaBodyApi
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi
import com.divinelink.core.testing.factories.api.jellyseerr.JellyseerrRequestMediaBodyApiFactory
import com.divinelink.core.testing.storage.FakeEncryptedPreferenceStorage
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.google.common.truth.Truth.assertThat
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class JellyseerrRestClientTest {

  private lateinit var client: JellyseerrRestClient
  private lateinit var encryptedStorage: EncryptedStorage
  private lateinit var datastore: PreferenceStorage
  private lateinit var engine: HttpClientEngine

  private val address = "http://localhost:8080"

  @Test
  fun `test unauthorized request triggers reAuthentication`() = runTest {
    val requestHistory = mutableListOf<Pair<String, HttpStatusCode>>()

    val expectedRequests = listOf(
      "http://localhost:8080/api/v1/request" to HttpStatusCode.Unauthorized,
      "http://localhost:8080/api/v1/auth/local" to HttpStatusCode.OK,
      "http://localhost:8080/api/v1/request" to HttpStatusCode.OK,
    )

    var requestCount = 0

    engine = MockEngine { request ->
      when (request.method) {
        HttpMethod.Get -> respond(
          content = "Failed to authenticate",
          status = HttpStatusCode.Unauthorized,
        )
        HttpMethod.Post -> {
          val response = if (requestCount == 0) {
            respond(
              content = "Failed to authenticate",
              status = HttpStatusCode.Unauthorized,
            )
          } else {
            respond(
              content = """{"success": true}""",
              status = HttpStatusCode.OK,
            )
          }
          requestCount++
          response.also { requestHistory.add(request.url.toString() to response.statusCode) }
        }
        else -> error("Unexpected request method: ${request.method}")
      }
    }

    encryptedStorage = FakeEncryptedPreferenceStorage(
      jellyseerrPassword = "testPassword",
    )
    datastore = FakePreferenceStorage(
      jellyseerrAccount = "testAccount",
      jellyseerrAddress = address,
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    client = JellyseerrRestClient(
      engine = engine,
      encryptedStorage = encryptedStorage,
      datastore = datastore,
    )

    // Initial request
    client.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
      url = "http://localhost:8080/api/v1/request",
      body = JellyseerrRequestMediaBodyApiFactory.movie(),
    )

    assertThat(requestHistory.size).isEqualTo(3)
    assertThat(requestHistory).isEqualTo(expectedRequests)
  }

  @Test
  fun `test reAuthentication with invalidCredentials throws UnauthorizedException`() = runTest {
    val requestHistory = mutableListOf<Pair<String, HttpStatusCode>>()

    val expectedRequests = listOf(
      "http://localhost:8080/api/v1/request" to HttpStatusCode.Unauthorized,
      "http://localhost:8080/api/v1/auth/local" to HttpStatusCode.Unauthorized,
      "http://localhost:8080/api/v1/request" to HttpStatusCode.Unauthorized,
      "http://localhost:8080/api/v1/auth/local" to HttpStatusCode.Unauthorized,
    )

    engine = MockEngine { request ->
      when (request.method) {
        HttpMethod.Get -> respond(
          content = "Failed to authenticate",
          status = HttpStatusCode.Unauthorized,
        )
        HttpMethod.Post -> {
          respond(
            content = "Failed to authenticate",
            status = HttpStatusCode.Unauthorized,
          ).also {
            requestHistory.add(request.url.toString() to it.statusCode)
          }
        }
        else -> error("Unexpected request method: ${request.method}")
      }
    }

    encryptedStorage = FakeEncryptedPreferenceStorage(
      jellyseerrPassword = "testPassword",
    )
    datastore = FakePreferenceStorage(
      jellyseerrAccount = "testAccount",
      jellyseerrAddress = address,
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    client = JellyseerrRestClient(
      engine = engine,
      encryptedStorage = encryptedStorage,
      datastore = datastore,
    )

    assertFailsWith<InvalidStatusException> {
      client.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
        url = "http://localhost:8080/api/v1/request",
        body = JellyseerrRequestMediaBodyApiFactory.movie(),
      )
    }

    assertThat(requestHistory.size).isEqualTo(4)
    assertThat(requestHistory).isEqualTo(expectedRequests)
  }

  @Test
  fun `test reAuthentication without password throws InvalidCredentials`() = runTest {
    engine = MockEngine {
      respond(
        content = "Failed to authenticate",
        status = HttpStatusCode.Unauthorized,
      )
    }

    encryptedStorage = FakeEncryptedPreferenceStorage(
      jellyseerrPassword = null,
    )

    datastore = FakePreferenceStorage(
      jellyseerrAccount = "testAccount",
      jellyseerrAddress = address,
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    client = JellyseerrRestClient(
      engine = engine,
      encryptedStorage = encryptedStorage,
      datastore = datastore,
    )

    assertFailsWith<JellyseerrInvalidCredentials> {
      client.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
        url = "http://localhost:8080/api/v1/request",
        body = JellyseerrRequestMediaBodyApiFactory.movie(),
      )
    }
  }

  @Test
  fun `test reAuthentication without account throws InvalidCredentials`() = runTest {
    engine = MockEngine {
      respond(
        content = "Failed to authenticate",
        status = HttpStatusCode.Unauthorized,
      )
    }

    encryptedStorage = FakeEncryptedPreferenceStorage(
      jellyseerrPassword = "testPassword",
    )

    datastore = FakePreferenceStorage(
      jellyseerrAccount = null,
      jellyseerrAddress = address,
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    client = JellyseerrRestClient(
      engine = engine,
      encryptedStorage = encryptedStorage,
      datastore = datastore,
    )

    assertFailsWith<JellyseerrInvalidCredentials> {
      client.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
        url = "http://localhost:8080/api/v1/request",
        body = JellyseerrRequestMediaBodyApiFactory.movie(),
      )
    }
  }

  @Test
  fun `test reAuthentication without address throws InvalidCredentials`() = runTest {
    engine = MockEngine {
      respond(
        content = "Failed to authenticate",
        status = HttpStatusCode.Unauthorized,
      )
    }

    encryptedStorage = FakeEncryptedPreferenceStorage(
      jellyseerrPassword = "testPassword",
    )

    datastore = FakePreferenceStorage(
      jellyseerrAccount = "testAccount",
      jellyseerrAddress = null,
      jellyseerrSignInMethod = JellyseerrAuthMethod.JELLYSEERR.name,
    )

    client = JellyseerrRestClient(
      engine = engine,
      encryptedStorage = encryptedStorage,
      datastore = datastore,
    )

    assertFailsWith<JellyseerrInvalidCredentials> {
      client.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
        url = "http://localhost:8080/api/v1/request",
        body = JellyseerrRequestMediaBodyApiFactory.movie(),
      )
    }
  }

  @Test
  fun `test reAuthentication without signInMethod throws InvalidCredentials`() = runTest {
    engine = MockEngine {
      respond(
        content = "Failed to authenticate",
        status = HttpStatusCode.Unauthorized,
      )
    }

    encryptedStorage = FakeEncryptedPreferenceStorage(
      jellyseerrPassword = "testPassword",
    )

    datastore = FakePreferenceStorage(
      jellyseerrAccount = "testAccount",
      jellyseerrAddress = address,
      jellyseerrSignInMethod = null,
    )

    client = JellyseerrRestClient(
      engine = engine,
      encryptedStorage = encryptedStorage,
      datastore = datastore,
    )

    assertFailsWith<JellyseerrInvalidCredentials> {
      client.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
        url = "http://localhost:8080/api/v1/request",
        body = JellyseerrRequestMediaBodyApiFactory.movie(),
      )
    }
  }

  @Test
  fun `test reAuthentication with invalid sign in method throws InvalidCredentials`() = runTest {
    engine = MockEngine {
      respond(
        content = "Failed to authenticate",
        status = HttpStatusCode.Unauthorized,
      )
    }

    encryptedStorage = FakeEncryptedPreferenceStorage(
      jellyseerrPassword = "testPassword",
    )

    datastore = FakePreferenceStorage(
      jellyseerrAccount = "testAccount",
      jellyseerrAddress = address,
      jellyseerrSignInMethod = "invalid method",
    )

    client = JellyseerrRestClient(
      engine = engine,
      encryptedStorage = encryptedStorage,
      datastore = datastore,
    )

    assertFailsWith<JellyseerrInvalidCredentials> {
      client.post<JellyseerrRequestMediaBodyApi, JellyseerrResponseBodyApi>(
        url = "http://localhost:8080/api/v1/request",
        body = JellyseerrRequestMediaBodyApiFactory.movie(),
      )
    }
  }
}
