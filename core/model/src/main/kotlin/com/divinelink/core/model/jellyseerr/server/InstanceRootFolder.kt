package com.divinelink.core.model.jellyseerr.server

import kotlinx.serialization.Serializable

@Serializable
data class InstanceRootFolder(
  val id: Int,
  val freeSpace: String,
  val path: String,
)
