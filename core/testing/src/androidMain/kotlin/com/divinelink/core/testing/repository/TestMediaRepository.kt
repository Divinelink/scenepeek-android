package com.divinelink.core.testing.repository

import com.divinelink.core.data.media.repository.MediaListResult
import com.divinelink.core.data.media.repository.MediaRepository
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.details.Season
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.search.MultiSearch
import kotlinx.coroutines.flow.Flow
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

  suspend fun mockFetchTrending(
    page: Int = any(),
    response: Flow<Result<PaginationData<MediaItem>>>,
  ) {
    whenever(
      mock.fetchTrending(
        page = page,
      ),
    ).thenReturn(response)
  }

  fun mockFetchMediaLists(
    response: Flow<Result<PaginationData<MediaItem>>>,
  ) {
    whenever(
      mock.fetchMediaLists(
        request = any(),
        page = any(),
      ),
    ).thenReturn(response)
  }

  fun mockFetchSearchMovies(response: Result<MultiSearch>) {
    whenever(
      mock.fetchSearchMovies(
        mediaType = any(),
        request = any(),
      ),
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

  fun mockFetchTvSeasons(response: Result<List<Season>>) {
    whenever(mock.fetchTvSeasons(any())).thenReturn(flowOf(response))
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
