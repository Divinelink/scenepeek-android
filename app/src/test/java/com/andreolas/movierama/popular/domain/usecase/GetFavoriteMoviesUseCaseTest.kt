package com.andreolas.movierama.popular.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.factories.MediaItemFactory
import com.andreolas.movierama.factories.MediaItemFactory.wizard
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.usecase.GetFavoriteMoviesUseCase
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetFavoriteMoviesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: FakeMoviesRepository

  @Before
  fun setUp() {
    repository = FakeMoviesRepository()
  }

  private val favorites = MediaItemFactory.MoviesList(1..6).map { movie ->
    movie.wizard {
      withFavorite(true)
    }
  }

  @Test
  fun `successfully fetch favorite movies test`() = runTest {
    val expectedResult = Result.Success(favorites)

    repository.mockFetchFavoriteMovies(
      response = Result.Success(favorites)
    )

    val useCase = GetFavoriteMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(Unit).first()
    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `getFavoriteMovies failure test`() = runTest {
    val expectedResult = Result.Error(Exception("Something went wrong."))

    repository.mockFetchFavoriteMovies(
      response = Result.Error(Exception("Oops"))
    )

    val useCase = GetFavoriteMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(Unit).first()
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
