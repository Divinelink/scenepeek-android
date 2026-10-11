package com.divinelink.core.database.media.mapper

import com.divinelink.core.commons.extensions.parseOrNull
import com.divinelink.core.database.user.GetWatchHistory
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.track.WatchedEpisode
import kotlinx.datetime.LocalDate

fun GetWatchHistory.map(): WatchedEpisode {
  val episode = Episode(
    id = episodeId.toInt(),
    name = episodeName,
    airDate = LocalDate.parseOrNull(airDate),
    overview = null,
    runtime = null,
    number = episodeNumber.toInt(),
    seasonNumber = seasonNumber.toInt(),
    showId = showId,
    stillPath = episodeThumbnail,
    voteAverage = null,
    voteCount = null,
    crew = emptyList(),
    guestStars = emptyList(),
    accountRating = null,
  )

  return WatchedEpisode(
    episode = episode,
    showTitle = showTitle,
    showPosterPath = showPosterPath,
    watchedAt = LocalDate.parse(watchedAt ?: ""),
  )
}
