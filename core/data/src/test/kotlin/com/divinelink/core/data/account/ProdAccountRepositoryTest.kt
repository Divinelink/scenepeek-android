package com.divinelink.core.data.account

import app.cash.turbine.test
import com.divinelink.core.network.media.model.movie.MoviesResponseApi
import com.divinelink.core.network.media.model.movie.toMoviesList
import com.divinelink.core.testing.factories.api.PopularMovieApiFactory
import com.divinelink.core.testing.service.TestAccountService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ProdAccountRepositoryTest {

  private val response = MoviesResponseApi( // TODO Add factory on testing module
    page = 1,
    results = PopularMovieApiFactory.EmptyList(),
    totalPages = 0,
    totalResults = 0
  )

  private var remote = TestAccountService()

  private lateinit var repository: AccountRepository

  @Before
  fun setUp() {
    repository = ProdAccountRepository(remote.mock)
  }

  @Test
  fun `test fetch movie watchlist successfully`() = runTest {
    remote.mockFetchMoviesWatchlist(
      response = flowOf(response)
    )

    val result = repository.fetchMoviesWatchlist(
      accountId = 1,
      page = 1,
    )

    assertThat(result.first()).isEqualTo(Result.success(response.toMoviesList()))
  }

  @Test
  fun `test fetch movie watchlist failure`() = runTest {
    repository.fetchMoviesWatchlist(
      accountId = 1,
      page = 1,
    ).test {
      assertThat(awaitError()).isInstanceOf(Exception::class.java)
    }
  }
}
