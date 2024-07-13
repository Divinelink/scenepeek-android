package com.divinelink.core.model.details

/**
 * Represents details of a movie.
 */
data class Movie(
  override val id: Int,
  override val title: String,
  override val posterPath: String,
  override val overview: String?,
  override val genres: List<String>?,
  override val releaseDate: String,
  override val rating: String,
  val runtime: String?,
  val cast: List<Person>,
  val director: Person?,
  override val isFavorite: Boolean,
) : MediaDetails()
