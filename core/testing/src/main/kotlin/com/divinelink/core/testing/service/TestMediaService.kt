package com.divinelink.core.testing.service

import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistResponseApi
import com.divinelink.core.network.media.model.movie.MovieResponseApi
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchResponseApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi
import com.divinelink.core.network.media.service.MediaService
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestMediaService {

  val mock: MediaService = mock()

  fun mockFetchPopularMovies(
    request: MoviesRequestApi,
    result: Flow<MoviesResponseApi>,
  ) {
    whenever(
      mock.fetchPopularMovies(request),
    ).thenReturn(
      result,
    )
  }

  fun mockFetchSearchMovies(
    request: SearchRequestApi,
    result: Flow<SearchResponseApi>,
  ) {
    whenever(
      mock.fetchSearchMovies(request),
    ).thenReturn(
      result,
    )
  }

  fun mockFetchMovieDetails(
    request: DetailsRequestApi,
    response: Flow<DetailsResponseApi>,
  ) {
    whenever(
      mock.fetchDetails(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchMovieReviews(
    request: DetailsRequestApi,
    response: Flow<ReviewsResponseApi>,
  ) {
    whenever(
      mock.fetchReviews(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchSimilarMovies(
    request: SimilarRequestApi,
    response: Flow<MovieResponseApi>,
  ) {
    whenever(
      mock.fetchRecommendedMovies(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchMovieVideos(
    request: DetailsRequestApi,
    response: Flow<VideosResponseApi>,
  ) {
    whenever(
      mock.fetchVideos(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi,
    response: Flow<AccountMediaDetailsResponseApi>,
  ) {
    whenever(
      mock.fetchAccountMediaDetails(request),
    ).thenReturn(
      response,
    )
  }

  fun mockSubmitRating(
    request: AddRatingRequestApi,
    response: Flow<Unit>,
  ) {
    whenever(
      mock.submitRating(request),
    ).thenReturn(
      response,
    )
  }

  fun mockDeleteRating(
    request: DeleteRatingRequestApi,
    response: Flow<Unit>,
  ) {
    whenever(
      mock.deleteRating(request),
    ).thenReturn(
      response,
    )
  }

  fun mockAddToWatchlist(
    request: AddToWatchlistRequestApi,
    response: Flow<AddToWatchlistResponseApi>,
  ) {
    whenever(
      mock.addToWatchlist(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchAggregateCredits(response: Flow<AggregateCreditsApi>) {
    whenever(
      mock.fetchAggregatedCredits(any()),
    ).thenReturn(
      response,
    )
  }
}
