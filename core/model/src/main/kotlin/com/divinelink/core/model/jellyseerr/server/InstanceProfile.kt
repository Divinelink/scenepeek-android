package com.divinelink.core.model.jellyseerr.server

import kotlinx.serialization.Serializable

@Serializable
data class InstanceProfile(
  val id: Int,
  val name: String,
)
