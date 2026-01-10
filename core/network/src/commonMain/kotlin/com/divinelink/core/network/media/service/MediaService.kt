package com.divinelink.core.network.media.service

import com.divinelink.core.model.discover.DiscoverFilter
import com.divinelink.core.model.home.HomeSection
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.GenresListResponse
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.details.watchlist.SubmitOnAccountResponse
import com.divinelink.core.network.media.model.find.FindByIdResponseApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import kotlinx.coroutines.flow.Flow

interface MediaService {

  suspend fun fetchMediaLists(
    section: HomeSection,
    page: Int,
  ): Result<MultiSearchResponseApi>

  fun fetchDiscoverMovies(
    page: Int,
    filters: List<DiscoverFilter>,
  ): Flow<MoviesResponseApi>

  fun fetchDiscoverTv(
    page: Int,
    filters: List<DiscoverFilter>,
  ): Flow<TvResponseApi>

  fun fetchMultiInfo(request: MultiSearchRequestApi): Flow<MultiSearchResponseApi>

  suspend fun fetchSearchMovies(
    mediaType: MediaType,
    request: SearchRequestApi,
  ): Result<MultiSearchResponseApi>

  fun fetchDetails(
    request: MediaRequestApi,
    appendToResponse: Boolean,
  ): Flow<DetailsResponseApi>

  fun fetchReviews(request: MediaRequestApi): Flow<ReviewsResponseApi>

  fun fetchRecommendedMovies(request: MediaRequestApi.Movie): Flow<MoviesResponseApi>
  fun fetchRecommendedTv(request: MediaRequestApi.TV): Flow<TvResponseApi>

  fun fetchVideos(request: MediaRequestApi): Flow<VideosResponseApi>

  fun fetchAggregatedCredits(id: Long): Flow<AggregateCreditsApi>

  fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi,
  ): Flow<AccountMediaDetailsResponseApi>

  suspend fun submitRating(request: AddRatingRequestApi): Result<SubmitOnAccountResponse>

  suspend fun deleteRating(request: DeleteRatingRequestApi): Result<SubmitOnAccountResponse>

  suspend fun addToWatchlist(request: AddToWatchlistRequestApi): Result<SubmitOnAccountResponse>

  fun findById(externalId: String): Flow<FindByIdResponseApi>

  suspend fun fetchGenres(mediaType: MediaType): Result<GenresListResponse>
}
