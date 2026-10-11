package com.divinelink.core.database.track

import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.model.track.backup.LibraryExport
import com.divinelink.core.model.track.backup.RestoreProcess
import kotlinx.coroutines.flow.Flow

interface TrackDao {

  fun getUserShows(): Flow<List<Long>>

  fun updateShowStatus(
    showId: Long,
    status: UserShowStatus,
  ): Result<Unit>

  fun getEpisodesLeftThisSeason(
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

  fun isWatchedEpisode(episodeId: Long): Boolean
  fun markEpisodeAsWatched(
    episodeId: Long,
    watchedAt: Long,
  )
  fun unmarkEpisodeAsWatched(episodeId: Long)

  fun exportLibrary(): LibraryExport
  fun restoreLibrary(encoded: String): Result<RestoreProcess>

  fun observeLatestWatchHistory(): Flow<List<WatchedEpisode>>
  fun getWatchHistory(lastId: Long, lastWatchedAt: Long?): List<WatchedEpisode>
}
