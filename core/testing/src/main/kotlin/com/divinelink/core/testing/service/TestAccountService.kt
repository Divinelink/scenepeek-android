package com.divinelink.core.testing.service

import com.divinelink.core.network.account.AccountService
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.search.multi.MultiSearchResponseApi
import com.divinelink.core.network.media.model.tv.TvResponseApi
import kotlinx.coroutines.flow.Flow
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class TestAccountService {

  val mock: AccountService = mock()

  fun mockFetchMoviesWatchlist(
    response: Flow<MoviesResponseApi>
  ) {
    whenever(
      mock.fetchMoviesWatchlist(
        page = any(),
        sortBy = any(),
        accountId = any()
      )
    ).thenReturn(response)
  }

  fun mockFetchTvShowsWatchlist(
    response: Flow<TvResponseApi>
  ) {
    whenever(
      mock.fetchTvShowsWatchlist(
        page = any(),
        sortBy = any(),
        accountId = any()
      )
    ).thenReturn(response)
  }
}
