package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.model.details.provider.StreamingProvider
import com.divinelink.core.model.details.provider.WatchProviders
import com.divinelink.core.model.details.provider.WatchProvidersByRegion
import com.divinelink.core.model.locale.Country
import com.divinelink.core.network.media.model.details.providers.StreamingProviderResponse
import com.divinelink.core.network.media.model.details.providers.WatchProvidersByRegionResponse
import com.divinelink.core.network.media.model.details.providers.WatchProvidersResponse

fun WatchProvidersResponse.map() = WatchProviders(
  id = id,
  results = results.map(),
)

fun Map<String, WatchProvidersByRegionResponse>.map(): Map<Country?, WatchProvidersByRegion> = this
  .entries
  .associate { result ->
    Country.fromCode(result.key) to result.value.map()
  }

fun WatchProvidersByRegionResponse.map() = WatchProvidersByRegion(
  link = link,
  buy = buy?.map { it.map() } ?: emptyList(),
  rent = rent?.map { it.map() } ?: emptyList(),
  stream = flatrate?.map { it.map() } ?: emptyList(),
)

fun StreamingProviderResponse.map() = StreamingProvider(
  logoPath = logoPath,
  providerId = providerId,
  providerName = providerName,
  displayPriority = displayPriority,
)
