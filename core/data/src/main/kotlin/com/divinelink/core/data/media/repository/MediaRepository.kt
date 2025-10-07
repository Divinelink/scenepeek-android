package com.divinelink.core.data.media.repository

import com.divinelink.core.model.Genre
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.search.MultiSearch
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.network.Resource
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
  fun fetchPopularMovies(request: MoviesRequestApi): Flow<MediaListResult>

  fun discoverMovies(
    page: Int,
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>>

  fun discoverTvShows(
    page: Int,
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>>

  /**
   * Fetch all popular movies that the user has marked as favorite.
   */
  fun fetchFavorites(): Flow<MediaListResult>

  /**
   * Fetch all favorite ids: movies, series and persons.
   * Uses [Flow] in order to observe changes to our favorite list.
   */
//  fun fetchFavoriteIds(): Flow<Result<List<Pair<Int, MediaType>>>>

  /**
   * Request movies through a search query. Uses pagination.
   * Uses [Flow] in order to observe changes to our movies list.
   */
  fun fetchSearchMovies(request: SearchRequestApi): Flow<MediaListResult>

  /**
   * Request movies, tv series and persons through a search query.
   */
  fun fetchMultiInfo(requestApi: MultiSearchRequestApi): Flow<Result<MultiSearch>>

  fun fetchTvSeasons(id: Int): Flow<Result<List<Season>>>

  /**
   * Add favorite [media] to local storage.
   */
  suspend fun insertFavoriteMedia(media: MediaItem.Media)

  /**
   * Remove favorite movie using its [id] from local storage.
   */
  suspend fun removeFavoriteMedia(
    id: Int,
    mediaType: MediaType,
  )

  suspend fun checkIfMediaIsFavorite(
    id: Int,
    mediaType: MediaType,
  ): Result<Boolean>

  suspend fun fetchGenres(mediaType: MediaType): Flow<Resource<List<Genre>>>
}
