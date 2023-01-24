package com.andreolas.movierama.base.data.remote.movies.dto.popular

import kotlinx.serialization.Serializable

@Serializable
data class PopularRequestApi(
    val page: Int,
)
