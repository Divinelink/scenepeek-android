package com.divinelink.core.testing.repository

import com.divinelink.core.data.account.AccountRepository
import com.divinelink.core.model.PaginationData
import com.divinelink.core.model.media.MediaItem
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestAccountRepository {
  val mock: AccountRepository = mock()

  suspend fun mockFetchMoviesWatchlist(response: Flow<Result<PaginationData<MediaItem.Media>>>) {
    whenever(
      mock.fetchMoviesWatchlist(
        page = any(),
        sortBy = any(),
        accountId = any(),
        sessionId = any(),
      ),
    ).thenReturn(response)
  }

  suspend fun mockFetchTvShowsWatchlist(response: Flow<Result<PaginationData<MediaItem.Media>>>) {
    whenever(
      mock.fetchTvShowsWatchlist(
        page = any(),
        sortBy = any(),
        accountId = any(),
        sessionId = any(),
      ),
    ).thenReturn(response)
  }
}
