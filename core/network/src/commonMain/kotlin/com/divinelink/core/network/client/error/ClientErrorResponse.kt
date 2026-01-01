package com.divinelink.core.network.client.error

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable(with = ClientErrorResponseSerializer::class)
sealed interface ClientErrorResponse {

  @Serializable
  data class Jellyseerr(
    val message: String,
  ) : ClientErrorResponse

  @Serializable
  data class TMDB(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String,
    @SerialName("success") val success: Boolean,
  ) : ClientErrorResponse

  @Serializable
  data object Unknown : ClientErrorResponse
}
