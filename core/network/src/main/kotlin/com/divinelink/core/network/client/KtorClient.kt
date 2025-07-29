package com.divinelink.core.network.client

import com.divinelink.core.commons.Constants
import com.divinelink.core.network.AppException
import com.divinelink.core.network.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
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
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import timber.log.Timber
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

val localJson = Json {
  prettyPrint = true
  isLenient = true
  coerceInputValues = true
  ignoreUnknownKeys = true
}

fun ktorClient(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
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

  HttpResponseValidator {
    validateResponse { response ->
      if (!response.status.isSuccess()) {
        val statusCode = response.status.value
        val error = when (statusCode) {
          HttpStatusCode.Unauthorized.value -> AppException.Unauthorized(
            response.status.description,
          )
          HttpStatusCode.Forbidden.value -> AppException.Forbidden()
          HttpStatusCode.NotFound.value -> AppException.NotFound()
          HttpStatusCode.Conflict.value -> AppException.Conflict()
          HttpStatusCode.TooManyRequests.value -> AppException.TooManyRequests()
          HttpStatusCode.PayloadTooLarge.value -> AppException.PayloadTooLarge()
          in 500..599 -> AppException.ServerError()
          else -> AppException.BadRequest()
        }
        throw error
      }
    }
    handleResponseExceptionWithRequest { cause, request ->
      Timber.e("Exception occurred: $cause, URL: ${request.url}")
      val dataError = when (cause) {
        is SocketTimeoutException -> AppException.SocketTimeout()
        is ConnectTimeoutException -> AppException.ConnectionTimeout()
        is HttpRequestTimeoutException -> AppException.RequestTimeout()
        is SSLHandshakeException -> AppException.Ssl()
        is SerializationException -> AppException.Serialization()
        is UnknownHostException -> AppException.Offline()
        is IOException -> AppException.Offline()
        else -> AppException.Unknown(message = cause.message)
      }
      throw dataError
    }
  }
}

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Any> HttpClient.get(url: String): T {
  try {
    val json = this.get(url).bodyAsText()

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
  try {
    val response = this.post(url) { setBody(body) }

    val json = response.bodyAsText()
    return localJson.decodeFromString(V::class.serializer(), json)
  } catch (e: ResponseException) {
    throw e
  } catch (e: Exception) {
    throw e
  }
}

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Any, reified V : Any> HttpClient.delete(
  url: String,
  body: T,
): V {
  try {
    val json = this.delete(url) { setBody(body) }.bodyAsText()

    return localJson.decodeFromString(V::class.serializer(), json)
  } catch (e: ResponseException) {
    throw e
  } catch (e: Exception) {
    throw e
  }
}

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Any> HttpClient.delete(url: String): T {
  try {
    val json = this.delete(url).bodyAsTextOrNull() ?: Constants.EMPTY_JSON_RESPONSE

    return localJson.decodeFromString(T::class.serializer(), json)
  } catch (e: ResponseException) {
    throw e
  } catch (e: Exception) {
    throw e
  }
}

@OptIn(InternalSerializationApi::class)
suspend inline fun <reified T : Any, reified V : Any> HttpClient.put(
  url: String,
  body: T,
): V {
  try {
    val response = this.put(url) { setBody(body) }

    val json = response.bodyAsText()
    return localJson.decodeFromString(V::class.serializer(), json)
  } catch (e: ResponseException) {
    throw e
  } catch (e: Exception) {
    throw e
  }
}

suspend inline fun HttpResponse.bodyAsTextOrNull(): String? = when (status) {
  HttpStatusCode.NoContent -> null
  else -> bodyAsText()
}
