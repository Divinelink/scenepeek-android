package com.divinelink.core.network.media.model.details.credits

import com.divinelink.core.network.client.decode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

object CastSerializer : KSerializer<CastApi> {
  override val descriptor: SerialDescriptor = PolymorphicSerializer(CastApi::class).descriptor

  override fun deserialize(decoder: Decoder): CastApi {
    val jsonInput = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")
    val json = jsonInput.decodeJsonElement().jsonObject

    return when {
      json.containsKey("cast_id") -> json.decode<CastApi.Movie>()
      json.containsKey("known_for_department") -> json.decode<CastApi.TV>()
      else -> throw SerializationException("Unknown type: $json")
    }
  }

  override fun serialize(
    encoder: Encoder,
    value: CastApi,
  ) {
    error("Serialization is not supported")
  }
}
