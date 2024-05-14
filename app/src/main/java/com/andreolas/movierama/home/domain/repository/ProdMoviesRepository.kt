package com.andreolas.movierama.home.domain.repository

import com.andreolas.movierama.base.data.local.popular.MovieDAO
import com.andreolas.movierama.base.data.local.popular.checkIfMediaIsFavorite
import com.andreolas.movierama.base.data.local.popular.fetchFavoriteMediaIDs
import com.andreolas.movierama.base.data.local.popular.insertFavoriteMedia
import com.andreolas.movierama.base.data.local.popular.map
import com.andreolas.movierama.base.data.local.popular.removeFavoriteMedia
import com.andreolas.movierama.base.data.local.popular.toDomainMoviesList
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.movies.model.popular.PopularRequestApi
import com.divinelink.core.network.movies.model.popular.toDomainMoviesList
import com.divinelink.core.network.movies.model.search.movie.SearchRequestApi
import com.divinelink.core.network.movies.model.search.movie.toDomainMoviesList
import com.divinelink.core.network.movies.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.movies.model.search.multi.mapper.map
import com.divinelink.core.network.movies.service.MovieService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProdMoviesRepository @Inject constructor(
  private val movieDAO: MovieDAO,
  private val movieRemote: MovieService,
) : MoviesRepository {

  override fun fetchPopularMovies(request: PopularRequestApi): Flow<MediaListResult> {
    return movieRemote
      .fetchPopularMovies(request)
      .map { apiResponse ->
        Result.success(apiResponse.toDomainMoviesList())
      }
      .catch { exception ->
        flowOf(Result.failure<Exception>(Exception(exception.message)))
      }
  }

  override fun fetchFavoriteMovies(): Flow<MediaListResult> = movieDAO
    .fetchFavoriteMovies()
    .map { moviesList ->
      Result.success(moviesList.toDomainMoviesList())
    }
    .catch { exception ->
      flowOf(Result.failure<Exception>(exception))
    }

  override fun fetchFavoriteTVSeries(): Flow<MediaListResult> = movieDAO
    .fetchFavoriteTVSeries()
    .map { tvList ->
      Result.success(tvList.map())
    }
    .catch { exception ->
      flowOf(Result.failure<Exception>(exception))
    }

  override fun fetchFavoriteIds(): Flow<Result<List<Pair<Int, MediaType>>>> = movieDAO
    .fetchFavoriteMediaIDs()
    .map { moviesList ->
      Result.success(moviesList)
    }
    .catch { exception ->
      flowOf(Result.failure<Exception>(exception))
    }

  @Deprecated("Use fetchMultiInfo instead")
  override fun fetchSearchMovies(request: SearchRequestApi): Flow<MediaListResult> {
    return movieRemote
      .fetchSearchMovies(request)
      .map { apiResponse ->
        Result.success(apiResponse.toDomainMoviesList())
      }
      .catch { exception ->
        flowOf(Result.failure<Exception>(exception))
      }
  }

  override fun fetchMultiInfo(requestApi: MultiSearchRequestApi): Flow<MultiListResult> {
    return movieRemote
      .fetchMultiInfo(requestApi)
      .map { apiResponse ->
        Result.success(apiResponse.map())
      }
      .catch { exception ->
        flowOf(Result.failure<Exception>(exception))
      }
  }

  override suspend fun insertFavoriteMedia(media: MediaItem.Media): Result<Unit> {
    movieDAO
      .insertFavoriteMedia(media)
      .also { return Result.success(it) }
  }

  override suspend fun removeFavoriteMedia(
    id: Int,
    mediaType: MediaType,
  ): Result<Unit> {
    movieDAO.removeFavoriteMedia(
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
    movieDAO.checkIfMediaIsFavorite(
      id = id, mediaType = mediaType
    ).also {
      return Result.success(it)
    }
  }
}
