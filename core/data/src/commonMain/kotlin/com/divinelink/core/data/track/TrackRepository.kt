package com.divinelink.core.data.track

interface TrackRepository {
  fun isSeasonComplete(showId: Long, seasonNumber: Int): Boolean
}
