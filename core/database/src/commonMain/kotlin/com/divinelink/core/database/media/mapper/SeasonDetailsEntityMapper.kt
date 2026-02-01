package com.divinelink.core.database.media.mapper

import com.divinelink.core.database.season.SeasonDetailsEntity
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.SeasonDetails

fun SeasonDetailsEntity.map(
  episodes: List<Episode>,
  guestStars: List<Person>,
) = SeasonDetails(
  id = id.toInt(),
  name = name,
  overview = overview,
  posterPath = posterPath,
  airDate = airDate,
  episodeCount = episodeCount.toInt(),
  voteAverage = voteAverage,
  episodes = episodes,
  totalRuntime = runtime,
  guestStars = guestStars,
)
