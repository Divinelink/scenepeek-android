package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class PersonMovieCastCredit(
  override val id: Long,
  override val adult: Boolean,
  override val backdropPath: String?,
  override val genreIds: List<Int>,
  override val originalLanguage: String,
  val originalTitle: String,
  override val overview: String,
  override val popularity: Double,
  val posterPath: String?,
  val releaseDate: String,
  val title: String,
  val video: Boolean,
  override val voteAverage: Double,
  override val voteCount: Long,
  val character: String,
  val mediaType: MediaType,
  val order: Int?,
  override val creditId: String,
) : PersonCredit(
  id = id,
  mediaItem = MediaItem.Media.Movie(
    id = id.toInt(),
    name = title,
    posterPath = posterPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount.toInt(),
    overview = overview,
    backdropPath = backdropPath,
    isFavorite = null,
  ),
  role = PersonRole.MovieActor(
    character = character,
    order = order,
  ),
)
