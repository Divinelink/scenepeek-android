package com.divinelink.core.network.media.service

import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarResponseApi
import com.divinelink.core.network.media.model.details.videos.VideosRequestApi
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistResponseApi
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
import kotlinx.coroutines.flow.Flow

interface MediaService {

  fun fetchPopularMovies(
    request: MoviesRequestApi,
  ): Flow<MoviesResponseApi>

  fun fetchMultiInfo(
    request: MultiSearchRequestApi,
  ): Flow<MultiSearchResponseApi>

  @Deprecated("Use fetchMultiInfo instead")
  fun fetchSearchMovies(
    request: SearchRequestApi,
  ): Flow<SearchResponseApi>

  fun fetchDetails(
    request: DetailsRequestApi,
  ): Flow<DetailsResponseApi>

  fun fetchReviews(
    request: ReviewsRequestApi,
  ): Flow<ReviewsResponseApi>

  fun fetchSimilarMovies(
    request: SimilarRequestApi,
  ): Flow<SimilarResponseApi>

  fun fetchVideos(
    request: VideosRequestApi,
  ): Flow<VideosResponseApi>

  fun fetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi
  ): Flow<AccountMediaDetailsResponseApi>

  fun submitRating(
    request: AddRatingRequestApi
  ): Flow<Unit>

  fun deleteRating(
    request: DeleteRatingRequestApi
  ): Flow<Unit>

  fun addToWatchlist(
    request: AddToWatchlistRequestApi
  ): Flow<AddToWatchlistResponseApi>
}
