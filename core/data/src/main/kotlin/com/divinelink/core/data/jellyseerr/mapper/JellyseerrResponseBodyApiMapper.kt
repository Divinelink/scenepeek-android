package com.divinelink.core.data.jellyseerr.mapper

import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.network.jellyseerr.model.JellyseerrResponseBodyApi

fun JellyseerrResponseBodyApi.map() = JellyseerrMediaRequest(message = message)
