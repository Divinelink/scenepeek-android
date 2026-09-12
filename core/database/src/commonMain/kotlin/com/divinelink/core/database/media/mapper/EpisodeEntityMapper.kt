package com.divinelink.core.database.media.mapper

import com.divinelink.core.commons.extensions.parseOrNull
import com.divinelink.core.database.season.EpisodeEntity
import com.divinelink.core.model.details.Episode
import kotlinx.datetime.LocalDate

fun EpisodeEntity.map(accountRating: Int?) = Episode(
  id = id.toInt(),
  name = name,
  airDate = LocalDate.parseOrNull(airDate),
  overview = overview,
  runtime = runtime,
  number = episodeNumber.toInt(),
  seasonNumber = seasonNumber.toInt(),
  showId = showId,
  stillPath = stillPath,
  voteAverage = voteAverage.toString(),
  voteCount = voteCount.toInt(),
  crew = emptyList(),
  guestStars = emptyList(),
  accountRating = accountRating,
)
