package com.divinelink.core.data.track

import com.divinelink.core.database.show.ShowSyncError
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
  fun getUserShows(): Flow<List<Long>>

  fun episodesLeftThisSeason(
    showId: Long,
    seasonNumber: Int,
  ): Int
  fun fetchNumberOfSeasons(showId: Long): Int
  suspend fun findCurrentWatchingSeason(showId: Long): Int?
  suspend fun isSeasonComplete(
    showId: Long,
    seasonNumber: Int,
  ): Boolean

  fun setSyncError(
    showId: Long,
    seasonNumber: Int,
  )
  fun getSyncErrors(): Flow<List<ShowSyncError>>
  fun clearSyncErrors(showIds: List<Long>)

  fun markEpisodeAsWatched(
    episodeId: Long,
    watchedAt: String,
  )
  fun unmarkEpisodeAsWatched(episodeId: Long)
  fun isWatchedEpisode(episodeId: Long): Boolean
}
