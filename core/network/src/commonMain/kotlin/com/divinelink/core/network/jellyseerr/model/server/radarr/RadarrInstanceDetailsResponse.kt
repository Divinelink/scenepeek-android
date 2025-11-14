package com.divinelink.core.network.jellyseerr.model.server.radarr

import com.divinelink.core.network.jellyseerr.model.server.InstanceProfileResponse
import com.divinelink.core.network.jellyseerr.model.server.InstanceRootFolderResponse
import kotlinx.serialization.Serializable

@Serializable
data class RadarrInstanceDetailsResponse(
  val server: RadarrInstanceResponse,
  val profiles: List<InstanceProfileResponse>,
  val rootFolders: List<InstanceRootFolderResponse>,
)
