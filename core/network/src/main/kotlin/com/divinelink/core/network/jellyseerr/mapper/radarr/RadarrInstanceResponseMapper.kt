package com.divinelink.core.network.jellyseerr.mapper.radarr

import com.divinelink.core.model.jellyseerr.radarr.RadarrInstance
import com.divinelink.core.network.jellyseerr.model.radarr.RadarrInstanceResponse

fun List<RadarrInstanceResponse>.map() = this.map {
  it.map()
}

fun RadarrInstanceResponse.map() = RadarrInstance(
  id = id,
  name = name,
  is4k = is4k,
  isDefault = isDefault,
  activeDirectory = activeDirectory,
  activeProfileId = activeProfileId,
)
