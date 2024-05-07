package com.andreolas.movierama.fakes.remote

import com.andreolas.movierama.base.data.remote.movies.dto.account.states.AccountMediaDetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.account.states.AccountMediaDetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularResponseApi
import com.andreolas.movierama.base.data.remote.movies.dto.rating.AddRatingRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchResponseApi
import com.andreolas.movierama.base.data.remote.movies.service.MovieService
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
}
