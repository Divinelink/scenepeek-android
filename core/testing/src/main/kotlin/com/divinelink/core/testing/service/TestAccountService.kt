package com.divinelink.core.testing.service

import com.divinelink.core.network.account.AccountService
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
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
        accountId = any()
      )
    ).thenReturn(response)
  }
}
