package com.divinelink.core.testing.repository

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestMoviesRepository {

  val mock: MediaRepository = mock()

  fun mockFetchFavoriteMovies(response: MediaListResult) {
    whenever(
      mock.fetchFavoriteMovies(),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchFavoriteTVSeries(response: MediaListResult) {
    whenever(
      mock.fetchFavoriteTVSeries(),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchFavoriteMoviesIds(response: Result<List<Pair<Int, MediaType>>>) {
    whenever(
      mock.fetchFavoriteIds(),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchPopularMovies(
    request: MoviesRequestApi,
    response: MediaListResult,
  ) {
    whenever(
      mock.fetchPopularMovies(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchSearchMovies(
    request: SearchRequestApi,
    response: MediaListResult,
  ) {
    whenever(
      mock.fetchSearchMovies(request),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockMarkAsFavorite(
    media: MediaItem.Media,
    response: Result<Unit>,
  ) {
    whenever(
      mock.insertFavoriteMedia(media),
    ).thenReturn(
      response,
    )
  }

  suspend fun mockCheckFavorite(
    id: Int,
    mediaType: MediaType,
    response: Result<Boolean>,
  ) {
    whenever(
      mock.checkIfMediaIsFavorite(id, mediaType),
    ).thenReturn(response)
  }

  suspend fun verifyCheckIsFavorite(response: Result<Boolean>) {
    whenever(
      mock.checkIfMediaIsFavorite(any(), any()),
    ).thenReturn(response)
  }

  suspend fun mockRemoveFavorite(
    id: Int,
    mediaType: MediaType,
    response: Result<Unit>,
  ) {
    whenever(
      mock.removeFavoriteMedia(id, mediaType),
    ).thenReturn(
      response,
    )
  }
}
