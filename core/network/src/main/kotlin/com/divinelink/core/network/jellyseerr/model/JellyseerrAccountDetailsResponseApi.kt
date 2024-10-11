package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrAccountDetailsResponseApi(
  val id: Long,
  val email: String,
  val displayName: String,
  val avatar: String? = null,
  val requestCount: Long,
  val createdAt: String,
)
