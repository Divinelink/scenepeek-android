package com.divinelink.core.network.client.error

import com.divinelink.core.network.client.decode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject

object ClientErrorResponseSerializer : KSerializer<ClientErrorResponse> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor(
    serialName = ClientErrorResponse::class.simpleName.toString(),
  )

  override fun deserialize(decoder: Decoder): ClientErrorResponse {
    val jsonInput = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")

    val json = jsonInput.decodeJsonElement().jsonObject

    return when {
      json.containsKey("status_message") &&
        json.containsKey("status_code") &&
        json.containsKey("success") -> json.decode<ClientErrorResponse.TMDB>()
      json.containsKey("message") -> json.decode<ClientErrorResponse.Jellyseerr>()
      else -> throw SerializationException("Unknown type: $json")
    }
  }

  override fun serialize(
    encoder: Encoder,
    value: ClientErrorResponse,
  ) {
    error("Serialization is not supported")
  }
}
