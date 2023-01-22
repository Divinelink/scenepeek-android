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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json

class RestClient {

    private val client = HttpClient(Android) {
        install(Logging) {
            logger = HttpLogger()
            level = if (BuildConfig.DEBUG) {
                LogLevel.ALL
            } else {
                LogLevel.NONE
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }

    suspend fun get(url: String): HttpResponse {
        return client.get(url)
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
