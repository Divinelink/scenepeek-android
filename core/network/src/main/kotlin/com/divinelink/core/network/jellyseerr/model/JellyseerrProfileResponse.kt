package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrProfileResponse(
  val id: Int,
  val email: String,
  val displayName: String,
  val avatar: String? = null,
  val requestCount: Int,
  val createdAt: String,
  val permissions: Long,
)
