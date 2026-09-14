package com.divinelink.core.database.track

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.track.backup.LibraryExport
import com.divinelink.core.model.track.backup.ShowExport
import com.divinelink.core.model.track.backup.WatchedEpisodeExport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProdTrackDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
  private val clock: ClockProvider,
) : TrackDao {
  private val userEpisodeQueries = database.userEpisodesEntityQueries
  private val syncErrorQueries = database.showSyncErrorQueries
  private val userShowQueries = database.userShowsEntityQueries
  private val mediaQueries = database.mediaItemEntityQueries
  private val savedQueries = database.favoriteMediaEntityQueries

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

  override fun markEpisodeAsWatched(
    episodeId: Long,
    watchedAt: String,
  ) = database.transaction {
    userEpisodeQueries.upsertUserEpisode(
      episodeId = episodeId,
      watchedAt = watchedAt,
    )
  }

  override fun unmarkEpisodeAsWatched(episodeId: Long) = database.transaction {
    userEpisodeQueries.deleteUserEpisode(
      episodeId = episodeId,
    )
  }

  override fun exportLibrary(): LibraryExport {
    val shows = getShowExports()

    return LibraryExport(
      exportedAt = clock.clock.now().toString(),
      shows = shows,
    )
  }

  private fun getShowExports() = userShowQueries
    .getAllUserShows()
    .executeAsList().map { userShow ->
      val watchedEpisodes = userEpisodeQueries.watchedEpisodesForShow(userShow.showId)
        .executeAsList()
        .map {
          WatchedEpisodeExport(
            it.episodeId,
            it.watchedAt,
          )
        }

      val media = mediaQueries.selectMediaItemByIdAndType(
        id = userShow.showId,
        mediaType = MediaType.TV.value,
      ).executeAsOne()

      ShowExport(
        tmdbId = userShow.showId,
        title = media.name,
        posterPath = media.posterPath,
        status = userShow.status,
        watchedEpisodes = watchedEpisodes,
        addedAt = userShow.addedAt,
      )
    }
}
