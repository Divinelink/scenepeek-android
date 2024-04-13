package com.andreolas.movierama.base.data.remote.movies.dto.search.multi

import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchRequestApi(
  val query: String,
  val page: Int,
)
