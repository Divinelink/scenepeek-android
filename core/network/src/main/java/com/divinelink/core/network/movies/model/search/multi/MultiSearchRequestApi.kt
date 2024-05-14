package com.divinelink.core.network.movies.model.search.multi

import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchRequestApi(
  val query: String,
  val page: Int,
)
