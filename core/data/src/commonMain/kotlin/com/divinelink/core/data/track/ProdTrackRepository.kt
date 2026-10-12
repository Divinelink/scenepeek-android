package com.divinelink.core.data.track

import com.divinelink.core.commons.util.encodeToString
import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.database.track.TrackDao
import com.divinelink.core.model.entity.EntityChange
import com.divinelink.core.model.export.FileData
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.model.track.backup.RestoreProcess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class ProdTrackRepository(
  val dao: TrackDao,
) : TrackRepository {
  private val _historyEvents = MutableSharedFlow<EntityChange>(extraBufferCapacity = 64)
  override val historyEvents: SharedFlow<EntityChange> = _historyEvents

  override fun getUserShows(): Flow<List<Long>> = dao.getUserShows()

  override fun updateShowStatus(
    showId: Long,
    status: UserShowStatus,
  ): Result<Unit> = dao
    .updateShowStatus(showId, status)

  override fun episodesLeftThisSeason(
    showId: Long,
    seasonNumber: Int,
  ): Int = dao
    .getEpisodesLeftThisSeason(showId, seasonNumber)

  override suspend fun findCurrentWatchingSeason(showId: Long): Int? = dao
    .findCurrentWatchingSeason(showId)

  override suspend fun isSeasonComplete(
    showId: Long,
    seasonNumber: Int,
  ): Boolean = dao
    .isSeasonComplete(showId, seasonNumber)

  override fun fetchNumberOfSeasons(showId: Long): Int = dao
    .fetchNumberOfSeasons(showId)

  override fun getSyncErrors(): Flow<List<ShowSyncError>> = dao
    .getSyncErrors()

  override fun setSyncError(
    showId: Long,
    seasonNumber: Int,
  ) = dao
    .setSyncError(showId, seasonNumber)

  override fun clearSyncErrors(showIds: List<Long>) = dao
    .clearSyncErrors(showIds)

  override fun markEpisodeAsWatched(
    episodeId: Long,
    watchedAt: Long,
  ) = dao.markEpisodeAsWatched(
    episodeId = episodeId,
    watchedAt = watchedAt,
  )

  override suspend fun unmarkEpisodeAsWatched(episodeId: Long) {
    dao.unmarkEpisodeAsWatched(episodeId = episodeId)
    _historyEvents.emit(EntityChange.Removed(ids = setOf(episodeId)))
  }

  override fun isWatchedEpisode(episodeId: Long): Boolean = dao
    .isWatchedEpisode(episodeId)

  override fun backupLibrary(): Result<FileData> = runCatching {
    val export = dao.exportLibrary()

    FileData(
      extension = "json",
      name = "scenepeek-backup-${export.exportedAt}",
      content = export.encodeToString(),
    )
  }

  override fun restoreLibrary(encodedFile: String): Result<RestoreProcess> = dao
    .restoreLibrary(encodedFile)

  override fun getWatchHistory(lastId: Long, lastWatchedAt: Long?): List<WatchedEpisode> = dao
    .getWatchHistory(lastId, lastWatchedAt)

  override fun observeRecentHistory(): Flow<List<WatchedEpisode>> = dao
    .observeLatestWatchHistory()
}
