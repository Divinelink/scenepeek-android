package com.divinelink.core.model.jellyseerr

import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest

data class JellyseerrRequests(
  val pageInfo: PageInfo,
  val results: List<JellyseerrRequest>,
)
