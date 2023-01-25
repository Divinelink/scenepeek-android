package com.andreolas.movierama.home.domain.model

data class PopularMovie(
    val id: Int,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val rating: String,
    val overview: String,
    val isFavorite: Boolean,
)
