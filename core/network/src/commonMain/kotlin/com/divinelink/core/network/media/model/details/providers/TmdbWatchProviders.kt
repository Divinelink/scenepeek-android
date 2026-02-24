package com.divinelink.core.network.media.model.details.providers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchProvidersResponse(
  val id: Int,
  val results: Map<String, WatchProvidersByRegionResponse>,
)

@Serializable
data class WatchProvidersByRegionResponse(
  val link: String,
  val buy: List<StreamingProviderResponse>?,
  val rent: List<StreamingProviderResponse>?,
  val flatrate: List<StreamingProviderResponse>?,
)

@Serializable
data class StreamingProviderResponse(
  @SerialName("logo_path") val logoPath: String,
  @SerialName("provider_id") val providerId: Int,
  @SerialName("provider_name") val providerName: String,
  @SerialName("display_priority") val displayPriority: Int,
)
