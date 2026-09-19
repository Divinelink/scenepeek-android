@file:Suppress("LargeClass")

package com.divinelink.core.database.media.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.commons.extensions.parseOrNull
import com.divinelink.core.commons.provider.ClockProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.MediaItemEntity
import com.divinelink.core.database.SearchHistoryEntity
import com.divinelink.core.database.SeasonEntity
import com.divinelink.core.database.currentEpochSeconds
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.database.season.EpisodeEntity
import com.divinelink.core.database.season.EpisodeRatingEntity
import com.divinelink.core.database.season.SeasonDetailsEntity
import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.details.SeasonProgress
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.details.toMediaItem
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.core.model.jellyseerr.media.SeasonRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.person.Gender
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.model.track.UserEpisodes
import com.divinelink.core.model.track.UserShowStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.datetime.LocalDate

class ProdMediaDao(
  private val database: Database,
  private val dispatcher: DispatcherProvider,
  private val clockProvider: ClockProvider,
) : MediaDao {
  private val episodeQueries = database.userEpisodesEntityQueries
  private val userShowQueries = database.userShowsEntityQueries
  private val mediaQueries = database.mediaItemEntityQueries

  override fun insertMedia(
    media: MediaItem.Media,
    seasons: List<Season>?,
  ) = database.transaction {
    database.mediaItemEntityQueries.insertMediaItem(media.map())
    seasons?.let {
      insertSeasons(media.id, seasons)
    }
  }

  override fun insertShowDetails(show: TV) = database.transaction {
    insertMedia(media = show.toMediaItem())

    show.genres?.map { it.id }?.joinToString()?.let { ids ->
      mediaQueries.updateMediaGenres(
        genreIdsJson = ids,
        id = show.id,
        mediaType = MediaType.TV.value,
      )
    }

    database.showDetailsEntityQueries.insertShowDetails(
      id = show.id,
      totalEpisodes = show.totalEpisodes.toLong(),
      totalSeasons = show.numberOfSeasons.toLong(),
    )
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
    .selectMediaItemByIdAndType(id = media.mediaId, mediaType = media.mediaType.value)
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
            id = favorite.id,
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.releaseDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            saved = true,
          )
        } else if (MediaType.from(favorite.mediaType) == MediaType.TV) {
          MediaItem.Media.TV(
            id = favorite.id,
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.firstAirDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            saved = true,
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
            id = favorite.id,
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.releaseDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            saved = true,
          )
        } else if (MediaType.from(favorite.mediaType) == MediaType.TV) {
          MediaItem.Media.TV(
            id = favorite.id,
            name = favorite.name,
            posterPath = favorite.posterPath,
            backdropPath = favorite.backdropPath,
            releaseDate = favorite.firstAirDate!!,
            voteAverage = favorite.voteAverage,
            voteCount = favorite.voteCount.toInt(),
            overview = favorite.overview,
            popularity = favorite.popularity,
            saved = true,
          )
        } else {
          null
        }
      }
    }

  override fun fetchFavoritePeople(): Flow<List<MediaItem.Person>> = database
    .favoriteMediaEntityQueries
    .getFavoritePeople()
    .asFlow()
    .mapToList(dispatcher.io)
    .distinctUntilChanged()
    .map { favorites ->
      favorites.map { favorite ->
        MediaItem.Person(
          id = favorite.id,
          name = favorite.name,
          profilePath = favorite.profilePath,
          gender = Gender.from(favorite.gender.toInt()),
          knownForDepartment = favorite.knownForDepartment,
          role = emptyList(),
          saved = true,
        )
      }
    }

  override fun getFavoriteMediaIds(mediaType: MediaType): Flow<List<Long>> = database
    .favoriteMediaEntityQueries
    .getFavoriteMediaIds(mediaType.value)
    .asFlow()
    .mapToList(dispatcher.io)
    .map { result ->
      result.map { it.mediaId }
    }

  override fun addToFavorites(
    mediaId: Long,
    mediaType: MediaType,
  ) = database.transaction {
    database.favoriteMediaEntityQueries.addToFavorites(
      mediaId = mediaId,
      mediaType = mediaType.value,
      favoritedAt = clockProvider.clock.currentEpochSeconds(),
    )
  }

  override fun removeFromFavorites(
    mediaId: Long,
    mediaType: MediaType,
  ) = database.transaction {
    database.favoriteMediaEntityQueries.removeFromFavorites(
      mediaId,
      mediaType.value,
    )
  }

  override fun isMediaFavorite(
    mediaId: Long,
    mediaType: MediaType,
  ) = database.transactionWithResult {
    database.favoriteMediaEntityQueries.isMediaFavorite(
      mediaId = mediaId,
      mediaType = mediaType.value,
    ).executeAsOneOrNull() == true
  }

  override fun fetchSeasons(id: Long): Flow<List<Season>> = database.transactionWithResult {
    database
      .seasonEntityQueries
      .fetchSeasons(id)
      .asFlow()
      .mapToList(dispatcher.io)
      .map {
        it.map { entity ->
          Season(
            id = entity.id,
            name = entity.name,
            overview = entity.overview,
            posterPath = entity.posterPath,
            airDate = LocalDate.parse(entity.airDate),
            episodeCount = entity.episodeCount.toInt(),
            voteAverage = entity.voteAverage,
            seasonNumber = entity.seasonNumber.toInt(),
            status = JellyseerrStatus.from(entity.status),
          )
        }
      }
  }

  override fun fetchSeason(
    showId: Long,
    seasonNumber: Int,
  ): Flow<Season> = database
    .transactionWithResult {
      database
        .seasonEntityQueries
        .fetchSeason(mediaId = showId, seasonNumber = seasonNumber.toLong())
        .asFlow()
        .mapToOne(context = dispatcher.io)
        .map { entity ->
          Season(
            id = entity.id,
            name = entity.name,
            overview = entity.overview,
            posterPath = entity.posterPath,
            airDate = LocalDate.parse(entity.airDate),
            episodeCount = entity.episodeCount.toInt(),
            voteAverage = entity.voteAverage,
            seasonNumber = entity.seasonNumber.toInt(),
            status = JellyseerrStatus.from(entity.status),
          )
        }
    }

  override fun insertSeasons(
    id: Long,
    seasons: List<Season>,
  ) = database.transaction {
    seasons.forEach { season ->
      database.seasonEntityQueries.insertSeason(
        SeasonEntity = SeasonEntity(
          mediaId = id,
          id = season.id,
          overview = season.overview,
          name = season.name,
          posterPath = season.posterPath,
          airDate = season.airDate.toString(),
          episodeCount = season.episodeCount.toLong(),
          voteAverage = season.voteAverage,
          seasonNumber = season.seasonNumber.toLong(),
          status = season.status?.value,
        ),
      )
    }
  }

  override fun updateSeasonStatus(
    mediaId: Long,
    seasons: List<SeasonRequest>,
    override: Boolean,
  ) = database.transaction {
    if (override) {
      database.seasonEntityQueries.clearAllSeasonStatuses(mediaId)
    }

    seasons.forEach { season ->
      database.seasonEntityQueries.updateSeasonStatus(
        mediaId = mediaId,
        status = season.status.value,
        seasonNumber = season.seasonNumber.toLong(),
      )
    }
  }

  override fun fetchGenres(
    mediaType: MediaType,
    locale: String,
  ): Flow<List<Genre>> = database
    .genreEntityQueries
    .fetchGenres(mediaType = mediaType.value, locale = locale)
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
    locale: String,
  ) = database.transaction {
    genres.forEach { genre ->
      database.genreEntityQueries.insertGenre(
        id = genre.id.toLong(),
        name = genre.name,
        mediaType = mediaType.value,
        locale = locale,
      )
    }
  }

  override fun insertEpisodes(episodes: List<Episode>) = database.transaction {
    episodes.forEach { episode ->
      database.episodeEntityQueries.insertEpisode(
        EpisodeEntity(
          id = episode.id.toLong(),
          showId = episode.showId,
          overview = episode.overview,
          name = episode.name,
          runtime = episode.runtime,
          episodeNumber = episode.number.toLong(),
          seasonNumber = episode.seasonNumber.toLong(),
          airDate = episode.airDate?.toString(),
          stillPath = episode.stillPath,
          voteAverage = episode.voteAverage?.toDouble() ?: 0.0,
          voteCount = episode.voteCount?.toLong() ?: 0,
        ),
      )

      episode.accountRating?.toLong()?.let { rating ->
        database.episodeRatingEntityQueries.insertEpisodeRating(
          EpisodeRatingEntity(
            number = episode.number.toLong(),
            showId = episode.showId,
            season = episode.seasonNumber.toLong(),
            rating = rating,
          ),
        )
      }
    }
  }

  override fun fetchEpisode(
    showId: Long,
    episodeNumber: Int,
    seasonNumber: Int,
  ): Flow<Episode> = database
    .transactionWithResult {
      val accountRating = database.episodeRatingEntityQueries.fetchEpisodeRating(
        number = episodeNumber.toLong(),
        showId = showId,
        season = seasonNumber.toLong(),
      )
        .asFlow()
        .mapToOneOrNull(dispatcher.io)
        .map { it?.rating }

      val episode = database
        .episodeEntityQueries
        .fetchEpisode(
          showId = showId,
          seasonNumber = seasonNumber.toLong(),
          episodeNumber = episodeNumber.toLong(),
        )
        .asFlow()
        .mapToOne(dispatcher.io)

      combine(
        episode,
        accountRating,
      ) { episode, rating ->
        episode.map(rating?.toInt())
      }
    }

  override fun fetchEpisodes(
    showId: Long,
    season: Int,
  ): Flow<List<Episode>> = database
    .transactionWithResult {
      val ratingsFlow = database.episodeRatingEntityQueries.fetchEpisodesRatings(
        showId = showId,
        season = season.toLong(),
      )
        .asFlow()
        .mapToList(dispatcher.io)

      val episodesFlow = database
        .episodeEntityQueries
        .fetchEpisodes(
          showId = showId,
          seasonNumber = season.toLong(),
        )
        .asFlow()
        .mapToList(dispatcher.io)

      combine(
        episodesFlow,
        ratingsFlow,
      ) { episodes, ratings ->
        episodes.map { episode ->
          episode.map(
            accountRating = ratings.find {
              it.number == episode.episodeNumber
            }?.rating?.toInt(),
          )
        }
      }
    }

  override fun insertSeasonDetails(
    seasonDetails: SeasonDetails,
    showId: Long,
    seasonNumber: Int,
  ) = database
    .transaction {
      database.seasonDetailsEntityQueries.insertSeasonDetails(
        SeasonDetailsEntity = SeasonDetailsEntity(
          id = seasonDetails.id.toLong(),
          showId = showId,
          name = seasonDetails.name,
          overview = seasonDetails.overview,
          posterPath = seasonDetails.posterPath,
          airDate = seasonDetails.airDate.toString(),
          episodeCount = seasonDetails.episodeCount.toLong(),
          voteAverage = seasonDetails.voteAverage,
          seasonNumber = seasonNumber.toLong(),
          runtime = seasonDetails.totalRuntime,
        ),
      )
    }

  override fun fetchSeasonDetails(
    showId: Long,
    season: Int,
  ): Flow<SeasonDetailsEntity?> = database
    .transactionWithResult {
      database
        .seasonDetailsEntityQueries
        .fetchSeasonDetails(
          showId = showId,
          seasonNumber = season.toLong(),
        )
        .asFlow()
        .mapToOneOrNull(dispatcher.io)
    }

  override fun fetchSeasonEpisodesCount(
    season: Int,
    showId: Long,
  ): List<Int> = database
    .transactionWithResult {
      database
        .episodeEntityQueries
        .countSeasonEpisodes(
          showId = showId,
          seasonNumber = season.toLong(),
        )
        .executeAsList()
        .map { it.toInt() }
    }

  override fun insertEpisodeRating(
    showId: Long,
    season: Int,
    number: Int,
    rating: Int,
  ) = database.transaction {
    database.episodeRatingEntityQueries.insertEpisodeRating(
      EpisodeRatingEntity(
        number = number.toLong(),
        showId = showId,
        season = season.toLong(),
        rating = rating.toLong(),
      ),
    )
  }

  override fun deleteEpisodeRating(
    showId: Long,
    season: Int,
    number: Int,
  ) = database.transaction {
    database.episodeRatingEntityQueries.deleteEpisodeRating(
      number = number.toLong(),
      showId = showId,
      season = season.toLong(),
    )
  }

  override fun clearAllEpisodeRatings() = database.transaction {
    database.episodeRatingEntityQueries.clearAllEpisodeRatings()
  }

  override fun addToSearchHistory(media: MediaReference) = with(database) {
    transaction {
      searchHistoryEntityQueries.insertSearchQuery(
        mediaId = media.mediaId,
        mediaType = media.mediaType.value,
        timestamp = clockProvider.clock.currentEpochSeconds().toLong(),
      )
    }
  }

  override fun clearSearchHistory() = with(database) {
    transaction { searchHistoryEntityQueries.clearSearchHistory() }
  }

  override fun removeFromSearchHistory(media: MediaReference) {
    database.searchHistoryEntityQueries.removeItemFromSearch(
      mediaId = media.mediaId,
      mediaType = media.mediaType.value,
    )
  }

  override fun fetchSearchHistory(): Flow<List<SearchHistoryEntity>> = with(database) {
    transactionWithResult {
      searchHistoryEntityQueries
        .fetchSearchHistory()
        .asFlow()
        .mapToList(dispatcher.io)
    }
  }

  override fun selectByIds(ids: List<Long>): Flow<List<MediaItem.Media>> = with(database) {
    transactionWithResult {
      mediaItemEntityQueries.selectByIds(ids).asFlow().mapToList(dispatcher.io).map { list ->
        list.mapNotNull { entity -> entity.map() }
      }
    }
  }

  override fun markSeasonAsWatched(
    episodeIds: List<Long>,
    watchedAt: String,
  ) {
    if (episodeIds.isEmpty()) return

    database.transaction {
      episodeIds.forEach { id ->
        episodeQueries.upsertUserEpisode(episodeId = id, watchedAt = watchedAt)
      }
    }
  }

  override fun unmarkSeasonAsWatched(episodeIds: List<Long>) {
    if (episodeIds.isEmpty()) return

    database.transaction {
      episodeIds.forEach { id ->
        episodeQueries.deleteUserEpisode(episodeId = id)
      }
    }
  }

  override fun getSeasonsProgress(showId: Long): Flow<List<SeasonProgress>> = episodeQueries
    .seasonsProgress(showId = showId)
    .asFlow()
    .mapToList(dispatcher.io)
    .distinctUntilChanged()
    .map { result ->
      result.map { season ->
        SeasonProgress(
          seasonNumber = season.seasonNumber.toInt(),
          watchedCount = season.watchedCount.toInt(),
        )
      }
    }

  override fun getWatchedEpisodesForSeason(
    showId: Long,
    seasonNumber: Int,
  ): Flow<List<Long>> = episodeQueries
    .watchedEpisodesForSeason(showId = showId, seasonNumber = seasonNumber.toLong())
    .asFlow()
    .mapToList(dispatcher.io)
    .distinctUntilChanged()

  override fun addShow(showId: Long) = database.transaction {
    userShowQueries.upsertUserShow(
      showId = showId,
      addedAt = clockProvider.clock.currentEpochSeconds(),
      updatedAt = clockProvider.clock.currentEpochSeconds(),
      status = UserShowStatus.WATCHING.value,
      notes = null,
      rating = null,
    )
  }

  override fun deleteShow(showId: Long) = database.transaction {
    userShowQueries.deleteUserShow(showId)
  }

  override fun getUserShowStatus(showId: Long): Flow<UserShowStatus?> = userShowQueries
    .getUserShowStatus(showId)
    .asFlow()
    .mapToOneOrNull(dispatcher.io)
    .map { entity ->
      UserShowStatus.from(entity?.status)
    }

  override fun getNextUpEpisode(showId: Long): Flow<Episode> = userShowQueries
    .nextUpEpisode(showId = showId)
    .asFlow()
    .mapToOneOrNull(dispatcher.io)
    .mapNotNull { entity ->
      if (entity == null) return@mapNotNull null
      Episode(
        id = entity.episodeId.toInt(),
        name = entity.episodeName,
        airDate = LocalDate.parseOrNull(entity.airDate),
        overview = null,
        runtime = null,
        number = entity.episodeNumber.toInt(),
        seasonNumber = entity.seasonNumber.toInt(),
        showId = showId,
        stillPath = entity.thumbnailPath,
        voteAverage = null,
        voteCount = null,
        crew = emptyList(),
        guestStars = emptyList(),
        accountRating = null,
      )
    }

  override fun getNextUpEpisodes(): Flow<UserEpisodes> = userShowQueries
    .getAllUpNextEpisodes()
    .asFlow()
    .mapToList(dispatcher.io)
    .map { entities ->
      val today = clockProvider.now

      val available = mutableListOf<NextUpEpisode>()
      val upcoming = mutableListOf<NextUpEpisode>()

      for (entity in entities) {
        val date = LocalDate.parseOrNull(entity.airDate) ?: continue
        when {
          date <= today -> available += entity.map()
          date > today -> upcoming += entity.map()
        }
      }

      UserEpisodes(
        available = available,
        upcoming = upcoming.sortedBy { it.episode.airDate },
      )
    }
}
