package com.divinelink.core.model.jellyseerr.request

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo

data class MediaRequestResult(
  val requestId: Int,
  val message: String?,
  val mediaInfo: JellyseerrMediaInfo,
)
