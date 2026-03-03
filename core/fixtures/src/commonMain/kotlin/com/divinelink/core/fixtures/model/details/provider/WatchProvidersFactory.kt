package com.divinelink.core.fixtures.model.details.provider

import com.divinelink.core.model.details.provider.StreamingProvider
import com.divinelink.core.model.details.provider.WatchProviders
import com.divinelink.core.model.details.provider.WatchProvidersByRegion
import com.divinelink.core.model.locale.Country

object WatchProvidersFactory {

  val norwayProviders = WatchProvidersByRegion(
    link = "https://www.themoviedb.org/tv/123-movie/watch?locale=NO",
    buy = listOf(
      StreamingProvider(
        logoPath = "/netflix-logo.png",
        providerId = 119,
        providerName = "Netflix",
        displayPriority = 0,
      ),
    ),
    rent = listOf(
      StreamingProvider(
        logoPath = "/prime-logo.png",
        providerId = 990,
        providerName = "Amazon Prime Video",
        displayPriority = 1,
      ),
    ),
    stream = listOf(
      StreamingProvider(
        logoPath = "/hbomax-logo.png",
        providerId = 384,
        providerName = "HBO Max",
        displayPriority = 2,
      ),
    ),
  )

  val denmarkProviders = WatchProvidersByRegion(
    link = "https://www.themoviedb.org/tv/123-movie/watch?locale=DK",
    buy = listOf(
      StreamingProvider(
        logoPath = "/tv2play-logo.png",
        providerId = 1197,
        providerName = "TV 2 Play",
        displayPriority = 0,
      ),
    ),
    rent = emptyList(),
    stream = listOf(
      StreamingProvider(
        logoPath = "/dplay-logo.png",
        providerId = 1198,
        providerName = "Dplay",
        displayPriority = 1,
      ),
    ),
  )

  val norway = WatchProviders(
    id = 1622,
    results = mapOf(
      Country.NORWAY to norwayProviders,
    ),
  )

  val denmark = WatchProviders(
    id = 1622,
    results = mapOf(
      Country.DENMARK to denmarkProviders,
    ),
  )

  val all = WatchProviders(
    id = 1622,
    results = mapOf(
      Country.NORWAY to norwayProviders,
      Country.DENMARK to denmarkProviders,
    ),
  )
}
