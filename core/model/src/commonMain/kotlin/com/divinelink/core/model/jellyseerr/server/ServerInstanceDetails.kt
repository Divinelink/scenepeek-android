package com.divinelink.core.model.jellyseerr.server

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class ServerInstanceDetails(
  @Contextual val server: ServerInstance,
  val profiles: List<InstanceProfile>,
  val rootFolders: List<InstanceRootFolder>,
)
