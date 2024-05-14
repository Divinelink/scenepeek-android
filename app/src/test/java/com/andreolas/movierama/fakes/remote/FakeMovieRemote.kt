package com.andreolas.movierama.fakes.remote

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
import com.divinelink.core.network.movies.model.states.AccountMediaDetailsRequestApi
import com.divinelink.core.network.movies.model.states.AccountMediaDetailsResponseApi
import com.divinelink.core.network.movies.service.MovieService
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeMovieRemote {

  val mock: MovieService = mock()

  fun mockFetchPopularMovies(
    request: PopularRequestApi,
    result: Flow<PopularResponseApi>,
  ) {
    whenever(
      mock.fetchPopularMovies(request)
    ).thenReturn(
      result
    )
  }

  fun mockFetchSearchMovies(
    request: SearchRequestApi,
    result: Flow<SearchResponseApi>,
  ) {
    whenever(
      mock.fetchSearchMovies(request)
    ).thenReturn(
      result
    )
  }

  fun mockFetchMovieDetails(
    request: DetailsRequestApi,
    response: Flow<DetailsResponseApi>,
  ) {
    whenever(
      mock.fetchDetails(request)
    ).thenReturn(
      response
    )
  }

  fun mockFetchMovieReviews(
    request: ReviewsRequestApi,
    response: Flow<ReviewsResponseApi>,
  ) {
    whenever(
      mock.fetchReviews(request)
    ).thenReturn(
      response
    )
  }

  fun mockFetchSimilarMovies(
    request: SimilarRequestApi,
    response: Flow<SimilarResponseApi>,
  ) {
    whenever(
      mock.fetchSimilarMovies(request)
    ).thenReturn(
      response
    )
  }

  fun mockFetchMovieVideos(
    request: VideosRequestApi,
    response: Flow<VideosResponseApi>,
  ) {
    whenever(
      mock.fetchVideos(request)
    ).thenReturn(
      response
    )
  }

  fun mockFetchAccountMediaDetails(
    request: AccountMediaDetailsRequestApi,
    response: Flow<AccountMediaDetailsResponseApi>,
  ) {
    whenever(
      mock.fetchAccountMediaDetails(request)
    ).thenReturn(
      response
    )
  }

  fun mockSubmitRating(
    request: AddRatingRequestApi,
    response: Flow<Unit>,
  ) {
    whenever(
      mock.submitRating(request)
    ).thenReturn(
      response
    )
  }

  fun mockDeleteRating(
    request: DeleteRatingRequestApi,
    response: Flow<Unit>,
  ) {
    whenever(
      mock.deleteRating(request)
    ).thenReturn(
      response
    )
  }
}
