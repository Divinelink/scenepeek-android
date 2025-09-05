package com.divinelink.core.fixtures.model.jellyseerr.radarr

import com.divinelink.core.model.jellyseerr.radarr.RadarrInstance

object RadarrInstanceFactory {

  val radarr = RadarrInstance(
    id = 0,
    name = "Radarr",
    is4k = false,
    isDefault = true,
    activeDirectory = "/data/movies",
    activeProfileId = 6,
  )

  val radarr4K = RadarrInstance(
    id = 1,
    name = "4K Radarr",
    is4k = true,
    isDefault = true,
    activeDirectory = "/data/movies",
    activeProfileId = 5,
  )

  val radarrSecondary = RadarrInstance(
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
