package com.divinelink.core.testing.repository

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestMediaRepository {

  val mock: MediaRepository = mock()

  fun mockFetchFavorites(response: MediaListResult) {
    whenever(
      mock.fetchFavorites(),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchPopularMovies(response: MediaListResult) {
    whenever(
      mock.fetchPopularMovies(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  fun mockFetchSearchMovies(response: MediaListResult) {
    whenever(
      mock.fetchSearchMovies(any()),
    ).thenReturn(
      flowOf(response),
    )
  }

  suspend fun mockMarkAsFavorite(media: MediaItem.Media) {
    whenever(
      mock.insertFavoriteMedia(media),
    ).thenReturn(Unit)
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
    response: Unit,
  ) {
    whenever(
      mock.removeFavoriteMedia(id, mediaType),
    ).thenReturn(
      response,
    )
  }
}
