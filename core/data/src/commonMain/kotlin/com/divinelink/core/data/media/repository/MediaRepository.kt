@file:Suppress("TooManyFunctions")

package com.divinelink.core.data.media.repository

import com.divinelink.core.model.Genre
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.details.Company
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.Keyword
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.details.SeasonDetails
import com.divinelink.core.model.details.SeasonProgress
import com.divinelink.core.model.details.TV
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.home.MediaListRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.search.MultiSearch
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.track.ShowStatus
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.model.user.data.UserDataResponse
import com.divinelink.core.network.Resource
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import kotlinx.coroutines.flow.Flow

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
  fun fetchFavorites(): Flow<List<MediaItem>>

  fun fetchFavorites(mediaType: MediaType): Flow<Result<PaginationData<MediaItem>>>

  suspend fun fetchSearchKeywords(request: SearchRequestApi): Result<PaginationData<Keyword>>

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

  fun fetchTvSeasons(id: Long): Flow<Result<List<Season>>>

  fun fetchSeason(
    showId: Long,
    seasonNumber: Int,
  ): Flow<Result<Season>>

  fun saveMediaItem(mediaItem: MediaItem.Media)
  fun saveShowDetails(show: TV)

  /**
   * Add favorite [media] to local storage.
   */
  suspend fun insertFavoriteMedia(media: MediaItem)

  /**
   * Remove favorite movie using its [id] from local storage.
   */
  suspend fun removeFavoriteMedia(
    id: Long,
    mediaType: MediaType,
  )

  suspend fun checkIfMediaIsFavorite(
    id: Long,
    mediaType: MediaType,
  ): Result<Boolean>

  suspend fun fetchGenres(mediaType: MediaType): Flow<Resource<List<Genre>>>

  fun fetchSeasonDetails(
    showId: Long,
    season: Int,
  ): Flow<Resource<SeasonDetails?>>

  fun fetchSeasonEpisodes(
    showId: Long,
    season: Int,
  ): Flow<Resource<List<Episode>?>>

  fun fetchEpisode(
    showId: Long,
    season: Int,
    number: Int,
  ): Flow<Result<Episode>>

  fun getSeasonEpisodesNumber(
    showId: Long,
    season: Int,
  ): Result<List<Int>>

  fun insertEpisodeRating(
    showId: Long,
    season: Int,
    number: Int,
    rating: Int,
  ): Result<Unit>

  fun clearAllEpisodeRatings(): Result<Unit>

  suspend fun searchCompany(request: SearchRequestApi): Result<PaginationData<Company>>

  fun getSeasonsProgress(showId: Long): Flow<List<SeasonProgress>>
  fun getWatchedEpisodesForSeason(
    showId: Long,
    seasonNumber: Int,
  ): Flow<List<Long>>

  fun markEpisodeAsWatched(episodeId: Long)
  fun unmarkEpisodeAsWatched(episodeId: Long)

  fun markSeasonAsWatched(episodeIds: List<Long>)
  fun unmarkSeasonAsWatched(episodeIds: List<Long>)

  fun getUserShows(): Flow<List<Long>>
  fun getUpNextEpisode(showId: Long): Flow<Episode>
  fun getNextUpEpisodes(): Flow<List<NextUpEpisode>>

  suspend fun findFirstIncompleteSeason(showId: Long): Int?
  suspend fun findFirstMissingSeason(showId: Long, maxSeason: Int): Int?
  suspend fun getMaxLocalSeason(showId: Long): Int?

  fun getUserShowStatus(showId: Long): ShowStatus?
  fun addShow(showId: Long)
  fun deleteShow(showId: Long)
}
