package com.divinelink.core.network.client

import com.divinelink.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import timber.log.Timber

class RestClient {
  val tmdbUrl = BuildConfig.TMDB_BASE_URL
  private val authToken = BuildConfig.TMDB_AUTH_TOKEN

  private val localJson = Json {
    prettyPrint = true
    isLenient = true
    coerceInputValues = true
    ignoreUnknownKeys = true
  }

  val client = HttpClient(Android) {
    install(Logging) {
      logger = HttpLogger()
      level = if (BuildConfig.DEBUG) {
        LogLevel.ALL
      } else {
        LogLevel.NONE
      }
    }

    install(HttpCookies) {
      // TODO Investigate if this is the best way to handle cookies
      storage = AcceptAllCookiesStorage()
    }

    install(ContentNegotiation) {
      json(localJson)
    }

    defaultRequest {
      contentType(ContentType.Application.Json)
      bearerAuth(authToken)
    }
  }

  @OptIn(InternalSerializationApi::class)
  internal suspend inline fun <reified T : Any> get(url: String): T {
    val json = client.get(url).bodyAsText()

    try {
      return localJson.decodeFromString(T::class.serializer(), json)
    } catch (e: Exception) {
      Timber.e("${e.message}")
      throw e
    }
  }

  @OptIn(InternalSerializationApi::class)
  internal suspend inline fun <reified T : Any, reified V : Any> post(
    url: String,
    body: T,
  ): V {
    val json = client.post(url) {
      setBody(body)
    }.bodyAsText()

    try {
      return localJson.decodeFromString(V::class.serializer(), json)
    } catch (e: Exception) {
      Timber.e("${e.message}")
      throw e
    }
  }

  @OptIn(InternalSerializationApi::class)
  internal suspend inline fun <reified T : Any, reified V : Any> delete(
    url: String,
    body: T,
  ): V {
    val json = client.delete(url) {
      setBody(body)
    }.bodyAsText()

    try {
      return localJson.decodeFromString(V::class.serializer(), json)
    } catch (e: Exception) {
      Timber.e("${e.message}")
      throw e
    }
  }

  @OptIn(InternalSerializationApi::class)
  internal suspend inline fun <reified T : Any> delete(url: String): T {
    val json = client.delete(url).bodyAsText()

    try {
      return localJson.decodeFromString(T::class.serializer(), json)
    } catch (e: Exception) {
      Timber.e("${e.message}")
      throw e
    }
  }

  @OptIn(InternalAPI::class)
  suspend fun put(
    url: String,
    body: String,
  ): HttpResponse = client.put(url) {
    this.body = body
  }

  fun close() {
    client.close()
  }
}
