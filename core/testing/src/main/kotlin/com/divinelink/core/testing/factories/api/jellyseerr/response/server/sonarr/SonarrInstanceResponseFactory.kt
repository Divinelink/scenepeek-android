package com.divinelink.core.testing.factories.api.jellyseerr.response.server.sonarr

import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceResponse

object SonarrInstanceResponseFactory {

  val sonarr = SonarrInstanceResponse(
    id = 0,
    name = "Sonarr",
    is4k = false,
    isDefault = true,
    activeDirectory = "/data/tv",
    activeProfileId = 6,
    activeAnimeProfileId = 6,
    activeAnimeDirectory = "/data/anime",
    activeLanguageProfileId = 1,
    activeAnimeLanguageProfileId = 1,
  )

  val sonarr4K = SonarrInstanceResponse(
    id = 1,
    name = "Sonarr 4K",
    is4k = true,
    isDefault = true,
    activeDirectory = "/data/tv",
    activeProfileId = 6,
    activeAnimeProfileId = null,
    activeAnimeDirectory = null,
    activeLanguageProfileId = null,
    activeAnimeLanguageProfileId = null,
  )

  val all = listOf(
    sonarr,
    sonarr4K,
  )
}
