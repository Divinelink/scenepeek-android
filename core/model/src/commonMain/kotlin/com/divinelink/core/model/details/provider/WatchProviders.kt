package com.divinelink.core.model.details.provider

import com.divinelink.core.model.locale.Country
import kotlinx.serialization.Serializable

@Serializable
data class WatchProviders(
  val id: Int,
  val results: Map<Country?, WatchProvidersByRegion>,
)
