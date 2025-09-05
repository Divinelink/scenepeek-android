package com.divinelink.core.fixtures.model.jellyseerr.server.sonarr

import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstance

object SonarrInstanceFactory {

  val sonarr = SonarrInstance(
    id = 0,
    name = "Sonarr",
    is4k = false,
    isDefault = true,
    activeDirectory = "/data/tv",
    activeProfileId = 6,
  )

  val sonarr4K = SonarrInstance(
    id = 1,
    name = "Sonarr 4K",
    is4k = true,
    isDefault = true,
    activeDirectory = "/data/tv",
    activeProfileId = 6,
  )

  val all = listOf(
    sonarr,
    sonarr4K,
  )
}
