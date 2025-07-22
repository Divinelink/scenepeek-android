package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class PersonMovieCrewCredit(
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
  override val voteAverage: Double,
  override val voteCount: Long,
  val job: String,
  val department: String,
  val mediaType: MediaType,
  override val creditId: String,
) : PersonCredit(
  id = id,
  mediaItem = MediaItem.Media.Movie(
    id = id.toInt(),
    name = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    voteAverage = voteAverage,
    voteCount = voteCount.toInt(),
    overview = overview,
    isFavorite = null,
  ),
  role = PersonRole.Crew(
    job = job,
    creditId = creditId,
    department = department,
  ),
)
