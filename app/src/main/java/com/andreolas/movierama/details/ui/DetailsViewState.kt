package com.andreolas.movierama.details.ui

import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.ui.UIText

/**
 * A sealed class defining all possible states of the details screen.
 */
sealed class DetailsViewState(
    val isLoading: Boolean,
) {

    object Initial : DetailsViewState(
        isLoading = true,
    )

    data class Error(
        val error: UIText,
    ) : DetailsViewState(
        isLoading = false,
    )

    data class Completed(
        val movieDetails: MovieDetails,
    ) : DetailsViewState(
        isLoading = false,
    )
}
