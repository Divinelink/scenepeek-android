package com.divinelink.core.database.track

interface TrackDao {

  fun isSeasonComplete(showId: Long, seasonNumber: Int): Boolean
}
