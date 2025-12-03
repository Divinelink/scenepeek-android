package com.divinelink.core.fixtures.model.jellyseerr.server.sonarr

import com.divinelink.core.model.jellyseerr.server.ServerInstance

object SonarrInstanceFactory {

  val sonarr = ServerInstance.Sonarr(
    id = 0,
    name = "Sonarr",
    is4k = false,
    isDefault = true,
    activeDirectory = "/data/tv",
    activeProfileId = 6,
  )

  val anime = ServerInstance.Sonarr(
    id = 1,
    name = "Animarr",
    is4k = false,
    isDefault = false,
    activeDirectory = "/data/anime",
    activeProfileId = 2,
  )

  val sonarr4K = ServerInstance.Sonarr(
    id = 2,
    name = "Sonarr 4K",
    is4k = true,
    isDefault = true,
    activeDirectory = "/data/tv",
    activeProfileId = 6,
  )

  val all = listOf(
    sonarr,
    anime,
    sonarr4K,
  )
}
