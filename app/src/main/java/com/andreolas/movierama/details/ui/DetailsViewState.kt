package com.andreolas.movierama.details.ui

import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.SimilarMovie
import com.andreolas.movierama.ui.UIText

data class DetailsViewState(
    val isLoading: Boolean = false,
    val movieId: Int,
    val movieDetails: MovieDetails? = null,
    val reviews: List<Review>? = null,
    val similarMovies: List<SimilarMovie>? = null,
    val error: UIText? = null,
)
