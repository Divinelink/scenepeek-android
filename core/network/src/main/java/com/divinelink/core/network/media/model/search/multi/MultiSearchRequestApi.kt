package com.divinelink.core.network.media.model.search.multi

import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchRequestApi(
  val query: String,
  val page: Int,
)
