package com.divinelink.core.network.jellyseerr.mapper.server.sonarr

import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceResponse

fun List<SonarrInstanceResponse>.map() = this.map {
  it.map()
}

fun SonarrInstanceResponse.map() = ServerInstance.Sonarr(
  id = id,
  name = name,
  is4k = is4k,
  isDefault = isDefault,
  activeDirectory = activeDirectory,
  activeProfileId = activeProfileId,
)
