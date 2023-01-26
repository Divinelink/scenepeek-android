package com.andreolas.movierama.details.ui

import com.andreolas.movierama.home.domain.model.PopularMovie

/**
 * Information required when launching the movie details screen.
 */
data class DetailsNavArguments(
    val movie: PopularMovie,
)
