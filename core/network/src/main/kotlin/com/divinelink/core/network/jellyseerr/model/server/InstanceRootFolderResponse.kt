package com.divinelink.core.network.jellyseerr.model.server

import kotlinx.serialization.Serializable

@Serializable
data class InstanceRootFolderResponse(
  val id: Int,
  val freeSpace: Long,
  val path: String,
)
