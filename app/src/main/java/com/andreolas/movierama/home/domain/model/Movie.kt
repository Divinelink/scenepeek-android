package com.andreolas.movierama.home.domain.model

import com.andreolas.movierama.details.domain.model.SimilarMovie

/**
 * This is a generic movie item that wraps every movie information
 * required to pass on composable components.
 */
data class Movie(
  val id: Int,
  val posterPath: String?,
  val releaseDate: String,
  val title: String,
  val rating: String,
  val overview: String,
  val isFavorite: Boolean?,
)

fun MediaItem.Media.Movie.toMediaItem(): Movie {
  return Movie(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    title = this.name,
    rating = this.rating,
    overview = this.overview,
    isFavorite = this.isFavorite,
  )
}

fun MediaItem.Media.TV.toMediaItem(): Movie {
  return Movie(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    title = this.name,
    rating = this.rating,
    overview = this.overview,
    isFavorite = this.isFavorite,
  )
}

// toMediaItem() is used to convert from the API model to the UI model
fun SimilarMovie.toMediaItem(): Movie {
  return Movie(
    id = this.id,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    title = this.title,
    rating = this.rating,
    overview = this.overview,
    isFavorite = null,
  )
}
