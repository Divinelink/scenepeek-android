package com.divinelink.core.network.movies.service

import com.divinelink.core.network.movies.model.details.DetailsRequestApi
import com.divinelink.core.network.movies.model.details.DetailsResponseApi
import com.divinelink.core.network.movies.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.movies.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.movies.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.movies.model.details.similar.SimilarResponseApi
import com.divinelink.core.network.movies.model.details.videos.VideosRequestApi
import com.divinelink.core.network.movies.model.details.videos.VideosResponseApi
import com.divinelink.core.network.movies.model.popular.PopularRequestApi
import com.divinelink.core.network.movies.model.popular.PopularResponseApi
import com.divinelink.core.network.movies.model.rating.AddRatingRequestApi
import com.divinelink.core.network.movies.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.movies.model.search.movie.SearchRequestApi
import com.divinelink.core.network.movies.model.search.movie.SearchResponseApi
import com.divinelink.core.network.movies.model.search.multi.MultiSearchRequestApi
import com.divinelink.core.network.movies.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.movies.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.movies.model.states.AccountMediaDetailsResponseApi
import kotlinx.coroutines.flow.Flow

interface MovieService {

  fun fetchPopularMovies(
    request: PopularRequestApi,
  ): Flow<PopularResponseApi>

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
}
