package com.divinelink.core.network.media.service

import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistResponseApi
import com.divinelink.core.network.media.model.find.FindByIdResponseApi
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchResponseApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import kotlinx.coroutines.flow.Flow

interface MediaService {

  fun fetchPopularMovies(request: MoviesRequestApi): Flow<MoviesResponseApi>

  fun fetchMultiInfo(request: MultiSearchRequestApi): Flow<MultiSearchResponseApi>

  @Deprecated("Use fetchMultiInfo instead")
  fun fetchSearchMovies(request: SearchRequestApi): Flow<SearchResponseApi>

  fun fetchDetails(request: MediaRequestApi): Flow<DetailsResponseApi>

  fun fetchReviews(request: MediaRequestApi): Flow<ReviewsResponseApi>

  fun fetchRecommendedMovies(request: MediaRequestApi.Movie): Flow<MoviesResponseApi>
  fun fetchRecommendedTv(request: MediaRequestApi.TV): Flow<TvResponseApi>

  fun fetchVideos(request: MediaRequestApi): Flow<VideosResponseApi>

  fun fetchAggregatedCredits(id: Long): Flow<AggregateCreditsApi>

  fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi,
  ): Flow<AccountMediaDetailsResponseApi>

  fun submitRating(request: AddRatingRequestApi): Flow<Unit>

  fun deleteRating(request: DeleteRatingRequestApi): Flow<Unit>

  fun addToWatchlist(request: AddToWatchlistRequestApi): Flow<AddToWatchlistResponseApi>

  fun findById(externalId: String): Flow<FindByIdResponseApi>
}
