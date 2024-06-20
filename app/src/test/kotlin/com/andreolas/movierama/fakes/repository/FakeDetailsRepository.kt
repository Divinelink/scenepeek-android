package com.andreolas.movierama.fakes.repository

import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.Movie
import com.divinelink.core.model.details.Review
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.details.DetailsRequestApi
import com.divinelink.core.network.media.model.details.reviews.ReviewsRequestApi
import com.divinelink.core.network.media.model.details.similar.SimilarRequestApi
import com.divinelink.core.network.media.model.details.videos.VideosRequestApi
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class FakeDetailsRepository {

  val mock: DetailsRepository = mock()

  fun mockFetchMovieDetails(
    request: DetailsRequestApi,
    response: Result<Movie>,
  ) {
    whenever(
      mock.fetchMovieDetails(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchMovieReviews(
    request: ReviewsRequestApi,
    response: Result<List<Review>>,
  ) {
    whenever(
      mock.fetchMovieReviews(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchSimilarMovies(
    request: SimilarRequestApi,
    response: Result<List<MediaItem.Media>>,
  ) {
    whenever(
      mock.fetchSimilarMovies(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchMovieVideos(
    request: VideosRequestApi,
    response: Result<List<Video>>,
  ) {
    whenever(
      mock.fetchVideos(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchAccountMediaDetails(response: Result<AccountMediaDetails>) {
    whenever(
      mock.fetchAccountMediaDetails(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockSubmitRating(response: Result<Unit>) {
    whenever(
      mock.submitRating(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockDeleteRating(response: Result<Unit>) {
    whenever(
      mock.deleteRating(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockAddToWatchlist(response: Result<Unit>) {
    whenever(
      mock.addToWatchlist(any()),
    ).thenReturn(
      flowOf(response),
    )
  }
}
