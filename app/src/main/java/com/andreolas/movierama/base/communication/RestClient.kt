@file:Suppress("TooGenericExceptionCaught")

package com.andreolas.movierama.base.communication

import com.andreolas.movierama.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json
import timber.log.Timber

class RestClient {

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

    install(ContentNegotiation) {
      json(localJson)
    }

    defaultRequest {
      contentType(ContentType.Application.Json)
    }
  }

  internal suspend inline fun <reified T : Any> get(url: String): T {
    val json = client.get(url).bodyAsText()

    try {
      return localJson.decodeFromString(json)
    } catch (e: Exception) {
      Timber.e("${e.message}")
      throw e
    }
  }

  @OptIn(InternalAPI::class)
  suspend fun post(url: String, body: String): HttpResponse {
    return client.post(url) {
      this.body = body
    }
  }

  @OptIn(InternalAPI::class)
  suspend fun put(url: String, body: String): HttpResponse {
    return client.put(url) {
      this.body = body
    }
  }

  fun close() {
    client.close()
  }
}
