package com.divinelink.core.network.jellyseerr.mapper.server.sonarr

import com.divinelink.core.model.jellyseerr.server.sonarr.SonarrInstanceDetails
import com.divinelink.core.network.jellyseerr.mapper.server.map
import com.divinelink.core.network.jellyseerr.model.server.sonarr.SonarrInstanceDetailsResponse

fun SonarrInstanceDetailsResponse.map() = SonarrInstanceDetails(
  server = server.map(),
  profiles = profiles.map { it.map() },
  rootFolders = rootFolders.map { it.map() },
)
