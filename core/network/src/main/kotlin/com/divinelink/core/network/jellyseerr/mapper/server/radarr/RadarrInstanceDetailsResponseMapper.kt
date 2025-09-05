package com.divinelink.core.network.jellyseerr.mapper.server.radarr

import com.divinelink.core.model.jellyseerr.server.radarr.RadarrInstanceDetails
import com.divinelink.core.network.jellyseerr.mapper.server.map
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceDetailsResponse

fun RadarrInstanceDetailsResponse.map() = RadarrInstanceDetails(
  server = server.map(),
  profiles = profiles.map { it.map() },
  rootFolders = rootFolders.map { it.map() },
)
