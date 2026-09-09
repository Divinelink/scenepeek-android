package com.divinelink.core.database.track

import com.divinelink.core.database.Database

class ProdTrackDao(
  database: Database,
) : TrackDao {
  private val userEpisodeQueries = database.userEpisodesEntityQueries
  private val userShowQueries = database.userShowsEntityQueries

  override fun isSeasonComplete(showId: Long, seasonNumber: Int): Boolean = userEpisodeQueries
    .isSeasonComplete(
      showId = showId,
      seasonNumber = seasonNumber.toLong(),
    )
    .executeAsOneOrNull()
    ?.let {
      it.totalEpisodes == it.watchedEpisodes
    } ?: false
}
