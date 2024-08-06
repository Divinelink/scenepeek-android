package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
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
  override val posterPath: String?,
  val releaseDate: String,
  val title: String,
  val video: Boolean,
  override val voteAverage: Double,
  override val voteCount: Long,
  val character: String,
  override val mediaType: MediaType,
  override val creditId: String,
) : PersonCredit(
  id = id,
  mediaName = title,
  mediaOriginalName = originalTitle,
  mediaReleaseDate = releaseDate,
  mediaType = mediaType,
  posterPath = posterPath,
  role = PersonRole.MovieActor(character),
)
