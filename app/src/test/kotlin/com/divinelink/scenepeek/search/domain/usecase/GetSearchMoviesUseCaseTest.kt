package com.divinelink.scenepeek.search.domain.usecase

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.network.media.model.search.movie.SearchRequestApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.scenepeek.home.domain.usecase.GetSearchMoviesUseCase
import com.divinelink.scenepeek.home.domain.usecase.SearchResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class GetSearchMoviesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestMediaRepository

  private val request = SearchRequestApi(query = "test query", page = 1)

  private val searchResult = MediaItemFactory.MoviesList()

  @Before
  fun setUp() {
    repository = TestMediaRepository()
  }

  @Test
  fun `test fetch search movies`() = runTest {
    val expectedResult = SearchResult(
      query = "test query",
      searchList = searchResult.mapIndexed { index, movie ->
        movie.copy(isFavorite = index % 2 == 0)
      },
    )

    repository.mockFetchSearchMovies(
      response = Result.success(
        searchResult.mapIndexed { index, movie ->
          movie.copy(isFavorite = index % 2 == 0)
        },
      ),
    )

    val useCase = GetSearchMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    useCase(request).test {
      assertThat(awaitItem()).isEqualTo(Result.success(expectedResult))
      awaitComplete()
    }
  }

  @Test
  fun `given remote data failed then I expect Error Result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Something went wrong."))

    repository.mockFetchSearchMovies(
      response = Result.failure(Exception()),
    )
    val useCase = GetSearchMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).first()
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `test error result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Something went wrong."))

    repository.mockFetchSearchMovies(
      response = Result.failure(Exception()),
    )

    val useCase = GetSearchMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
