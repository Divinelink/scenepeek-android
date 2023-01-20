package com.andreolas.movierama.home.domain

data class PopularMovie(
    val id: Long,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val rating: String,
)
