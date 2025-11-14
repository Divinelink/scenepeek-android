package com.divinelink.core.network.media.model.states

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

object RateResponseApiSerializer : KSerializer<RateResponseApi> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
    serialName = RateResponseApi::class.java.name,
  )

  override fun deserialize(decoder: Decoder): RateResponseApi {
    val jsonInput = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")

    val json = jsonInput.decodeJsonElement()

    if (json is JsonPrimitive && json.content == "false") {
      return RateResponseApi.False
    }

    return if (json is JsonObject) {
      when {
        json.containsKey("value") -> {
          val value = json["value"]?.jsonPrimitive?.content?.toFloatOrNull()
          if (value != null) {
            RateResponseApi.Value(value)
          } else {
            throw SerializationException("Invalid value: $json")
          }
        }
        else -> throw SerializationException("Unknown type: $json")
      }
    } else {
      throw SerializationException("Invalid JSON: $json")
    }
  }

  override fun serialize(
    encoder: Encoder,
    value: RateResponseApi,
  ) {
    error("Serialization is not supported")
  }
}
