package com.andreolas.movierama.base.data.remote.movies.dto.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestApi(
    val query: String,
    val page: Int,
)
