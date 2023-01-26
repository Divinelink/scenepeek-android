package com.andreolas.movierama.base.data.remote.movies.dto.details.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewsRequestApi(
    @SerialName("movie_id")
    val movieId: Int,
)
