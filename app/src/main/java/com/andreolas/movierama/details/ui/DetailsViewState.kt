package com.andreolas.movierama.details.ui

import com.andreolas.movierama.details.domain.model.MediaDetails
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.TVDetails
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.snackbar.SnackbarMessage

data class DetailsViewState(
  val isLoading: Boolean = false,
  val mediaType: MediaType,
  val movieId: Int,
  val mediaDetails: MediaDetails? = null,
  val userRating: String? = null,
  val reviews: List<Review>? = null,
  val similarMovies: List<MediaItem.Media>? = null,
  val trailer: Video? = null,
  val error: UIText? = null,
  val snackbarMessage: SnackbarMessage? = null,
) {
  val mediaItem = when (mediaDetails) {
    is MovieDetails -> MediaItem.Media.Movie(
      id = mediaDetails.id,
      name = mediaDetails.title,
      posterPath = mediaDetails.posterPath,
      releaseDate = mediaDetails.releaseDate,
      rating = mediaDetails.rating,
      overview = mediaDetails.overview ?: "",
      isFavorite = mediaDetails.isFavorite,
    )
    is TVDetails -> MediaItem.Media.TV(
      id = mediaDetails.id,
      name = mediaDetails.title,
      posterPath = mediaDetails.posterPath,
      releaseDate = mediaDetails.releaseDate,
      rating = mediaDetails.rating,
      overview = mediaDetails.overview ?: "",
      isFavorite = mediaDetails.isFavorite,
    )
    null -> null
  }
}
