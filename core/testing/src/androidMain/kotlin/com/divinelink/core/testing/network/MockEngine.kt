package com.divinelink.core.testing.network

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

fun mockEngine(
  content: String,
  status: HttpStatusCode = HttpStatusCode.OK,
  headers: Headers = headersOf(HttpHeaders.ContentType, "application/json"),
) = MockEngine {
  respond(
    content = content,
    status = status,
    headers = headers,
  )
}
