package com.divinelink.core.model.jellyseerr

import com.divinelink.core.commons.extensions.localizeIsoDate

data class JellyseerrAccountDetails(
  val id: Long,
  val email: String?,
  val displayName: String,
  val avatar: String?,
  val requestCount: Long,
  val createdAt: String,
) {
  val formattedCreatedAt: String = createdAt.localizeIsoDate()
}
