package com.divinelink.core.testing.service

import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.GenresListResponse
import com.divinelink.core.network.media.model.MediaRequestApi
import com.divinelink.core.network.media.model.credits.AggregateCreditsApi
import com.divinelink.core.network.media.model.details.DetailsResponseApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsResponseApi
import com.divinelink.core.network.media.model.details.videos.VideosResponseApi
import com.divinelink.core.network.media.model.details.watchlist.AddToWatchlistRequestApi
import com.divinelink.core.network.media.model.details.watchlist.SubmitOnAccountResponse
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.rating.AddRatingRequestApi
import com.divinelink.core.network.media.model.rating.DeleteRatingRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchResponseApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.media.model.states.AccountMediaDetailsResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import com.divinelink.core.network.media.service.MediaService
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyNoInteractions
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

  fun mockFetchDetails(
    request: MediaRequestApi,
    response: Flow<DetailsResponseApi>,
    appendToResponse: Boolean = true,
  ) {
    whenever(
      mock.fetchDetails(request = request, appendToResponse = appendToResponse),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchMovieReviews(
    request: MediaRequestApi,
    response: Flow<ReviewsResponseApi>,
  ) {
    whenever(
      mock.fetchReviews(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchRecommendedMovies(
    request: MediaRequestApi.Movie,
    response: Flow<MoviesResponseApi>,
  ) {
    whenever(
      mock.fetchRecommendedMovies(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchRecommendedTv(
    request: MediaRequestApi.TV,
    response: Flow<TvResponseApi>,
  ) {
    whenever(
      mock.fetchRecommendedTv(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchMovieVideos(
    request: MediaRequestApi,
    response: Flow<VideosResponseApi>,
  ) {
    whenever(
      mock.fetchVideos(request),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchMultiSearch(response: Flow<MultiSearchResponseApi>) {
    whenever(
      mock.fetchMultiInfo(any()),
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

  suspend fun mockSubmitRating(
    request: AddRatingRequestApi,
    response: Result<SubmitOnAccountResponse>,
  ) {
    whenever(
      mock.submitRating(request),
    ).thenReturn(
      response,
    )
  }

  suspend fun mockDeleteRating(
    request: DeleteRatingRequestApi,
    response: Result<SubmitOnAccountResponse>,
  ) {
    whenever(
      mock.deleteRating(request),
    ).thenReturn(
      response,
    )
  }

  suspend fun mockAddToWatchlist(
    request: AddToWatchlistRequestApi,
    response: Result<SubmitOnAccountResponse>,
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

  suspend fun mockFetchMovieGenres(result: Result<GenresListResponse>) {
    whenever(
      mock.fetchGenres(MediaType.MOVIE),
    ).thenReturn(
      result,
    )
  }

  suspend fun mockFetchTvGenres(result: Result<GenresListResponse>) {
    whenever(
      mock.fetchGenres(MediaType.TV),
    ).thenReturn(
      result,
    )
  }

  fun verifyNoInteractions() {
    verifyNoInteractions(mock)
  }
}
