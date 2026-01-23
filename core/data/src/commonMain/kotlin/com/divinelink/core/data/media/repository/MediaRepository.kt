package com.divinelink.core.data.media.repository

import com.divinelink.core.model.Genre
import com.divinelink.core.model.PaginationData
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
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import kotlinx.coroutines.flow.Flow

typealias MediaListResult = Result<List<MediaItem.Media>>

/**
 * The data layer for any requests related to movies, tv and people.
 */
interface MediaRepository {

  /**
   * Request popular movies using pagination.
   * Uses [Flow] in order to observe changes to our popular movies list.
   */
  suspend fun fetchTrending(page: Int): Flow<Result<PaginationData<MediaItem>>>

  fun fetchMediaLists(
    request: MediaListRequest,
    page: Int,
  ): Flow<Result<PaginationData<MediaItem>>>

  fun discoverMovies(
    page: Int,
    sortOption: SortOption,
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>>

  fun discoverTvShows(
    page: Int,
    sortOption: SortOption,
    filters: List<DiscoverFilter>,
  ): Flow<Result<UserDataResponse>>

  /**
   * Fetch all popular movies that the user has marked as favorite.
   */
  fun fetchFavorites(): Flow<MediaListResult>

  fun fetchFavorites(mediaType: MediaType): Flow<Result<PaginationData<MediaItem>>>

  /**
   * Request movies through a search query. Uses pagination.
   * Uses [Flow] in order to observe changes to our movies list.
   */
  fun fetchSearchMovies(
    mediaType: MediaType,
    request: SearchRequestApi,
  ): Flow<Result<MultiSearch>>

  /**
   * Request movies, tv series and persons through a search query.
   */
  fun fetchMultiInfo(requestApi: MultiSearchRequestApi): Flow<Result<MultiSearch>>

  fun fetchTvSeasons(id: Int): Flow<Result<List<Season>>>

  fun fetchSeason(
    showId: Int,
    seasonNumber: Int,
  ): Flow<Result<Season>>

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

  fun fetchSeasonDetails(
    showId: Int,
    seasonNumber: Int,
  ): Flow<Result<SeasonDetails>>
}
