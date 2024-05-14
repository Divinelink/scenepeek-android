package com.divinelink.core.network.movies.model.search.movie

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestApi(
  val query: String,
  val page: Int,
)
