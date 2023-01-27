package com.andreolas.movierama.details.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val posterPath: String,
    val overview: String?,
    val genres: List<String>?,
    val director: Director?,
    val cast: List<Actor>,
    val releaseDate: String,
    val rating: String,
    val runtime: String?,
    val isFavorite: Boolean,
)
