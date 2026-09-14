package com.divinelink.core.data.track

import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.database.track.TrackDao
import kotlinx.coroutines.flow.Flow

class ProdTrackRepository(
  val dao: TrackDao,
) : TrackRepository {

  override fun getUserShows(): Flow<List<Long>> = dao.getUserShows()

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
    watchedAt: String,
  ) = dao.markEpisodeAsWatched(
    episodeId = episodeId,
    watchedAt = watchedAt,
  )

  override fun unmarkEpisodeAsWatched(episodeId: Long) = dao.unmarkEpisodeAsWatched(
    episodeId = episodeId,
  )

  override fun isWatchedEpisode(episodeId: Long): Boolean = dao
    .isWatchedEpisode(episodeId)
}
