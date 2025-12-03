package com.divinelink.core.network.jellyseerr.model

import kotlinx.serialization.Serializable

@Serializable
data class PageInfoResponse(
  val pages: Int,
  val pageSize: Int,
  val results: Int,
  val page: Int,
)
