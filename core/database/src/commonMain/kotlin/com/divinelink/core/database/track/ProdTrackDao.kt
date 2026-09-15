package com.divinelink.core.database.track

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.database.season.EpisodeEntity
import com.divinelink.core.database.season.SeasonDetailsEntity
import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.track.backup.EpisodeExport
import com.divinelink.core.model.track.backup.LibraryExport
import com.divinelink.core.model.track.backup.SeasonExport
import com.divinelink.core.model.track.backup.ShowExport
import com.divinelink.core.model.track.backup.UserEpisodeExport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class ProdTrackDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
  private val clock: ClockProvider,
) : TrackDao {
  private val userEpisodeQueries = database.userEpisodesEntityQueries
  private val syncErrorQueries = database.showSyncErrorQueries
  private val userShowQueries = database.userShowsEntityQueries
  private val mediaQueries = database.mediaItemEntityQueries
  private val episodeQueries = database.episodeEntityQueries
  private val seasonQueries = database.seasonDetailsEntityQueries
  private val tvDetailsQueries = database.showDetailsEntityQueries

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
    val seasons = seasonQueries.fetchAllSeasons().executeAsList().map { entity ->
      SeasonExport(
        id = entity.id,
        name = entity.name,
        airDate = entity.airDate,
        runtime = entity.runtime,
        number = entity.episodeCount.toInt(),
        seasonNumber = entity.seasonNumber.toInt(),
        showId = entity.showId,
        posterPath = entity.posterPath,
      )
    }
    val episodes = episodeQueries.fetchAllEpisodes().executeAsList().map { entity ->
      EpisodeExport(
        id = entity.id.toInt(),
        name = entity.name,
        airDate = entity.airDate.toString(),
        runtime = entity.runtime,
        number = entity.episodeNumber.toInt(),
        seasonNumber = entity.seasonNumber.toInt(),
        showId = entity.showId,
        stillPath = entity.stillPath,
      )
    }

    return LibraryExport(
      exportedAt = clock.clock.now().toString(),
      shows = shows,
      seasons = seasons,
      episodes = episodes,
    )
  }

  override fun restoreLibrary(encoded: String): Result<List<ShowExport>> = runCatching {
    val library = Json.decodeFromString<LibraryExport>(encoded)
    val addedShows = mutableListOf<ShowExport>()

    database.transactionWithResult {
      library.seasons.forEach { season ->
        seasonQueries.insertSeasonDetails(
          SeasonDetailsEntity = SeasonDetailsEntity(
            id = season.id,
            showId = season.showId,
            name = season.name,
            posterPath = season.posterPath,
            airDate = season.airDate,
            episodeCount = season.number.toLong(),
            voteAverage = 0.0,
            seasonNumber = season.seasonNumber.toLong(),
            runtime = season.runtime,
            overview = "",
          ),
        )
      }
      library.shows.forEach { show ->
        userShowQueries.insertUserShow(
          showId = show.id,
          addedAt = show.addedAt,
          updatedAt = show.addedAt,
          status = show.status,
          notes = null,
          rating = null,
        )
        tvDetailsQueries.insertShowDetails(
          id = show.id,
          totalSeasons = show.seasons.toLong(),
          totalEpisodes = show.episodes.toLong(),
        )
        insertShow(show)

        show.watched.forEach { episode ->
          userEpisodeQueries.upsertUserEpisode(
            episode.id,
            episode.watchedAt,
          )
        }
        addedShows.add(show)
      }
      library.episodes.forEach { episode ->
        episodeQueries.insertEpisode(
          EpisodeEntity(
            id = episode.id.toLong(),
            showId = episode.showId,
            overview = null,
            name = episode.name,
            runtime = episode.runtime,
            episodeNumber = episode.number.toLong(),
            seasonNumber = episode.seasonNumber.toLong(),
            airDate = episode.airDate,
            stillPath = episode.stillPath,
            voteAverage = 0.0,
            voteCount = 0,
          ),
        )
      }
    }

    addedShows
  }

  private fun insertShow(show: ShowExport) {
    mediaQueries.insertMediaItem(
      MediaItemEntity(
        id = show.id,
        posterPath = show.posterPath,
        mediaType = MediaType.TV.value,
        name = show.title,
        originalName = show.title,
        genreIdsJson = show.genres,
        adult = -1,
        originalLanguage = "",
        popularity = 0.0,
        voteAverage = 0.0,
        voteCount = 0,
        overview = "",
        releaseDate = "",
        video = -1,
        firstAirDate = "",
        originCountryJson = "",
        backdropPath = null,
      ),
    )
  }

  private fun getShowExports(): List<ShowExport> = userShowQueries
    .getAllUserShows()
    .executeAsList()
    .map { userShow ->
      val userEpisodes = userEpisodeQueries
        .watchedEpisodesForShow(userShow.showId)
        .executeAsList()
        .map { episode ->
          UserEpisodeExport(
            id = episode.episodeId,
            watchedAt = episode.watchedAt,
          )
        }

      val media = mediaQueries.selectMediaItemByIdAndType(
        id = userShow.showId,
        mediaType = MediaType.TV.value,
      ).executeAsOne()

      val showDetails = tvDetailsQueries
        .fetchShowDetails(userShow.showId)
        .executeAsOne()

      ShowExport(
        id = userShow.showId,
        title = media.name,
        posterPath = media.posterPath,
        status = userShow.status,
        watched = userEpisodes,
        addedAt = userShow.addedAt,
        episodes = showDetails.totalEpisodes.toInt(),
        seasons = showDetails.totalSeasons.toInt(),
        genres = media.genreIdsJson,
      )
    }
}
