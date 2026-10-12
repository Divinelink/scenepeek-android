package com.divinelink.core.database.track

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.database.cast.PersonEntity
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.database.season.EpisodeEntity
import com.divinelink.core.database.season.SeasonDetailsEntity
import com.divinelink.core.database.show.ShowSyncError
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.track.UserShowStatus
import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.model.track.backup.EpisodeExport
import com.divinelink.core.model.track.backup.LibraryExport
import com.divinelink.core.model.track.backup.MediaExport
import com.divinelink.core.model.track.backup.PersonExport
import com.divinelink.core.model.track.backup.RestoreProcess
import com.divinelink.core.model.track.backup.SavedExport
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
  private val personQueries = database.personEntityQueries
  private val savedQueries = database.savedMediaEntityQueries

  override fun getUserShows(): Flow<List<Long>> = userShowQueries
    .getAllUserShows()
    .asFlow()
    .mapToList(dispatcher.io)
    .map { entity ->
      entity.map { show -> show.showId }
    }

  override fun updateShowStatus(
    showId: Long,
    status: UserShowStatus,
  ): Result<Unit> = runCatching {
    database.transaction {
      userShowQueries.updateStatus(
        showId = showId,
        status = status.value,
      )
    }
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
    userEpisodeQueries.insertUserEpisode(
      episodeId = episodeId,
      watchedAt = watchedAt,
    )
  }

  override fun unmarkEpisodeAsWatched(episodeId: Long) = database.transaction {
    userEpisodeQueries.deleteUserEpisode(
      episodeId = episodeId,
    )
  }

  override fun observeLatestWatchHistory(): Flow<List<WatchedEpisode>> = userShowQueries
    .getWatchHistory(null, Long.MAX_VALUE)
    .asFlow()
    .mapToList(dispatcher.io)
    .map { entities ->
      entities.map { episode ->
        episode.map()
      }
    }

  override fun getWatchHistory(
    lastId: Long,
    lastWatchedAt: String?,
  ): List<WatchedEpisode> =
    userShowQueries
      .getWatchHistory(lastId = lastId, lastWatchedAt = lastWatchedAt)
      .executeAsList()
      .map { entities ->
        entities.map()
      }

  override fun exportLibrary(): LibraryExport {
    val shows = getShowExports()
    val seasons = getSeasonExports()
    val episodes = getEpisodeExports()
    val saved = getSavedExport()

    return LibraryExport(
      exportedAt = clock.clock.now().toString(),
      shows = shows,
      seasons = seasons,
      episodes = episodes,
      saved = saved.first,
      media = saved.second,
      people = saved.third,
    )
  }

  override fun restoreLibrary(encoded: String): Result<RestoreProcess> = runCatching {
    val library = Json.decodeFromString<LibraryExport>(encoded)
    var showsRestored = 0
    var episodesRestored = 0
    var seasonsRestored = 0
    var savedRestored = 0

    database.transaction {
      savedRestored = restoreSaved(library)
      seasonsRestored = restoreSeasons(library)
      showsRestored = restoreShows(library)
      episodesRestored = restoreEpisodes(library)
    }

    RestoreProcess(
      shows = showsRestored,
      seasons = seasonsRestored,
      episodes = episodesRestored,
      saved = savedRestored,
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

  /** Helper restore library functions **/
  private fun restoreSeasons(library: LibraryExport): Int {
    var seasonsRestored = 0

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
      seasonsRestored += 1
    }
    return seasonsRestored
  }

  private fun restoreShows(library: LibraryExport): Int {
    var totalRestored = 0

    library.shows.forEach { show ->
      userShowQueries.upsertUserShow(
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
        episode.watchedAt?.let { watchedAt ->
          userEpisodeQueries.insertUserEpisode(
            episode.id,
            watchedAt,
          )
        }
      }
      totalRestored += 1
    }
    return totalRestored
  }

  private fun restoreEpisodes(library: LibraryExport): Int {
    var totalRestored = 0

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
      totalRestored += 1
    }
    return totalRestored
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

  private fun getSavedExport(): Triple<SavedExport, List<MediaExport>, List<PersonExport>> {
    val tvIds = mutableSetOf<Long>()
    val movieIds = mutableSetOf<Long>()
    val peopleIds = mutableSetOf<Long>()
    val media = mutableListOf<MediaExport>()
    val people = mutableListOf<PersonExport>()

    database
      .savedMediaEntityQueries
      .getAllFavorites()
      .executeAsList()
      .forEach { saved ->
        when (MediaType.from(saved.mediaType)) {
          MediaType.TV -> {
            tvIds.add(saved.id)
            media.add(saved.map())
          }
          MediaType.MOVIE -> {
            movieIds.add(saved.id)
            media.add(saved.map())
          }
          MediaType.PERSON,
          MediaType.UNKNOWN,
            -> Unit
        }
      }

    database.savedMediaEntityQueries.getFavoritePeople()
      .executeAsList()
      .forEach { saved ->
        peopleIds.add(saved.id)
        people.add(saved.map())
      }

    return Triple(
      first = SavedExport(
        movies = movieIds,
        tv = tvIds,
        people = peopleIds,
      ),
      second = media,
      third = people,
    )
  }

  private fun getSeasonExports() = seasonQueries
    .fetchAllSeasons()
    .executeAsList()
    .map { entity ->
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

  private fun getEpisodeExports() = episodeQueries
    .fetchAllEpisodes()
    .executeAsList()
    .map { entity ->
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

  private fun restoreSaved(library: LibraryExport): Int {
    var totalRestored = 0

    library.people?.forEach { person ->
      personQueries.insertPerson(
        PersonEntity = PersonEntity(
          id = person.id,
          name = person.name,
          originalName = person.name,
          profilePath = person.profilePath,
          knownForDepartment = person.knownForDepartment,
          gender = person.gender.toLong(),
        ),
      )
      savedQueries.addToFavorites(
        mediaId = person.id,
        mediaType = MediaType.PERSON.value,
        savedAt = person.savedAt,
      )
      totalRestored += 1
    }

    library.media?.forEach { media ->
      mediaQueries.insertMediaItem(
        MediaItemEntity(
          id = media.id,
          posterPath = media.posterPath,
          mediaType = media.type,
          name = media.title,
          originalName = media.title,
          genreIdsJson = "",
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
      savedQueries.addToFavorites(
        mediaId = media.id,
        mediaType = media.type,
        savedAt = media.savedAt,
      )
      totalRestored += 1
    }

    return totalRestored
  }
  /** End region restore library functions **/
}
