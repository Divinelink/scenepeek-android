package com.andreolas.movierama.base.data.remote.movies.dto.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailsRequestApi(
    @SerialName("movie_id")
    val movieId: Int,
)
