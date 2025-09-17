package com.divinelink.core.model.jellyseerr

import com.divinelink.core.commons.extensions.localizeIsoDate
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrProfile(
  val id: Int,
  val email: String?,
  val displayName: String,
  val avatar: String?,
  val requestCount: Int,
  val createdAt: String,
  val permissions: List<ProfilePermission>,
) {
  val formattedCreatedAt: String = createdAt.localizeIsoDate()
}
