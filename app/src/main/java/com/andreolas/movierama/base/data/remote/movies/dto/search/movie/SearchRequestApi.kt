package com.andreolas.movierama.base.data.remote.movies.dto.search.movie

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestApi(
  val query: String,
  val page: Int,
)
