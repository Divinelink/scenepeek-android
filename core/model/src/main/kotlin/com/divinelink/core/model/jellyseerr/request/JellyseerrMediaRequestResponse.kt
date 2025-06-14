package com.divinelink.core.model.jellyseerr.request

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo

data class JellyseerrMediaRequestResponse(
  val message: String?,
  val mediaInfo: JellyseerrMediaInfo,
)
