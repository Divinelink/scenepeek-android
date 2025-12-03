package com.divinelink.core.network.jellyseerr.mapper.server.radarr

import com.divinelink.core.model.jellyseerr.server.ServerInstanceDetails
import com.divinelink.core.network.jellyseerr.mapper.server.map
import com.divinelink.core.network.jellyseerr.model.server.radarr.RadarrInstanceDetailsResponse

fun RadarrInstanceDetailsResponse.map() = ServerInstanceDetails(
  server = server.map(),
  profiles = profiles.map { it.map(server.activeProfileId == it.id) },
  rootFolders = rootFolders.map {
    it.map(
      isDefault = server.activeDirectory == it.path,
    )
  },
)
