package com.divinelink.core.data.media.repository

import com.divinelink.core.commons.data
import com.divinelink.core.database.media.dao.MediaDao
import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.search.MultiSearch
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.network.Resource
import com.divinelink.core.network.media.mapper.map
import com.divinelink.core.network.media.model.GenresListResponse
import com.divinelink.core.network.media.model.movie.map
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.toDomainMoviesList
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.media.model.search.multi.mapper.map
import com.divinelink.core.network.media.model.tv.map
import com.divinelink.core.network.media.service.MediaService
import com.divinelink.core.network.networkBoundResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ProdMediaRepository(
  private val remote: MediaService,
  private val dao: MediaDao,
) : MediaRepository {

  override fun fetchPopularMovies(page: Int): Flow<MediaListResult> = combine(
    remote.fetchPopularMovies(page = page),
    dao.getFavoriteMediaIds(MediaType.MOVIE),
  ) { response, favoriteIds ->
    val favoriteSet = favoriteIds.toSet()
    val updatedMovies = response.map().list.map { media ->
      (media as MediaItem.Media.Movie).copy(
        isFavorite = media.id in favoriteSet,
      )
    }
    Result.success(updatedMovies)
  }

  override fun discoverMovies(
    page: Int,
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>> = combine(
    remote.fetchDiscoverMovies(page, filters),
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
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>> = combine(
    remote.fetchDiscoverTv(page, filters),
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

  @Deprecated("Use fetchMultiInfo instead")
  override fun fetchSearchMovies(request: SearchRequestApi): Flow<MediaListResult> = remote
    .fetchSearchMovies(request)
    .map { apiResponse ->
      Result.success(apiResponse.toDomainMoviesList())
    }

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

  override fun fetchTvSeasons(id: Int): Flow<Result<List<Season>>> = dao.fetchSeasons(id).map {
    Result.success(it)
  }

  override suspend fun fetchGenres(mediaType: MediaType): Flow<Resource<List<Genre>>> =
    networkBoundResource(
      query = { dao.fetchGenres(mediaType) },
      fetch = { remote.fetchGenres(mediaType).map(GenresListResponse::map) },
      saveFetchResult = { remoteData ->
        dao.insertGenres(mediaType = mediaType, genres = remoteData.data)
      },
      shouldFetch = { it.isEmpty() },
    )
}
