package com.divinelink.core.database.media.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.database.SeasonEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.database.season.EpisodeEntity
import com.divinelink.core.database.season.SeasonDetailsEntity
import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

class ProdMediaDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
  private val clock: Clock,
) : MediaDao {

  override fun insertMedia(
    media: MediaItem.Media,
    seasons: List<Season>?,
  ) = database.transaction {
    database.mediaItemEntityQueries.insertMediaItem(media.map())
    seasons?.let {
      insertSeasons(media.id, seasons)
    }
  }

  override fun insertMediaList(media: List<MediaItem.Media>) {
    media.forEach {
      insertMedia(
        media = it,
        seasons = null,
      )
    }
  }

  override fun insertMediaEntities(media: List<MediaItemEntity>) = database.transaction {
    media.forEach { database.mediaItemEntityQueries.insertMediaItem(it) }
  }

  override fun fetchMedia(media: MediaReference): MediaItem.Media? = database
    .mediaItemEntityQueries
    .selectMediaItemByIdAndType(id = media.mediaId.toLong(), mediaType = media.mediaType.value)
    .executeAsOneOrNull()
    ?.map()

  override fun fetchAllFavorites(): Flow<List<MediaItem.Media>> = database
    .favoriteMediaEntityQueries
    .getAllFavorites()
    .asFlow()
    .mapToList(dispatcher.io)
    .distinctUntilChanged()
    .map { favorites ->
      favorites.mapNotNull { favorite ->
        if (MediaType.from(favorite.mediaType) == MediaType.MOVIE) {
          MediaItem.Media.Movie(
            id = favorite.id.toInt(),
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.releaseDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            isFavorite = true,
          )
        } else if (MediaType.from(favorite.mediaType) == MediaType.TV) {
          MediaItem.Media.TV(
            id = favorite.id.toInt(),
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.firstAirDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            isFavorite = true,
          )
        } else {
          null
        }
      }
    }

  override fun fetchFavorites(mediaType: MediaType): Flow<List<MediaItem.Media>> = database
    .favoriteMediaEntityQueries
    .getAllFavoritesByMediaType(mediaType.value)
    .asFlow()
    .mapToList(dispatcher.io)
    .distinctUntilChanged()
    .map { favorites ->
      favorites.mapNotNull { favorite ->
        if (MediaType.from(favorite.mediaType) == MediaType.MOVIE) {
          MediaItem.Media.Movie(
            id = favorite.id.toInt(),
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.releaseDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            isFavorite = true,
          )
        } else if (MediaType.from(favorite.mediaType) == MediaType.TV) {
          MediaItem.Media.TV(
            id = favorite.id.toInt(),
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.firstAirDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            isFavorite = true,
          )
        } else {
          null
        }
      }
    }

  override fun getFavoriteMediaIds(mediaType: MediaType): Flow<List<Int>> = database
    .favoriteMediaEntityQueries
    .getFavoriteMediaIds(mediaType.value)
    .asFlow()
    .mapToList(dispatcher.io)
    .map { result ->
      result.map { it.mediaId.toInt() }
    }

  override fun addToFavorites(
    mediaId: Int,
    mediaType: MediaType,
  ) = database.transaction {
    database.favoriteMediaEntityQueries.addToFavorites(
      mediaId = mediaId.toLong(),
      mediaType = mediaType.value,
      favoritedAt = clock.currentEpochSeconds(),
    )
  }

  override fun removeFromFavorites(
    mediaId: Int,
    mediaType: MediaType,
  ) = database.transaction {
    database.favoriteMediaEntityQueries.removeFromFavorites(
      mediaId.toLong(),
      mediaType.value,
    )
  }

  override fun isMediaFavorite(
    mediaId: Int,
    mediaType: MediaType,
  ) = database.transactionWithResult {
    database.favoriteMediaEntityQueries.isMediaFavorite(
      mediaId = mediaId.toLong(),
      mediaType = mediaType.value,
    ).executeAsOneOrNull() == true
  }

  override fun fetchSeasons(id: Int): Flow<List<Season>> = database.transactionWithResult {
    database
      .seasonEntityQueries
      .fetchSeasons(id.toLong())
      .asFlow()
      .mapToList(dispatcher.io)
      .map {
        it.map { entity ->
          Season(
            id = entity.id.toInt(),
            name = entity.name,
            overview = entity.overview,
            posterPath = entity.posterPath,
            airDate = entity.airDate,
            episodeCount = entity.episodeCount.toInt(),
            voteAverage = entity.voteAverage,
            seasonNumber = entity.seasonNumber.toInt(),
            status = JellyseerrStatus.from(entity.status),
          )
        }
      }
  }

  override fun fetchSeason(
    showId: Int,
    seasonNumber: Int,
  ): Flow<Season> = database
    .transactionWithResult {
      database
        .seasonEntityQueries
        .fetchSeason(mediaId = showId.toLong(), seasonNumber = seasonNumber.toLong())
        .asFlow()
        .mapToOne(context = dispatcher.io)
        .map { entity ->
          Season(
            id = entity.id.toInt(),
            name = entity.name,
            overview = entity.overview,
            posterPath = entity.posterPath,
            airDate = entity.airDate,
            episodeCount = entity.episodeCount.toInt(),
            voteAverage = entity.voteAverage,
            seasonNumber = entity.seasonNumber.toInt(),
            status = JellyseerrStatus.from(entity.status),
          )
        }
    }

  override fun insertSeasons(
    id: Int,
    seasons: List<Season>,
  ) = database.transaction {
    seasons.forEach { season ->
      database.seasonEntityQueries.insertSeason(
        SeasonEntity = SeasonEntity(
          mediaId = id.toLong(),
          id = season.id.toLong(),
          overview = season.overview,
          name = season.name,
          posterPath = season.posterPath,
          airDate = season.airDate,
          episodeCount = season.episodeCount.toLong(),
          voteAverage = season.voteAverage,
          seasonNumber = season.seasonNumber.toLong(),
          status = season.status?.value,
        ),
      )
    }
  }

  override fun updateSeasonStatus(
    mediaId: Int,
    seasons: List<SeasonRequest>,
    override: Boolean,
  ) = database.transaction {
    if (override) {
      database.seasonEntityQueries.clearAllSeasonStatuses(mediaId.toLong())
    }

    seasons.forEach { season ->
      database.seasonEntityQueries.updateSeasonStatus(
        mediaId = mediaId.toLong(),
        status = season.status.value,
        seasonNumber = season.seasonNumber.toLong(),
      )
    }
  }

  override fun fetchGenres(mediaType: MediaType): Flow<List<Genre>> = database
    .genreEntityQueries
    .fetchGenres(mediaType = mediaType.value, locale = "en")
    .asFlow()
    .mapToList(dispatcher.io)
    .map { list ->
      list.map { entity ->
        Genre(
          id = entity.id.toInt(),
          name = entity.name,
        )
      }
    }

  override fun insertGenres(
    mediaType: MediaType,
    genres: List<Genre>,
  ) = database.transaction {
    genres.forEach { genre ->
      database.genreEntityQueries.insertGenre(
        id = genre.id.toLong(),
        name = genre.name,
        mediaType = mediaType.value,
        locale = "en",
      )
    }
  }

  override fun insertEpisodes(episodes: List<Episode>) = database.transaction {
    episodes.forEach { episode ->
      database.episodeEntityQueries.insertEpisode(
        EpisodeEntity(
          id = episode.id.toLong(),
          showId = episode.showId.toLong(),
          overview = episode.overview,
          name = episode.name,
          runtime = episode.runtime,
          episodeNumber = episode.number.toLong(),
          seasonNumber = episode.seasonNumber.toLong(),
          airDate = episode.airDate,
          stillPath = episode.stillPath,
          voteAverage = episode.voteAverage?.toDouble() ?: 0.0,
          voteCount = episode.voteCount?.toLong() ?: 0,
        ),
      )
    }
  }

  override fun fetchEpisode(
    showId: Int,
    episodeNumber: Int,
    seasonNumber: Int,
  ): Episode = database
    .transactionWithResult {
      database
        .episodeEntityQueries
        .fetchEpisode(
          showId = showId.toLong(),
          seasonNumber = seasonNumber.toLong(),
          episodeNumber = episodeNumber.toLong(),
        )
        .executeAsOne()
        .map()
    }

  override fun fetchEpisodes(
    showId: Int,
    season: Int,
  ): Flow<List<Episode>> = database
    .transactionWithResult {
      database
        .episodeEntityQueries
        .fetchEpisodes(
          showId = showId.toLong(),
          seasonNumber = season.toLong(),
        )
        .asFlow()
        .mapToList(dispatcher.io)
        .map { entities ->
          entities.map { it.map() }
        }
    }

  override fun insertSeasonDetails(
    seasonDetails: SeasonDetails,
    showId: Int,
    seasonNumber: Int,
  ) = database
    .transaction {
      database.seasonDetailsEntityQueries.insertSeasonDetails(
        SeasonDetailsEntity = SeasonDetailsEntity(
          id = seasonDetails.id.toLong(),
          showId = showId.toLong(),
          name = seasonDetails.name,
          overview = seasonDetails.overview,
          posterPath = seasonDetails.posterPath,
          airDate = seasonDetails.airDate,
          episodeCount = seasonDetails.episodeCount.toLong(),
          voteAverage = seasonDetails.voteAverage,
          seasonNumber = seasonNumber.toLong(),
          runtime = seasonDetails.totalRuntime,
        ),
      )
    }

  override fun fetchSeasonDetails(
    season: Int,
    showId: Int,
  ): Flow<SeasonDetailsEntity?> = database
    .transactionWithResult {
      database
        .seasonDetailsEntityQueries
        .fetchSeasonDetails(
          showId = showId.toLong(),
          seasonNumber = season.toLong(),
        )
        .asFlow()
        .mapToOneOrNull(dispatcher.io)
    }

  override fun fetchSeasonEpisodesCount(
    season: Int,
    showId: Int,
  ): List<Int> = database
    .transactionWithResult {
      database
        .episodeEntityQueries
        .countSeasonEpisodes(
          showId = showId.toLong(),
          seasonNumber = season.toLong(),
        )
        .executeAsList()
        .map { it.toInt() }
    }
}
