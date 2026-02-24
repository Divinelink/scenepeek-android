package com.divinelink.core.model.details.provider

import kotlinx.serialization.Serializable

@Serializable
data class WatchProvidersByRegion(
  val link: String,
  val buy: List<StreamingProvider>,
  val rent: List<StreamingProvider>,
  val flatrate: List<StreamingProvider>,
)
