package com.divinelink.core.network.jellyseerr.model.server

import kotlinx.serialization.Serializable

@Serializable
data class InstanceProfileResponse(
  val id: Int,
  val name: String,
)
