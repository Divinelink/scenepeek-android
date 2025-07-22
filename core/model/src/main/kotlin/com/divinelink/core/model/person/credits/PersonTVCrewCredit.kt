package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType

data class PersonTVCrewCredit(
  val name: String,
  val originalName: String,
  val firstAirDate: String,
  val job: String,
  val episodeCount: Long,
  val department: String,
  val originCountry: List<String>,
  override val id: Long,
  override val adult: Boolean,
  override val backdropPath: String?,
  override val genreIds: List<Int>,
  override val originalLanguage: String,
  override val overview: String,
  override val popularity: Double,
  val posterPath: String?,
  override val voteAverage: Double,
  override val voteCount: Long,
  val mediaType: MediaType,
  override val creditId: String,
) : PersonCredit(
  id = id,
  mediaItem = MediaItem.Media.TV(
    id = id.toInt(),
    name = name,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = firstAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount.toInt(),
    overview = overview,
    isFavorite = null,
  ),
  role = PersonRole.Crew(
    job = job,
    creditId = creditId,
    totalEpisodes = episodeCount,
    department = department,
  ),
)
