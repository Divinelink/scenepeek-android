package com.divinelink.core.network.jellyseerr.mapper.sonarr

import com.divinelink.core.model.jellyseerr.radarr.SonarrInstance
import com.divinelink.core.network.jellyseerr.model.radarr.SonarrInstanceResponse

fun List<SonarrInstanceResponse>.map() = this.map {
  it.map()
}

fun SonarrInstanceResponse.map() = SonarrInstance(
  id = id,
  name = name,
  is4k = is4k,
  isDefault = isDefault,
  activeDirectory = activeDirectory,
  activeProfileId = activeProfileId,
)
