package com.divinelink.core.network.jellyseerr.model.server.sonarr

import com.divinelink.core.network.jellyseerr.model.server.InstanceProfileResponse
import com.divinelink.core.network.jellyseerr.model.server.InstanceRootFolderResponse
import kotlinx.serialization.Serializable

@Serializable
data class SonarrInstanceDetailsResponse(
  val server: SonarrInstanceResponse,
  val profiles: List<InstanceProfileResponse>,
  val rootFolders: List<InstanceRootFolderResponse>,
)
