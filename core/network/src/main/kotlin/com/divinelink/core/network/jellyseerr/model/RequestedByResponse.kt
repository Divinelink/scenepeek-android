package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestedByResponse(
  val id: Int,
  val displayName: String,
)
