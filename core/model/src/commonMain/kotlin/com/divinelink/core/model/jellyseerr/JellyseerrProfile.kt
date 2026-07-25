package com.divinelink.core.model.jellyseerr

import com.divinelink.core.commons.extensions.toLocalDateTime
import com.divinelink.core.model.jellyseerr.permission.ProfilePermission
import kotlinx.datetime.LocalDate
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
)

val JellyseerrProfile.createdAtLocalDate: LocalDate?
  get() = createdAt.toLocalDateTime()?.date
