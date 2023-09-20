package com.andreolas.movierama.details.domain.model

sealed class MediaDetails {
  abstract val id: Int
  abstract val title: String
  abstract val posterPath: String
  abstract val overview: String?
  abstract val director: Director?
  abstract val cast: List<Actor>
  abstract val releaseDate: String
  abstract val rating: String
  abstract val isFavorite: Boolean

  fun copy(
    id: Int = this.id,
    title: String = this.title,
    posterPath: String = this.posterPath,
    overview: String? = this.overview,
    director: Director? = this.director,
    cast: List<Actor> = this.cast,
    releaseDate: String = this.releaseDate,
    rating: String = this.rating,
    isFavorite: Boolean = this.isFavorite,
  ): MediaDetails {
    return when (this) {
      is MovieDetails -> this.copy(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        director = director,
        cast = cast,
        releaseDate = releaseDate,
        rating = rating,
        isFavorite = isFavorite,
      )
      is TVDetails -> this.copy(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        director = director,
        cast = cast,
        releaseDate = releaseDate,
        rating = rating,
        isFavorite = isFavorite,
      )
    }
  }
}

data class MovieDetails(
  override val id: Int,
  override val title: String,
  override val posterPath: String,
  override val overview: String?,
  val genres: List<String>?,
  override val director: Director?,
  override val cast: List<Actor>,
  override val releaseDate: String,
  override val rating: String,
  val runtime: String?,
  override val isFavorite: Boolean,
) : MediaDetails()

data class TVDetails(
  override val id: Int,
  override val title: String,
  override val posterPath: String,
  override val overview: String?,
  override val director: Director?,
  override val cast: List<Actor>,
  override val releaseDate: String,
  override val rating: String,
  override val isFavorite: Boolean,
  val seasons: List<Nothing>? = null, // TODO
) : MediaDetails()
