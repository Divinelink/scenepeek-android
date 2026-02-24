package com.divinelink.core.model.details.provider

import kotlinx.serialization.Serializable

@Serializable
data class WatchProvidersByRegion(
  val link: String,
  val buy: List<StreamingProvider>,
  val rent: List<StreamingProvider>,
  val stream: List<StreamingProvider>,
) {
  val isEmpty = buy.isEmpty() && rent.isEmpty() && stream.isEmpty()
}
