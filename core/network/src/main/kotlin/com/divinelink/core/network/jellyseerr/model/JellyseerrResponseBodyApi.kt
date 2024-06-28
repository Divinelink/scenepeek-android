package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class JellyseerrResponseBodyApi(
  val message: String? = null,
  val type: String? = null,
)
