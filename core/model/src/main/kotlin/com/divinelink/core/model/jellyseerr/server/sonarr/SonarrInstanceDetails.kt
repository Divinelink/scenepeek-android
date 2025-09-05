package com.divinelink.core.model.jellyseerr.server.sonarr

import com.divinelink.core.model.jellyseerr.server.InstanceProfile
import com.divinelink.core.model.jellyseerr.server.InstanceRootFolder
import kotlinx.serialization.Serializable

@Serializable
data class SonarrInstanceDetails(
  val server: SonarrInstance,
  val profiles: List<InstanceProfile>,
  val rootFolders: List<InstanceRootFolder>,
)
