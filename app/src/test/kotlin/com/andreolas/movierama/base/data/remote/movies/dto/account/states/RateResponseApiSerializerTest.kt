package com.andreolas.movierama.base.data.remote.movies.dto.account.states

import com.andreolas.factories.api.account.states.RateResponseApiFactory
import com.divinelink.core.network.media.model.states.RateResponseApiSerializer
import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.junit.Assert.assertThrows
import org.junit.Test

class RateResponseApiSerializerTest {

  private val serializer = RateResponseApiSerializer

  @Test
  fun `should serialize and deserialize RateResponseApi`() {
    val jsonAsString = """
      {
        "value": 8.0
      }
    """.trimIndent()

    val expected = RateResponseApiFactory.rated()

    val json = Json.decodeFromString(serializer, jsonAsString)

    assertThat(json).isEqualTo(expected)
  }

  @Test
  fun `deserialize - false as primitive`() {
    val jsonAsString = """false"""

    val expected = RateResponseApiFactory.`false`()
    val json = Json.decodeFromString(serializer, jsonAsString)

    assertThat(json).isEqualTo(expected)
  }

  @Test
  @Throws(SerializationException::class)
  fun `deserialize - missing value key throws exception`() {
    val json = """{"wrongKey": "data"}"""

    val exception = assertThrows(SerializationException::class.java) {
      Json.decodeFromString(serializer, json)
    }

    assertThat("Unknown type: {\"wrongKey\":\"data\"}").isEqualTo(exception.message)
  }

  @Test
  fun `deserialize - invalid value format throws exception`() {
    val json = """{"value": "invalid"}"""

    val exception = assertThrows(SerializationException::class.java) {
      Json.decodeFromString(serializer, json)
    }

    assertThat(exception.message).isEqualTo(
      "Invalid value: {\"value\":\"invalid\"}",
    )
  }

  @Test
  fun `deserialize - invalid json throws exception`() {
    val json = """invalid-json"""

    val exception = assertThrows(SerializationException::class.java) {
      Json.decodeFromString(serializer, json)
    }

    assertThat(exception.message).isEqualTo(
      "Invalid JSON: invalid-json",
    )
  }
}
