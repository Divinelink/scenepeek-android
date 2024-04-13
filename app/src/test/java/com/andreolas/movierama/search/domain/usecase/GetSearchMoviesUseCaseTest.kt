package com.andreolas.movierama.search.domain.usecase

import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.MediaItemFactory.toWizard
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.search.movie.SearchRequestApi
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.MediaType
import com.andreolas.movierama.home.domain.usecase.GetSearchMoviesUseCase
import com.andreolas.movierama.home.domain.usecase.SearchResult
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSearchMoviesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: FakeMoviesRepository

  private val request = SearchRequestApi(query = "test query", page = 1)

  // Movies with id 1, 3, 5, 7, 9 are marked as favorite.
  private val localFavoriteMovies = MediaItemFactory.MoviesList(1..10 step 2).map {
    it.toWizard { withFavorite(true) }
  }

  private val searchResult = MediaItemFactory.MoviesList()

  @Before
  fun setUp() {
    repository = FakeMoviesRepository()
  }

  @Test
  fun `given both favorites and non favorites when I fetch popular then I expect combined list`() =
    runTest {
      val expectedResult = Result.Success(
        SearchResult(
          query = "test query",
          searchList = searchResult.mapIndexed { index, movie ->
            movie.copy(isFavorite = index % 2 == 0)
          }
        )
      )

      repository.mockFetchSearchMovies(
        request = request,
        response = Result.Success(searchResult)
      )

      repository.mockFetchFavoriteMoviesIds(
        response = Result.Success(
          localFavoriteMovies.map {
            Pair(it.id, MediaType.MOVIE)
          }
        )
      )

      val useCase = GetSearchMoviesUseCase(
        moviesRepository = repository.mock,
        dispatcher = testDispatcher,
      )
      val result = useCase(request).last()

      assertThat(result).isEqualTo(expectedResult)
    }

  @Test
  fun `given remote data failed then I expect Error Result`() = runTest {
    val expectedResult = Result.Error(Exception("Something went wrong."))

    repository.mockFetchSearchMovies(
      request = request,
      response = Result.Error(Exception()),
    )
    val useCase = GetSearchMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).first()
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `test error result`() = runTest {
    val expectedResult = Result.Error(Exception("Something went wrong."))

    repository.mockFetchSearchMovies(
      request = request,
      response = Result.Error(Exception()),
    )

    repository.mockFetchFavoriteMovies(
      response = Result.Loading,
    )

    val useCase = GetSearchMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `success loading`() = runTest {
    val expectedResult = Result.Loading

    repository.mockFetchSearchMovies(
      request = SearchRequestApi(page = 0, query = "test query"),
      response = Result.Loading,
    )

    repository.mockFetchFavoriteMoviesIds(
      response = Result.Loading,
    )

    val useCase = GetSearchMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(SearchRequestApi(page = 0, query = "test query")).first()

    assertThat(result).isEqualTo(expectedResult)
  }
}
