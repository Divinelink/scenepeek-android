package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrProfileResponse(
  val id: Long,
  val email: String,
  val displayName: String,
  val avatar: String? = null,
  val requestCount: Long,
  val createdAt: String,
  val permissions: Long,
)
