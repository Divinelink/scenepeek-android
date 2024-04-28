package com.andreolas.movierama.fakes.repository

import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.videos.VideosRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.Video
import com.andreolas.movierama.details.domain.repository.DetailsRepository
import com.andreolas.movierama.home.domain.model.MediaItem
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeDetailsRepository {

  val mock: DetailsRepository = mock()

  fun mockFetchMovieDetails(
    request: DetailsRequestApi,
    response: Result<MovieDetails>,
  ) {
    whenever(
      mock.fetchMovieDetails(request)
    ).thenReturn(
      flowOf(response)
    )
  }

  fun mockFetchMovieReviews(
    request: ReviewsRequestApi,
    response: Result<List<Review>>,
  ) {
    whenever(
      mock.fetchMovieReviews(request)
    ).thenReturn(
      flowOf(response)
    )
  }

  fun mockFetchSimilarMovies(
    request: SimilarRequestApi,
    response: Result<List<MediaItem.Media>>,
  ) {
    whenever(
      mock.fetchSimilarMovies(request)
    ).thenReturn(
      flowOf(response)
    )
  }

  fun mockFetchMovieVideos(
    request: VideosRequestApi,
    response: Result<List<Video>>,
  ) {
    whenever(
      mock.fetchVideos(request)
    ).thenReturn(
      flowOf(response)
    )
  }
}
