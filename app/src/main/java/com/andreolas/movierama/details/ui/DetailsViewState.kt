package com.andreolas.movierama.details.ui

import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.ui.UIText

/**
 * A sealed class defining all possible states of the details screen.
 */
sealed class DetailsViewState(
    open val movie: PopularMovie,
    val isLoading: Boolean,
) {

    data class Initial(
        override val movie: PopularMovie,
    ) : DetailsViewState(
        isLoading = true,
        movie = movie,
    )

    data class Error(
        override val movie: PopularMovie,
        val error: UIText,
    ) : DetailsViewState(
        isLoading = false,
        movie = movie,
    )

    data class Completed(
        override val movie: PopularMovie,
        val movieDetails: MovieDetails,
    ) : DetailsViewState(
        isLoading = false,
        movie = movie,
    )
}
