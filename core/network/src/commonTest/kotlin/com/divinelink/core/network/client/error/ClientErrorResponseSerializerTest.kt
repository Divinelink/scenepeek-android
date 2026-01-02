package com.divinelink.core.network.client.error

import io.kotest.matchers.shouldBe
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ClientErrorResponseSerializerTest {

  private val serializer = ClientErrorResponseSerializer

  @Test
  fun `test serialize client error with message`() {
    val response = """
      {"message": "invalid csrf token"}
    """.trimIndent()

    val json = Json.decodeFromString(serializer, response)

    json shouldBe ClientErrorResponse.Jellyseerr("invalid csrf token")
  }

  @Test
  fun `test serialize client error with status_message`() {
    val response = """
      {
        "status_code": 7,
        "status_message": "Invalid API key: You must be granted a valid key.",
        "success": false
      }
    """.trimIndent()

    val json = Json.decodeFromString(serializer, response)

    json shouldBe ClientErrorResponse.TMDB(
      statusCode = 7,
      statusMessage = "Invalid API key: You must be granted a valid key.",
      success = false,
    )
  }

  @Test
  fun `test serialize client error with unknown properties`() {
    val response = """
      {
        "status_code": 7
      }
    """.trimIndent()

    assertFailsWith<SerializationException> {
      Json.decodeFromString(serializer, response)
    }.apply {
      message shouldBe "Unknown type: {\"status_code\":7}"
    }
  }
}
