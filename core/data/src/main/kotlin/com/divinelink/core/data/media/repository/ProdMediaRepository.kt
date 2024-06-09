package com.divinelink.core.data.media.repository

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.movie.toMoviesList
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.toDomainMoviesList
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.media.model.search.multi.mapper.map
import com.divinelink.core.network.media.service.MediaService
import com.divinelink.database.dao.MediaDao
import com.divinelink.database.dao.checkIfMediaIsFavorite
import com.divinelink.database.dao.fetchFavoriteMediaIDs
import com.divinelink.database.dao.insertFavoriteMedia
import com.divinelink.database.dao.removeFavoriteMedia
import com.divinelink.database.mapper.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdMediaRepository @Inject constructor(
  private val mediaDao: MediaDao,
  private val mediaRemote: MediaService,
) : MediaRepository {

  override fun fetchPopularMovies(request: MoviesRequestApi): Flow<MediaListResult> {
    return mediaRemote
      .fetchPopularMovies(request)
      .map { apiResponse ->
        Result.success(apiResponse.toMoviesList())
      }
      .catch { exception ->
        flowOf(Result.failure<Exception>(Exception(exception.message)))
      }
  }

  override fun fetchFavoriteMovies(): Flow<MediaListResult> = mediaDao
    .fetchFavoriteMovies()
    .map { moviesList ->
      Result.success(moviesList.map())
    }
    .catch { exception ->
      flowOf(Result.failure<Exception>(exception))
    }

  override fun fetchFavoriteTVSeries(): Flow<MediaListResult> = mediaDao
    .fetchFavoriteTVSeries()
    .map { tvList ->
      Result.success(tvList.map())
    }
    .catch { exception ->
      flowOf(Result.failure<Exception>(exception))
    }

  override fun fetchFavoriteIds(): Flow<Result<List<Pair<Int, MediaType>>>> = mediaDao
    .fetchFavoriteMediaIDs()
    .map { moviesList ->
      Result.success(moviesList)
    }
    .catch { exception ->
      flowOf(Result.failure<Exception>(exception))
    }

  @Deprecated("Use fetchMultiInfo instead")
  override fun fetchSearchMovies(request: SearchRequestApi): Flow<MediaListResult> {
    return mediaRemote
      .fetchSearchMovies(request)
      .map { apiResponse ->
        Result.success(apiResponse.toDomainMoviesList())
      }
      .catch { exception ->
        flowOf(Result.failure<Exception>(exception))
      }
  }

  override fun fetchMultiInfo(requestApi: MultiSearchRequestApi): Flow<MultiListResult> {
    return mediaRemote
      .fetchMultiInfo(requestApi)
      .map { apiResponse ->
        Result.success(apiResponse.map())
      }
      .catch { exception ->
        flowOf(Result.failure<Exception>(exception))
      }
  }

  override suspend fun insertFavoriteMedia(media: MediaItem.Media): Result<Unit> {
    mediaDao
      .insertFavoriteMedia(media)
      .also { return Result.success(it) }
  }

  override suspend fun removeFavoriteMedia(
    id: Int,
    mediaType: MediaType,
  ): Result<Unit> {
    mediaDao.removeFavoriteMedia(
      id = id,
      mediaType = mediaType,
    ).also {
      return Result.success(it)
    }
  }

  override suspend fun checkIfMediaIsFavorite(
    id: Int,
    mediaType: MediaType,
  ): Result<Boolean> {
    mediaDao.checkIfMediaIsFavorite(
      id = id, mediaType = mediaType
    ).also {
      return Result.success(it)
    }
  }
}
