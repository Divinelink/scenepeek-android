package com.divinelink.core.data.track

import com.divinelink.core.database.track.TrackDao

class ProdTrackRepository(
  val dao: TrackDao,
) : TrackRepository {

  override fun isSeasonComplete(showId: Long, seasonNumber: Int): Boolean = dao
    .isSeasonComplete(showId, seasonNumber)
}
