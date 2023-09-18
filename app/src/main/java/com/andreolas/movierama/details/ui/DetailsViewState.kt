package com.andreolas.movierama.details.ui

import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.ui.UIText

data class DetailsViewState(
  val isLoading: Boolean = false,
  val mediaType: MediaType,
  val movieId: Int,
  val movieDetails: MovieDetails? = null,
  val reviews: List<Review>? = null,
  val similarMovies: List<MediaItem.Media>? = null,
  val trailer: Video? = null,
  val error: UIText? = null,
)
