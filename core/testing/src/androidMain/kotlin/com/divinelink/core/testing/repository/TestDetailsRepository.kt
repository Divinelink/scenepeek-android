package com.divinelink.core.testing.repository

import com.divinelink.core.data.details.repository.DetailsRepository
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.review.Review
import com.divinelink.core.model.details.video.Video
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.MediaRequestApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestDetailsRepository {

  val mock: DetailsRepository = mock()

  fun mockFetchMediaDetails(
    request: MediaRequestApi,
    response: Result<MediaDetails>,
  ) {
    whenever(
      mock.fetchMediaDetails(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchMediaDetails(response: Channel<Result<MediaDetails>>) = apply {
    whenever(mock.fetchMediaDetails(any())).thenReturn(response.consumeAsFlow())
  }

  fun mockFetchMovieReviews(
    request: MediaRequestApi,
    response: Result<List<Review>>,
  ) {
    whenever(
      mock.fetchMediaReviews(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchSimilarMovies(
    request: MediaRequestApi.Movie,
    response: Result<PaginationData<MediaItem.Media>>,
  ) {
    whenever(
      mock.fetchRecommendedMovies(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchMovieVideos(
    request: MediaRequestApi,
    response: Result<List<Video>>,
  ) {
    whenever(
      mock.fetchVideos(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchAggregateCredits(response: Result<AggregateCredits>) {
    whenever(
      mock.fetchAggregateCredits(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchAggregateCredits(response: Flow<Result<AggregateCredits>>) {
    whenever(mock.fetchAggregateCredits(any())).thenReturn(response)
  }

  fun mockFetchAccountMediaDetails(response: Result<AccountMediaDetails>) {
    whenever(
      mock.fetchAccountMediaDetails(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchAccountMediaDetails(response: Flow<Result<AccountMediaDetails>>) {
    whenever(
      mock.fetchAccountMediaDetails(any()),
    ).thenReturn(response)
  }

  suspend fun mockSubmitRating(response: Result<Unit>) {
    whenever(
      mock.submitRating(any()),
    ).thenReturn(
      response,
    )
  }

  suspend fun mockDeleteRating(response: Result<Unit>) {
    whenever(
      mock.deleteRating(any()),
    ).thenReturn(
      response,
    )
  }

  suspend fun mockAddToWatchlist(response: Result<Unit>) {
    whenever(
      mock.addToWatchlist(any()),
    ).thenReturn(
      response,
    )
  }

  fun mockFetchIMDbDetails(response: Result<RatingDetails?>) {
    whenever(
      mock.fetchIMDbDetails(any()),
    ).thenReturn(flowOf(response))
  }

  fun mockFetchTraktRating(response: Result<RatingDetails>) {
    whenever(
      mock.fetchTraktRating(any(), any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFindById(response: Result<MediaItem>) {
    whenever(mock.findById(any())).thenReturn(flowOf(response))
  }
}
