package com.andreolas.movierama.details.domain.model

import com.andreolas.movierama.R
import com.andreolas.movierama.ui.UIText

/**
 * A collection of possible results for an attempt to fetch movie details, similar movies and reviews.
 */
sealed class MovieDetailsResult {
    data class DetailsSuccess(val movieDetails: MovieDetails) : MovieDetailsResult()

    data class ReviewsSuccess(val reviews: List<Review>) : MovieDetailsResult()

    data class SimilarSuccess(val similar: List<SimilarMovie>) : MovieDetailsResult()

    sealed class Failure(
        open val message: UIText = UIText.ResourceText(R.string.general_error_message),
    ) : MovieDetailsResult() {

        data class FatalError(
            override val message: UIText = UIText.ResourceText(R.string.details__fatal_error_fetching_details),
        ) : Failure(
            message = message,
        )

        object Unknown : Failure()
    }
}

class MovieDetailsException : Exception()
