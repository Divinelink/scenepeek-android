package com.divinelink.core.model.details

import com.divinelink.core.model.details.rating.RatingCount

/**
 * Represents details of a TV show.
 */
data class TV(
  override val id: Int,
  override val title: String,
  override val posterPath: String,
  override val overview: String?,
  override val genres: List<String>?,
  override val releaseDate: String,
  override val ratingCount: RatingCount,
  override val isFavorite: Boolean,
  override val imdbId: String?,
  val creators: List<Person>?,
  val credits: List<Person>,
  val seasons: List<Nothing>? = null, // TODO
  val numberOfSeasons: Int,
) : MediaDetails()
