package com.divinelink.core.network.media.mapper.details

import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.network.media.model.details.season.SeasonDetailsResponse
import com.divinelink.core.network.media.model.details.toHourMinuteFormat

fun SeasonDetailsResponse.map(): SeasonDetails = SeasonDetails(
  id = id,
  name = name,
  overview = overview,
  airDate = airDate,
  episodeCount = episodes.count(),
  posterPath = posterPath,
  voteAverage = voteAverage,
  totalRuntime = episodes
    .filter { it.runtime != null }
    .sumOf { it.runtime!! }
    .toHourMinuteFormat(),
  episodes = episodes.map { it.map() },
)
