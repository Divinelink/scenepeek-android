package com.divinelink.core.model.jellyseerr

import com.divinelink.core.commons.extensions.localizeIsoDate
import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrProfile(
  val id: Long,
  val email: String?,
  val displayName: String,
  val avatar: String?,
  val requestCount: Long,
  val createdAt: String,
  val permissions: List<ProfilePermission>,
) {
  val formattedCreatedAt: String = createdAt.localizeIsoDate()
}
