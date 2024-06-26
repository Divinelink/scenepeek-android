package com.divinelink.core.network.client

import com.divinelink.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
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

val localJson = Json {
  prettyPrint = true
  isLenient = true
  coerceInputValues = true
  ignoreUnknownKeys = true
}

fun androidClient(): HttpClient = HttpClient(Android) {
  install(Logging) {
    logger = HttpLogger()
    level = if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
  }

  install(ContentNegotiation) {
    json(localJson)
  }

  defaultRequest {
    contentType(ContentType.Application.Json)
  }
}

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Any> HttpClient.get(url: String): T {
  val json = this.get(url).bodyAsText()
  try {
    return localJson.decodeFromString(T::class.serializer(), json)
  } catch (e: Exception) {
    Timber.e("${e.message}")
    throw e
  }
}

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Any, reified V : Any> HttpClient.post(
  url: String,
  body: T,
): V {
  val json = this.post(url) {
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
suspend inline fun <reified T : Any, reified V : Any> HttpClient.delete(
  url: String,
  body: T,
): V {
  val json = this.delete(url) {
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
suspend inline fun <reified T : Any> HttpClient.delete(url: String): T {
  val json = this.delete(url).bodyAsText()

  try {
    return localJson.decodeFromString(T::class.serializer(), json)
  } catch (e: Exception) {
    Timber.e("${e.message}")
    throw e
  }
}

@OptIn(InternalAPI::class)
suspend fun HttpClient.put(
  url: String,
  body: String,
): HttpResponse = this.put(url) {
  this.body = body
}
