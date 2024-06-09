package com.divinelink.core.data.media.repository

import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import kotlinx.coroutines.flow.Flow

typealias MultiListResult = Result<List<MediaItem>>
typealias MediaListResult = Result<List<MediaItem.Media>>

/**
 * The data layer for any requests related to movies, tv and people.
 */
interface MediaRepository {

  /**
   * Request popular movies using pagination.
   * Uses [Flow] in order to observe changes to our popular movies list.
   */
  fun fetchPopularMovies(
    request: MoviesRequestApi,
  ): Flow<MediaListResult>

  /**
   * Fetch all popular movies that the user has marked as favorite.
   */
  fun fetchFavoriteMovies(): Flow<MediaListResult>

  /**
   * Fetch all favorite tv series.
   */
  fun fetchFavoriteTVSeries(): Flow<MediaListResult>

  /**
   * Fetch all favorite ids: movies, series and persons.
   * Uses [Flow] in order to observe changes to our favorite list.
   */
  fun fetchFavoriteIds(): Flow<Result<List<Pair<Int, MediaType>>>>

  /**
   * Request movies through a search query. Uses pagination.
   * Uses [Flow] in order to observe changes to our movies list.
   */
  fun fetchSearchMovies(
    request: SearchRequestApi,
  ): Flow<MediaListResult>

  /**
   * Request movies, tv series and persons through a search query.
   */
  fun fetchMultiInfo(
    requestApi: MultiSearchRequestApi,
  ): Flow<MultiListResult>

  /**
   * Add favorite [media] to local storage.
   */
  suspend fun insertFavoriteMedia(
    media: MediaItem.Media,
  ): Result<Unit>

  /**
   * Remove favorite movie using its [id] from local storage.
   */
  suspend fun removeFavoriteMedia(
    id: Int,
    mediaType: MediaType,
  ): Result<Unit>

  suspend fun checkIfMediaIsFavorite(
    id: Int,
    mediaType: MediaType,
  ): Result<Boolean>
}
