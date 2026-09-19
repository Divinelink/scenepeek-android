package com.divinelink.core.database.media.mapper

import com.divinelink.core.commons.extensions.parseOrNull
import com.divinelink.core.database.user.GetAllUpNextEpisodes
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.model.track.UserShowStatus
import kotlinx.datetime.LocalDate

fun GetAllUpNextEpisodes.map(): NextUpEpisode {
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

  return NextUpEpisode(
    episode = episode,
    showTitle = showTitle,
    showPosterPath = showPosterPath,
    showStatus = UserShowStatus.from(showStatus) ?: UserShowStatus.WATCHING,
    watchedEpisodes = watchedEpisodes.toInt(),
    totalEpisodes = totalEpisodes.toInt(),
  )
}
