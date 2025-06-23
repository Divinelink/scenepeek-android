package com.divinelink.core.model.jellyseerr.request

import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import kotlinx.serialization.SerialName

data class JellyseerrMediaRequestResponse(
  @SerialName("id") val requestId: Int,
  val message: String?,
  val mediaInfo: JellyseerrMediaInfo,
)
