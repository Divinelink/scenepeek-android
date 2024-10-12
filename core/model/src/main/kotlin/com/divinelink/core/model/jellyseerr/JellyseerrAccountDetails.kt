package com.divinelink.core.model.jellyseerr

import com.divinelink.core.commons.Constants
import com.divinelink.core.commons.extensions.formatTo

data class JellyseerrAccountDetails(
  val id: Long,
  val email: String?,
  val displayName: String,
  val avatar: String?,
  val requestCount: Long,
  val createdAt: String,
) {
  val formattedCreatedAt: String = createdAt.formatTo(
    inputFormat = Constants.ISO_8601,
    outputFormat = Constants.MMMM_DD_YYYY,
  ) ?: createdAt
}
