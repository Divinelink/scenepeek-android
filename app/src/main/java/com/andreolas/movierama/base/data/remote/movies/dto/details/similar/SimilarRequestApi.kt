package com.andreolas.movierama.base.data.remote.movies.dto.details.similar

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimilarRequestApi(
    val page: Int = 1,
    @SerialName("movie_id")
    val movieId: Int,
)
