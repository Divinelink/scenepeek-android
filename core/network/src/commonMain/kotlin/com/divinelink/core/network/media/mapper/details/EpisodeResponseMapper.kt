package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.model.details.Episode
import com.divinelink.core.network.media.model.details.season.EpisodeResponse
import com.divinelink.core.network.media.model.details.toHourMinuteFormat
import com.divinelink.core.network.media.model.details.toPerson

fun EpisodeResponse.map(accountRating: Int?) = Episode(
  id = id,
  name = name,
  airDate = airDate,
  overview = overview,
  runtime = runtime.toHourMinuteFormat(),
  seasonNumber = seasonNumber,
  showId = showId,
  stillPath = stillPath,
  voteCount = voteCount,
  voteAverage = voteAverage,
  number = episodeNumber,
  crew = crew.map(),
  guestStars = guestStars.map { it.toPerson() },
  accountRating = accountRating,
)
