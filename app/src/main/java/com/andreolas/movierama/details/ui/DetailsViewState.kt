package com.andreolas.movierama.details.ui

import com.andreolas.movierama.details.domain.model.MediaDetails
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.TVDetails
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.ui.UIText

data class DetailsViewState(
  val isLoading: Boolean = false,
  val mediaType: MediaType,
  val movieId: Int,
  val movieDetails: MediaDetails? = null,
  val reviews: List<Review>? = null,
  val similarMovies: List<MediaItem.Media>? = null,
  val trailer: Video? = null,
  val error: UIText? = null,
) {
  val mediaItem = when (movieDetails) {
    is MovieDetails -> MediaItem.Media.Movie(
      id = movieDetails.id,
      name = movieDetails.title,
      posterPath = movieDetails.posterPath,
      releaseDate = movieDetails.releaseDate,
      rating = movieDetails.rating,
      overview = movieDetails.overview ?: "",
      isFavorite = movieDetails.isFavorite,
    )
    is TVDetails -> MediaItem.Media.TV(
      id = movieDetails.id,
      name = movieDetails.title,
      posterPath = movieDetails.posterPath,
      releaseDate = movieDetails.releaseDate,
      rating = movieDetails.rating,
      overview = movieDetails.overview ?: "",
      isFavorite = movieDetails.isFavorite,
    )
    null -> null
  }
}
