package com.divinelink.core.data.account

import app.cash.turbine.test
import com.divinelink.core.network.media.model.movie.map
import com.divinelink.core.network.media.model.tv.map
import com.divinelink.core.testing.dao.TestMediaDao
import com.divinelink.core.testing.factories.api.movie.MoviesResponseApiFactory
import com.divinelink.core.testing.factories.api.tv.TvResponseApiFactory
import com.divinelink.core.testing.service.TestAccountService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class ProdAccountRepositoryTest {

  private val response = MoviesResponseApiFactory.full()

  private var remote = TestAccountService()

  private lateinit var repository: AccountRepository
  private lateinit var dao: TestMediaDao

  @Before
  fun setUp() {
    dao = TestMediaDao()
    repository = ProdAccountRepository(remote.mock, dao.mock)
  }

  @Test
  fun `test fetch movie watchlist successfully`() = runTest {
    remote.mockFetchMoviesWatchlist(
      response = flowOf(response),
    )

    dao.mockFetchFavoriteMovieIds(flowOf(emptyList()))

    val result = repository.fetchMoviesWatchlist(
      accountId = "1",
      page = 1,
      sortBy = "desc",
      sessionId = "sha33dfd2xEemCssDs",
    )

    assertThat(result.first()).isEqualTo(
      Result.success(response.map()),
    )
  }

  @Test
  fun `test fetch tv watchlist successfully`() = runTest {
    val tvResponse = TvResponseApiFactory.full()
    remote.mockFetchTvShowsWatchlist(
      response = flowOf(tvResponse),
    )

    dao.mockFetchFavoriteTvIds(flowOf(emptyList()))

    val result = repository.fetchTvShowsWatchlist(
      accountId = "1",
      page = 1,
      sortBy = "desc",
      sessionId = "sha33dfd2xEemCssDs",
    )

    assertThat(result.first()).isEqualTo(
      Result.success(tvResponse.map()),
    )
  }

  @Test
  fun `test fetch movie watchlist failure`() = runTest {
    repository.fetchMoviesWatchlist(
      accountId = "1",
      page = 1,
      sortBy = "desc",
      sessionId = "sha33dfd2xEemCssDs",
    ).test {
      assertThat(awaitError()).isInstanceOf(Exception::class.java)
    }
  }
}
