package com.divinelink.core.network.media.service

import com.divinelink.core.datastore.auth.SavedStateStorage
import com.divinelink.core.datastore.auth.tmdbSessionId
import com.divinelink.core.model.details.AccountDataSection
import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.home.MediaListRequest
import com.divinelink.core.model.media.MediaReference
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.network.client.TMDbClient
import com.divinelink.core.network.media.model.GenresListResponse
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.details.CollectionDetailsResponse
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.providers.WatchProvidersResponse
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.season.SeasonDetailsResponse
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.details.watchlist.AddToAccountRequest
import com.divinelink.core.network.media.model.details.watchlist.AddToAccountRequestBody
import com.divinelink.core.network.media.model.details.watchlist.TMDBResponse
import com.divinelink.core.network.media.model.find.FindByIdResponseApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestBodyApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.search.SearchKeywordResponse
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import com.divinelink.core.network.media.util.buildCollectionsUrl
import com.divinelink.core.network.media.util.buildDiscoverUrl
import com.divinelink.core.network.media.util.buildFetchDetailsUrl
import com.divinelink.core.network.media.util.buildFetchMediaListUrl
import com.divinelink.core.network.media.util.buildFindByIdUrl
import com.divinelink.core.network.media.util.buildGenreUrl
import com.divinelink.core.network.media.util.buildSearchKeywordUrl
import com.divinelink.core.network.media.util.buildSeasonDetailsUrl
import com.divinelink.core.network.media.util.buildWatchProvidersUrl
import com.divinelink.core.network.runCatchingWithNetworkRetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProdMediaService(
  private val restClient: TMDbClient,
  private val storage: SavedStateStorage,
) : MediaService {

  override suspend fun fetchMediaLists(
    request: MediaListRequest,
    page: Int,
  ): Result<MultiSearchResponseApi> = runCatching {
    val url = buildFetchMediaListUrl(
      request = request,
      page = page,
      language = restClient.language(),
    )

    restClient.get<MultiSearchResponseApi>(url = url)
  }

  override fun fetchDiscoverMovies(
    page: Int,
    sortOption: SortOption,
    filters: List<DiscoverFilter>,
  ): Flow<MoviesResponseApi> = flow {
    val url = buildDiscoverUrl(
      media = MediaType.MOVIE,
      page = page,
      sortOption = sortOption,
      filters = filters,
      language = restClient.language(),
    )

    val response = restClient.get<MoviesResponseApi>(url = url)

    emit(response)
  }

  override fun fetchDiscoverTv(
    page: Int,
    sortOption: SortOption,
    filters: List<DiscoverFilter>,
  ): Flow<TvResponseApi> = flow {
    val url = buildDiscoverUrl(
      media = MediaType.TV,
      page = page,
      sortOption = sortOption,
      filters = filters,
      language = restClient.language(),
    )

    val response = restClient.get<TvResponseApi>(url = url)

    emit(response)
  }

  override fun fetchMultiInfo(request: MultiSearchRequestApi): Flow<MultiSearchResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/search/multi?"
    val url = baseUrl +
      "&language=${restClient.language()}" +
      "&query=${request.query}" +
      "&page=${request.page}" +
      "&include_adult=false"

    val response = restClient.get<MultiSearchResponseApi>(url = url)

    emit(response)
  }

  override suspend fun fetchSearchMovies(
    mediaType: MediaType,
    request: SearchRequestApi,
  ): Result<MultiSearchResponseApi> = runCatching {
    val baseUrl = "${restClient.tmdbUrl}/search/${mediaType.value}?"
    val url = baseUrl +
      "&language=${restClient.language()}" +
      "&query=${request.query}" +
      "&page=${request.page}" +
      "&include_adult=false"

    restClient.get<MultiSearchResponseApi>(url = url)
  }

  override fun fetchDetails(
    request: MediaRequestApi,
    appendToResponse: Boolean,
  ): Flow<DetailsResponseApi> = flow {
    val url = buildFetchDetailsUrl(
      media = request.mediaType,
      id = request.id,
      appendToResponse = appendToResponse,
      language = restClient.language(),
    )

    val response = restClient.get<DetailsResponseApi>(url = url)

    emit(response)
  }

  override fun fetchReviews(request: MediaRequestApi): Flow<ReviewsResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.mediaType.value}/"
    val url = baseUrl +
      "${request.id}" +
      "/reviews?" +
      "&language=${restClient.language()}" +
      "&include_adult=false"

    val response = restClient.get<ReviewsResponseApi>(url = url)

    emit(response)
  }

  override fun fetchRecommendedMovies(request: MediaRequestApi.Movie): Flow<MoviesResponseApi> =
    flow {
      val baseUrl = "${restClient.tmdbUrl}/${request.mediaType.value}/"
      val url = baseUrl +
        "${request.id}" +
        "/recommendations?" +
        "&language=${restClient.language()}" +
        "&include_adult=false"

      val response = restClient.get<MoviesResponseApi>(url = url)

      emit(response)
    }

  override fun fetchRecommendedTv(request: MediaRequestApi.TV): Flow<TvResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.mediaType.value}/"
    val url = baseUrl +
      "${request.id}" +
      "/recommendations?" +
      "&language=${restClient.language()}" +
      "&include_adult=false"

    val response = restClient.get<TvResponseApi>(url = url)

    emit(response)
  }

  override fun fetchVideos(request: MediaRequestApi): Flow<VideosResponseApi> = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.mediaType.value}/"
    val url = baseUrl +
      "${request.id}" +
      "/videos?" +
      "&language=${restClient.language()}"

    val response = restClient.get<VideosResponseApi>(url = url)

    emit(response)
  }

  override fun fetchAggregatedCredits(id: Long): Flow<AggregateCreditsApi> = flow {
    val url = "${restClient.tmdbUrl}/tv/$id/aggregate_credits" +
      "?language=${restClient.language()}"

    val response = restClient.get<AggregateCreditsApi>(url = url)

    emit(response)
  }

  override fun fetchAccountMediaDetails(request: AccountMediaDetailsRequestApi) = flow {
    val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
    val url = baseUrl +
      "${request.id}" +
      "/account_states?" +
      "&session_id=${request.sessionId}"

    val response = restClient.get<AccountMediaDetailsResponseApi>(
      url = url,
    )

    emit(response)
  }

  override suspend fun submitRating(request: AddRatingRequestApi): Result<TMDBResponse> =
    runCatchingWithNetworkRetry(
      maxDelay = 1000L,
      times = 10,
    ) {
      val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
      val url = baseUrl +
        "${request.id}/rating?" +
        "&session_id=${request.sessionId}"

      restClient.post<AddRatingRequestBodyApi, TMDBResponse>(
        url = url,
        body = AddRatingRequestBodyApi(request.rating),
      )
    }

  override suspend fun deleteRating(request: DeleteRatingRequestApi): Result<TMDBResponse> =
    runCatchingWithNetworkRetry(
      maxDelay = 1000L,
      times = 10,
    ) {
      val baseUrl = "${restClient.tmdbUrl}/${request.endpoint}/"
      val url = baseUrl +
        "${request.id}/rating?" +
        "&session_id=${request.sessionId}"

      restClient.delete<TMDBResponse>(url = url)
    }

  override suspend fun addToAccount(
    request: AddToAccountRequest,
    section: AccountDataSection,
  ): Result<TMDBResponse> = runCatchingWithNetworkRetry(
    maxDelay = 1000L,
    times = 10,
  ) {
    val url = "${restClient.tmdbUrl}/account/${request.accountId}/${section.value}" +
      "?session_id=${request.sessionId}"

    restClient.post<AddToAccountRequestBody, TMDBResponse>(
      url = url,
      body = AddToAccountRequestBody(
        mediaType = request.mediaType,
        mediaId = request.mediaId,
        favorite = request.favorite,
        watchlist = request.watchlist,
      ),
    )
  }

  override fun findById(externalId: String): Flow<FindByIdResponseApi> = flow {
    val url = buildFindByIdUrl(externalId = externalId)

    emit(restClient.get<FindByIdResponseApi>(url = url))
  }

  override suspend fun fetchGenres(mediaType: MediaType): Result<GenresListResponse> = runCatching {
    restClient.get<GenresListResponse>(
      url = buildGenreUrl(
        media = mediaType,
        language = restClient.language(),
      ),
    )
  }

  override suspend fun fetchSeason(
    showId: Long,
    season: Int,
  ): Result<SeasonDetailsResponse> = runCatching {
    restClient.get<SeasonDetailsResponse>(
      url = buildSeasonDetailsUrl(
        showId = showId,
        seasonNumber = season,
        sessionId = storage.tmdbSessionId,
        language = restClient.language(),
      ),
    )
  }

  override suspend fun fetchCollectionDetails(id: Int): Result<CollectionDetailsResponse> =
    runCatching {
      restClient.get<CollectionDetailsResponse>(
        url = buildCollectionsUrl(
          id = id,
          language = restClient.language(),
        ),
      )
    }

  override suspend fun searchKeywords(request: SearchRequestApi): Result<SearchKeywordResponse> =
    runCatching {
      restClient.get<SearchKeywordResponse>(url = buildSearchKeywordUrl(request = request))
    }

  override suspend fun fetchWatchProviders(media: MediaReference): Result<WatchProvidersResponse> =
    runCatching {
      restClient.get<WatchProvidersResponse>(url = buildWatchProvidersUrl(media = media))
    }
}
