package com.divinelink.core.network.client

import com.divinelink.core.model.exception.AppException
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ConnectTimeoutException
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

class KtorClientTest {

  private lateinit var engine: HttpClientEngine

  private val url = "http://localhost:8080"

  @Test
  fun `test successful response returns expected data`() = runTest {
    engine = MockEngine {
      respond(
        content = """{"message": "Success"}""",
        status = HttpStatusCode.OK,
      )
    }

    val response: String = ktorClient(engine).get(url).bodyAsText()
    response shouldBe """{"message": "Success"}"""
  }

  @Test
  fun `test unauthorised status returns AppException Unauthorised`() = runTest {
    engine = MockEngine {
      respond(
        content = "Unauthorized",
        status = HttpStatusCode.Unauthorized,
      )
    }

    assertFailsWith<AppException.Unauthorized> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test forbidden status returns AppException Forbidden`() = runTest {
    engine = MockEngine {
      respond(
        content = "",
        status = HttpStatusCode.Forbidden,
      )
    }

    assertFailsWith<AppException.Forbidden> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test not found status returns AppException NotFound`() = runTest {
    engine = MockEngine {
      respond(
        content = "",
        status = HttpStatusCode.NotFound,
      )
    }

    assertFailsWith<AppException.NotFound> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test conflict status returns AppException Conflict`() = runTest {
    engine = MockEngine {
      respond(
        content = "",
        status = HttpStatusCode.Conflict,
      )
    }

    assertFailsWith<AppException.Conflict> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test too many requests status returns AppException TooManyRequests`() = runTest {
    engine = MockEngine {
      respond(
        content = "",
        status = HttpStatusCode.TooManyRequests,
      )
    }

    assertFailsWith<AppException.TooManyRequests> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test payload too large status returns AppException PayloadTooLarge`() = runTest {
    engine = MockEngine {
      respond(
        content = "",
        status = HttpStatusCode.PayloadTooLarge,
      )
    }

    assertFailsWith<AppException.PayloadTooLarge> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test server error status returns AppException ServerError`() = runTest {
    engine = MockEngine {
      respond(
        content = "",
        status = HttpStatusCode.InternalServerError,
      )
    }

    assertFailsWith<AppException.ServerError> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test bad request status returns AppException BadRequest`() = runTest {
    engine = MockEngine {
      respond(
        content = "",
        status = HttpStatusCode.BadRequest,
      )
    }

    assertFailsWith<AppException.BadRequest> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test unknown exception returns AppException Unknown`() = runTest {
    engine = MockEngine {
      throw Exception("Unknown error")
    }

    assertFailsWith<AppException.Unknown> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test socket timeout exception returns AppException SocketTimeout`() = runTest {
    engine = MockEngine {
      throw java.net.SocketTimeoutException("Socket timeout")
    }

    assertFailsWith<AppException.SocketTimeout> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test connection timeout exception returns AppException ConnectionTimeout`() = runTest {
    engine = MockEngine {
      throw ConnectTimeoutException("Connection timeout", null)
    }

    assertFailsWith<AppException.ConnectionTimeout> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test SSL handshake exception returns AppException Ssl`() = runTest {
    engine = MockEngine {
      throw javax.net.ssl.SSLHandshakeException("SSL handshake failed")
    }

    assertFailsWith<AppException.Ssl> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test serialization exception returns AppException Serialization`() = runTest {
    engine = MockEngine {
      throw kotlinx.serialization.SerializationException("Serialization error")
    }

    assertFailsWith<AppException.Serialization> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test connect exception returns AppException Offline`() = runTest {
    engine = MockEngine {
      throw java.net.ConnectException("Connection failed")
    }

    assertFailsWith<AppException.Offline> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test unknown host exception returns AppException Offline`() = runTest {
    engine = MockEngine {
      throw java.net.UnknownHostException("Unknown host")
    }

    assertFailsWith<AppException.Offline> {
      ktorClient(engine).get<Any>(url)
    }
  }

  @Test
  fun `test app exception returns AppException Unknown`() = runTest {
    val customMessage = "Custom error message"
    val appException = AppException.Unknown(customMessage)

    assertFailsWith<AppException.Unknown> {
      throw appException
    }.apply {
      assert(this.message == customMessage)
    }
  }
}
