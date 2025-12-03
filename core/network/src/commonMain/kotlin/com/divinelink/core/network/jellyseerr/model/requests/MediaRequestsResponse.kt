package com.divinelink.core.network.jellyseerr.model.requests

import com.divinelink.core.network.jellyseerr.model.MediaInfoRequestResponse
import com.divinelink.core.network.jellyseerr.model.PageInfoResponse
import kotlinx.serialization.Serializable

@Serializable
data class MediaRequestsResponse(
  val pageInfo: PageInfoResponse,
  val results: List<MediaInfoRequestResponse>,
)
