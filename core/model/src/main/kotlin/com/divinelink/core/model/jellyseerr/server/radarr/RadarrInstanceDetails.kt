package com.divinelink.core.model.jellyseerr.server.radarr

import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import kotlinx.serialization.Serializable

@Serializable
data class RadarrInstanceDetails(
  val server: RadarrInstance,
  val profiles: List<InstanceProfile>,
  val rootFolders: List<InstanceRootFolder>,
)
