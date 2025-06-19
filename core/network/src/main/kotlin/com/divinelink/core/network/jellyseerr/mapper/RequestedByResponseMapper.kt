package com.divinelink.core.network.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.media.JellyseerrRequester
import com.divinelink.core.network.jellyseerr.model.RequestedByResponse

fun RequestedByResponse.map() = JellyseerrRequester(
  displayName = displayName,
)
