package com.divinelink.core.testing.factories.api.jellyseerr.response.server.radarr

import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceResponse

object RadarrInstanceResponseFactory {

  val radarr = RadarrInstanceResponse(
    id = 0,
    name = "Radarr",
    is4k = false,
    isDefault = true,
    activeDirectory = "/data/movies",
    activeProfileId = 6,
  )

  val radarr4K = RadarrInstanceResponse(
    id = 1,
    name = "4K Radarr",
    is4k = true,
    isDefault = true,
    activeDirectory = "/data/movies",
    activeProfileId = 5,
  )

  val radarrSecondary = RadarrInstanceResponse(
    id = 2,
    name = "Secondary Radarr",
    is4k = false,
    isDefault = false,
    activeDirectory = "/data/movies",
    activeProfileId = 2,
  )

  val all = listOf(
    radarr,
    radarr4K,
    radarrSecondary,
  )
}
