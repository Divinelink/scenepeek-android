package com.andreolas.movierama.base.data.remote.movies.dto.details.credits

import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

object CastSerializer : KSerializer<Cast> {
  override val descriptor: SerialDescriptor = PolymorphicSerializer(Cast::class).descriptor

  override fun deserialize(decoder: Decoder): Cast {
    val jsonInput = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")
    val json = jsonInput.decodeJsonElement().jsonObject

    val format = Json { ignoreUnknownKeys = true }

    return when {
      json.containsKey("cast_id") -> format.decodeFromJsonElement(
        deserializer = Cast.Movie.serializer(),
        element = json
      )
      json.containsKey("known_for_department") -> format.decodeFromJsonElement(
        deserializer = Cast.TV.serializer(),
        element = json
      )
      else -> throw SerializationException("Unknown type: $json")
    }
  }

  override fun serialize(encoder: Encoder, value: Cast) {
    error("Serialization is not supported")
  }
}
