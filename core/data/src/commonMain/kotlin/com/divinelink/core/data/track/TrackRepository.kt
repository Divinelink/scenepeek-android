package com.divinelink.core.data.track

import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.model.export.FileData
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.model.track.backup.RestoreProcess
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
  fun getUserShows(): Flow<List<Long>>

  fun updateShowStatus(
    showId: Long,
    status: UserShowStatus,
  ): Result<Unit>

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

  fun backupLibrary(): Result<FileData>
  fun restoreLibrary(encodedFile: String): Result<RestoreProcess>

  fun getWatchHistory(offset: Long): Flow<List<WatchedEpisode>>
}
