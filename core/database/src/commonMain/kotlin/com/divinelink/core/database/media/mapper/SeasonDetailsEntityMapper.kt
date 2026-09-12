package com.divinelink.core.database.media.mapper

import com.divinelink.core.commons.extensions.parseOrNull
import com.divinelink.core.database.season.SeasonDetailsEntity
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.media.MediaItem
import kotlinx.datetime.LocalDate

fun SeasonDetailsEntity.map(
  episodes: List<Episode>,
  guestStars: List<MediaItem.Person>,
) = SeasonDetails(
  id = id.toInt(),
  name = name,
  overview = overview,
  posterPath = posterPath,
  airDate = LocalDate.parseOrNull(airDate),
  episodeCount = episodeCount.toInt(),
  voteAverage = voteAverage,
  episodes = episodes,
  totalRuntime = runtime,
  guestStars = guestStars,
)
