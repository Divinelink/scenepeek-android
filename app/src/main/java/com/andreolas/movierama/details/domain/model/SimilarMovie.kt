package com.andreolas.movierama.details.domain.model

import kotlinx.serialization.Serializable

@Serializable
@Deprecated("Use MediaItem.Media instead")
data class SimilarMovie(
    val id: Int,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val rating: String,
    val overview: String,
)
