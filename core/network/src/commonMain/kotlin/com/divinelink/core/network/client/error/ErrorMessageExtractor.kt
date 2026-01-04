package com.divinelink.core.network.client.error

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

suspend inline fun <reified T : ClientErrorResponse> HttpResponse.extractErrorMessage(): String? =
  try {
    when (val error = body<T>()) {
      is ClientErrorResponse.Jellyseerr -> error.message
      is ClientErrorResponse.TMDB -> error.statusMessage
      ClientErrorResponse.Unknown -> null
    }
  } catch (_: Exception) {
    try {
      bodyAsText().takeIf { it.isNotBlank() }
    } catch (_: Exception) {
      null
    }
  }
