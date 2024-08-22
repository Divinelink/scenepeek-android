package com.divinelink.core.network.media.model.details

import com.divinelink.core.network.client.decode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

object DetailsResponseApiSerializer : KSerializer<DetailsResponseApi> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
    serialName = DetailsResponseApi::class.java.name,
  )

  override fun deserialize(decoder: Decoder): DetailsResponseApi {
    val jsonInput = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")

    val json = jsonInput.decodeJsonElement().jsonObject

    return when {
      json.containsKey("original_title") -> json.decode<DetailsResponseApi.Movie>()
      json.containsKey("first_air_date") -> json.decode<DetailsResponseApi.TV>()
      else -> throw SerializationException("Unknown type: $json")
    }
  }

  override fun serialize(
    encoder: Encoder,
    value: DetailsResponseApi,
  ) {
    error("Serialization is not supported")
  }
}
