package com.andreolas.movierama.base.data.remote

import com.andreolas.movierama.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object RestClient {

    val client = HttpClient(Android) {
        install(Logging) {
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
    }

    //    suspend fun get(url: String): HttpResponse {
//        return client.get(url)
//    }
//
//    @OptIn(InternalAPI::class)
//    suspend fun post(url: String, body: String): HttpResponse {
//        return client.post(url) {
//            this.body = body
//        }
//    }
//
//    @OptIn(InternalAPI::class)
//    suspend fun put(url: String, body: String): HttpResponse {
//        return client.put(url) {
//            this.body = body
//        }
//    }
//
//    fun close() {
//        client.close()
//    }
}
