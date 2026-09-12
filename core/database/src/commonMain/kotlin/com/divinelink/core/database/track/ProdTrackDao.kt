package com.divinelink.core.database.track

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.show.ShowSyncError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProdTrackDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
) : TrackDao {
  private val userEpisodeQueries = database.userEpisodesEntityQueries
  private val syncErrorQueries = database.showSyncErrorQueries
  private val userShowQueries = database.userShowsEntityQueries

  override fun getUserShows(): Flow<List<Long>> = userShowQueries
    .getAllUserShows()
    .asFlow()
    .mapToList(dispatcher.io)
    .map { entity ->
      entity.map { show -> show.showId }
    }

  override fun getEpisodesLeftThisSeason(
    showId: Long,
    seasonNumber: Int,
  ): Int = userEpisodeQueries
    .getUserSeasonProgress(
      showId = showId,
      seasonNumber = seasonNumber.toLong(),
    )
    .executeAsOneOrNull()
    ?.let {
      it.totalEpisodes.toInt() - it.watchedEpisodes.toInt()
    } ?: 0

  override fun getSyncErrors(): Flow<List<ShowSyncError>> = syncErrorQueries
    .getAllSyncErrors()
    .asFlow()
    .mapToList(context = dispatcher.io)

  override fun setSyncError(
    showId: Long,
    seasonNumber: Int,
  ) = database.transaction {
    syncErrorQueries.insertSyncError(showId, seasonNumber.toLong())
  }

  override fun clearSyncErrors(showIds: List<Long>) = database.transaction {
    showIds.forEach { showId ->
      syncErrorQueries.clearSyncErrors(showId)
    }
  }

  override suspend fun isSeasonComplete(
    showId: Long,
    seasonNumber: Int,
  ): Boolean {
    val result = userEpisodeQueries
      .getUserSeasonProgress(showId, seasonNumber.toLong())
      .executeAsOneOrNull()

    val total = result?.totalEpisodes ?: 0
    val watched = result?.watchedEpisodes

    return total > 0 && total == watched
  }

  override fun fetchNumberOfSeasons(showId: Long): Int = database
    .showDetailsEntityQueries
    .fetchNumberOfSeasons(showId)
    .executeAsOneOrNull()
    ?.toInt() ?: 0

  override suspend fun findCurrentWatchingSeason(showId: Long): Int? = database
    .userShowsEntityQueries
    .findCurrentWatchingSeason(showId)
    .executeAsOneOrNull()
    ?.toInt()

  override fun isWatchedEpisode(episodeId: Long): Boolean = userEpisodeQueries
    .isWatchedEpisode(episodeId)
    .executeAsOneOrNull() ?: false

  override fun markEpisodeAsWatched(episodeId: Long) = database.transaction {
    userEpisodeQueries.upsertUserEpisode(
      episodeId = episodeId,
      watchedAt = null,
    )
  }

  override fun unmarkEpisodeAsWatched(episodeId: Long) = database.transaction {
    userEpisodeQueries.deleteUserEpisode(
      episodeId = episodeId,
    )
  }
}
