package com.divinelink.core.network.jellyseerr.mapper.server.radarr

import com.divinelink.core.model.jellyseerr.server.ServerInstance
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceResponse

fun List<RadarrInstanceResponse>.map() = this.map {
  it.map()
}

fun RadarrInstanceResponse.map() = ServerInstance.Radarr(
  id = id,
  name = name,
  is4k = is4k,
  isDefault = isDefault,
  activeDirectory = activeDirectory,
  activeProfileId = activeProfileId,
)
