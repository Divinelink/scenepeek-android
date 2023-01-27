package com.andreolas.movierama.details.ui

/**
 * Information required when launching the movie details screen.
 */
data class DetailsNavArguments(
    val movieId: Int,
    val isFavorite: Boolean,
)
