package com.divinelink.core.data.media.repository

import com.divinelink.core.commons.data
import com.divinelink.core.commons.domain.DispatcherProvider
import com.divinelink.core.database.Database
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.database.media.mapper.map
import com.divinelink.core.database.person.PersonDao
import com.divinelink.core.model.Genre
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.home.MediaListRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.search.MultiSearch
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.network.Resource
import com.divinelink.core.network.media.mapper.details.map
import com.divinelink.core.network.media.mapper.map
import com.divinelink.core.network.media.model.GenresListResponse
import com.divinelink.core.network.media.model.movie.map
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.media.model.search.multi.mapper.map
import com.divinelink.core.network.media.model.tv.map
import com.divinelink.core.network.media.service.MediaService
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProdMediaRepository(
  private val remote: MediaService,
  private val dao: MediaDao,
  private val database: Database,
  private val personDao: PersonDao,
  private val dispatcher: DispatcherProvider,
) : MediaRepository {

  override suspend fun fetchTrending(page: Int): Flow<Result<PaginationData<MediaItem>>> = combine(
    flowOf(remote.fetchMediaLists(MediaListRequest.TrendingAll, page)),
    dao.getFavoriteMediaIds(MediaType.MOVIE),
    dao.getFavoriteMediaIds(MediaType.TV),
  ) { response, favoriteMovieIds, favoriteTvShowIds ->
    val data = response.data.map()
    val moviesIdsSet = favoriteMovieIds.toSet()
    val tvShowIdsSet = favoriteTvShowIds.toSet()

    val updatedMedia = data.searchList.map { media ->
      when (media.mediaType) {
        MediaType.TV -> (media as MediaItem.Media.TV).copy(
          isFavorite = media.id in tvShowIdsSet,
        )
        MediaType.MOVIE -> (media as MediaItem.Media.Movie).copy(
          isFavorite = media.id in moviesIdsSet,
        )
        else -> media
      }
    }

    val paginationData = PaginationData(
      page = page,
      totalPages = data.totalPages,
      totalResults = data.totalPages, // todo
      list = updatedMedia,
    )
    Result.success(paginationData)
  }

  override fun fetchMediaLists(
    request: MediaListRequest,
    page: Int,
  ): Flow<Result<PaginationData<MediaItem>>> = flow {
    val mediaType = when (request) {
      is MediaListRequest.Popular -> request.mediaType
      is MediaListRequest.TopRated -> request.mediaType
      is MediaListRequest.Upcoming -> request.mediaType
      MediaListRequest.TrendingAll -> null
    }
    mediaType ?: return@flow

    val response = remote.fetchMediaLists(request, page).getOrThrow()

    val paginationData = PaginationData(
      page = response.page,
      totalPages = response.totalPages,
      totalResults = response.totalResults,
      list = response.results.map(mediaType.value),
    )

    dao
      .getFavoriteMediaIds(mediaType)
      .map { favoriteIds ->
        val favoriteIdsSet = favoriteIds.toSet()

        val updatedMedia = paginationData.list.map { media ->
          when (media.mediaType) {
            MediaType.TV -> (media as MediaItem.Media.TV).copy(
              isFavorite = media.id in favoriteIdsSet,
            )
            MediaType.MOVIE -> (media as MediaItem.Media.Movie).copy(
              isFavorite = media.id in favoriteIdsSet,
            )
            else -> media
          }
        }

        paginationData.copy(list = updatedMedia)
      }
      .collect { resultWithFavorites ->
        emit(Result.success(resultWithFavorites))
      }
  }

  override fun discoverMovies(
    page: Int,
    sortOption: SortOption,
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>> = combine(
    remote.fetchDiscoverMovies(page, sortOption, filters),
    dao.getFavoriteMediaIds(MediaType.MOVIE),
  ) { response, favoriteIds ->
    val data = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedMovies = data.list.map { media ->
      (media as MediaItem.Media.Movie).copy(isFavorite = media.id in favoriteSet)
    }

    Result.success(
      UserDataResponse(
        data = updatedMovies,
        page = page,
        totalResults = data.totalResults,
        type = MediaType.MOVIE,
        canFetchMore = page < data.totalPages,
      ),
    )
  }

  override fun discoverTvShows(
    page: Int,
    sortOption: SortOption,
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>> = combine(
    remote.fetchDiscoverTv(page, sortOption, filters),
    dao.getFavoriteMediaIds(MediaType.TV),
  ) { response, favoriteIds ->
    val data = response.map()
    val favoriteSet = favoriteIds.toSet()

    val updatedTv = data.list.map { media ->
      (media as MediaItem.Media.TV).copy(isFavorite = media.id in favoriteSet)
    }

    Result.success(
      UserDataResponse(
        data = updatedTv,
        page = page,
        totalResults = data.totalResults,
        type = MediaType.TV,
        canFetchMore = page < data.totalPages,
      ),
    )
  }

  override fun fetchFavorites(): Flow<MediaListResult> = dao
    .fetchAllFavorites()
    .map { favorites ->
      Result.success(favorites)
    }

  override fun fetchFavorites(mediaType: MediaType): Flow<Result<PaginationData<MediaItem>>> = dao
    .fetchFavorites(mediaType)
    .map { favorites ->
      Result.success(
        PaginationData(
          page = 1,
          totalPages = 1,
          totalResults = favorites.size,
          list = favorites,
        ),
      )
    }

  override fun fetchSearchMovies(
    mediaType: MediaType,
    request: SearchRequestApi,
  ): Flow<Result<MultiSearch>> = flow {
    val response = remote.fetchSearchMovies(mediaType, request).getOrThrow()

    val baseResult = MultiSearch(
      searchList = response.map(mediaType.value).searchList,
      totalPages = response.totalPages,
      page = response.page,
    )

    dao.getFavoriteMediaIds(mediaType)
      .map { favoriteIds ->
        val favoriteIdsSet = favoriteIds.toSet()

        val updatedMedia = baseResult.searchList.map { media ->
          when (media.mediaType) {
            MediaType.TV -> (media as MediaItem.Media.TV).copy(
              isFavorite = media.id in favoriteIdsSet,
            )
            MediaType.MOVIE -> (media as MediaItem.Media.Movie).copy(
              isFavorite = media.id in favoriteIdsSet,
            )
            else -> media
          }
        }

        baseResult.copy(searchList = updatedMedia)
      }
      .collect { resultWithFavorites ->
        emit(Result.success(resultWithFavorites))
      }
  }.flowOn(dispatcher.io)

  override fun fetchMultiInfo(requestApi: MultiSearchRequestApi): Flow<Result<MultiSearch>> =
    combine(
      remote.fetchMultiInfo(requestApi),
      dao.getFavoriteMediaIds(MediaType.MOVIE),
      dao.getFavoriteMediaIds(MediaType.TV),
    ) { response, favoriteMovieIds, favoriteTvShowIds ->
      val data = response.map()
      val moviesIdsSet = favoriteMovieIds.toSet()
      val tvShowIdsSet = favoriteTvShowIds.toSet()

      val updatedMedia = data.searchList.map { media ->
        when (media.mediaType) {
          MediaType.TV -> (media as MediaItem.Media.TV).copy(
            isFavorite = media.id in tvShowIdsSet,
          )
          MediaType.MOVIE -> (media as MediaItem.Media.Movie).copy(
            isFavorite = media.id in moviesIdsSet,
          )
          else -> media
        }
      }

      Result.success(data.copy(searchList = updatedMedia))
    }

  override suspend fun insertFavoriteMedia(media: MediaItem.Media) {
    dao.insertMedia(media, null)
    dao.addToFavorites(mediaId = media.id, mediaType = media.mediaType)
  }

  override suspend fun removeFavoriteMedia(
    id: Int,
    mediaType: MediaType,
  ) {
    dao.removeFromFavorites(
      mediaId = id,
      mediaType = mediaType,
    )
  }

  override suspend fun checkIfMediaIsFavorite(
    id: Int,
    mediaType: MediaType,
  ): Result<Boolean> {
    dao.isMediaFavorite(
      mediaId = id,
      mediaType = mediaType,
    ).also {
      return Result.success(it)
    }
  }

  override fun fetchTvSeasons(id: Int): Flow<Result<List<Season>>> = dao.fetchSeasons(id)
    .map { runCatching { it } }

  override fun fetchSeason(
    showId: Int,
    seasonNumber: Int,
  ): Flow<Result<Season>> = dao
    .fetchSeason(
      showId = showId,
      seasonNumber = seasonNumber,
    ).map { Result.success(it) }

  override suspend fun fetchGenres(mediaType: MediaType): Flow<Resource<List<Genre>>> =
    networkBoundResource(
      query = { dao.fetchGenres(mediaType) },
      fetch = { remote.fetchGenres(mediaType).map(GenresListResponse::map) },
      saveFetchResult = { remoteData ->
        dao.insertGenres(mediaType = mediaType, genres = remoteData.data)
      },
      shouldFetch = { it.isEmpty() },
    )

  override fun fetchSeasonDetails(
    showId: Int,
    season: Int,
  ): Flow<Resource<SeasonDetails?>> = networkBoundResource(
    query = {
      combine(
        dao.fetchEpisodes(showId = showId, season = season),
        dao.fetchSeasonDetails(showId = showId, season = season),
        personDao.fetchGuestStars(showId = showId, season = season),
      ) { episodes, details, guestStars ->
        details?.map(
          episodes = episodes,
          guestStars = guestStars,
        )
      }
    },
    fetch = {
      remote.fetchSeason(
        showId = showId,
        season = season,
      ).map { it.map() }
    },
    saveFetchResult = { result ->
      withContext(dispatcher.io) {
        val data = result.data

        dao.insertSeasonDetails(
          seasonDetails = data,
          showId = showId,
          seasonNumber = season,
        )

        dao.insertEpisodes(data.episodes)

        data.episodes.forEach { episode ->
          personDao.insertGuestStars(
            season = season,
            showId = showId,
            episode = episode.number,
            guestStars = episode.guestStars,
          )
        }
      }
    },
    shouldFetch = { true },
  )

  override fun getSeasonEpisodesNumber(
    showId: Int,
    season: Int,
  ): Result<List<Int>> = runCatching {
    dao.fetchSeasonEpisodesCount(
      showId = showId,
      season = season,
    )
  }

  override fun fetchEpisode(
    showId: Int,
    season: Int,
    number: Int,
  ): Flow<Result<Episode>> = combine(
    personDao.fetchEpisodeGuestStars(
      showId = showId,
      episode = number,
      season = season,
    ),
    dao.fetchEpisode(
      showId = showId,
      episodeNumber = number,
      seasonNumber = season,
    ),
  ) { guestStars, episode ->
    runCatching {
      episode.copy(
        guestStars = guestStars,
      )
    }
  }

  override fun insertEpisodeRating(
    showId: Int,
    season: Int,
    number: Int,
    rating: Int,
  ): Result<Unit> = runCatching {
    dao.insertEpisodeRating(
      showId = showId,
      season = season,
      number = number,
      rating = rating,
    )
  }
}
