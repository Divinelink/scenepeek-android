package com.divinelink.core.model.person.credits

import com.divinelink.core.model.credits.PersonRole
import com.divinelink.core.model.media.MediaType

data class PersonTVCastCredit(
  override val id: Long,
  override val adult: Boolean,
  override val backdropPath: String?,
  override val genreIds: List<Int>,
  override val originalLanguage: String,
  val originalName: String,
  override val overview: String,
  override val popularity: Double,
  override val posterPath: String?,
  val firstAirDate: String,
  val name: String,
  override val voteAverage: Double,
  override val voteCount: Long,
  val character: String,
  val episodeCount: Int,
  val originCountry: List<String>,
  override val mediaType: MediaType,
  override val creditId: String,
) : PersonCredit(
  id = id,
  mediaName = name,
  mediaOriginalName = originalName,
  mediaReleaseDate = firstAirDate,
  mediaType = mediaType,
  posterPath = posterPath,
  role = PersonRole.SeriesActor(
    character = character,
    creditId = creditId,
    totalEpisodes = episodeCount,
  ),
)
