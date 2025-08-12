package com.divinelink.core.model.details

import com.divinelink.core.model.details.media.MediaDetailsInformation
import com.divinelink.core.model.details.rating.RatingCount

/**
 * Represents details of a movie.
 */
data class Movie(
  override val id: Int,
  override val title: String,
  override val posterPath: String,
  override val backdropPath: String,
  override val overview: String?,
  override val genres: List<String>?,
  override val releaseDate: String,
  override val ratingCount: RatingCount,
  override val tagline: String?,
  val runtime: String?,
  val cast: List<Person>,
  val creators: List<Person>?,
  override val isFavorite: Boolean,
  override val imdbId: String?,
  override val information: MediaDetailsInformation.Movie,
) : MediaDetails()
