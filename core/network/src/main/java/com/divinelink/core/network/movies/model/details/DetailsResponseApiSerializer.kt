package com.divinelink.core.network.movies.model.details

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

object DetailsResponseApiSerializer : KSerializer<DetailsResponseApi> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
    serialName = DetailsResponseApi::class.java.name
  )

  override fun deserialize(decoder: Decoder): DetailsResponseApi {
    val jsonInput = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")

    val format = Json {
      prettyPrint = true
      isLenient = true
      coerceInputValues = true
      ignoreUnknownKeys = true
    }

    val json = jsonInput.decodeJsonElement().jsonObject

    return when {
      json.containsKey("original_title") -> format.decodeFromJsonElement(
        deserializer = DetailsResponseApi.Movie.serializer(),
        element = json
      )
      json.containsKey("first_air_date") -> format.decodeFromJsonElement(
        deserializer = DetailsResponseApi.TV.serializer(),
        element = json
      )
      else -> throw SerializationException("Unknown type: $json")
    }
  }

  override fun serialize(encoder: Encoder, value: DetailsResponseApi) {
    error("Serialization is not supported")
  }
}
