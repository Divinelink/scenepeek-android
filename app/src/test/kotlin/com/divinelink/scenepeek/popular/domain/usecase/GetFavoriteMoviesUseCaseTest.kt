package com.divinelink.scenepeek.popular.domain.usecase

import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.fixtures.model.media.MediaItemFactory.toWizard
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.scenepeek.home.domain.usecase.GetFavoriteMoviesUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class GetFavoriteMoviesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestMediaRepository

  @Before
  fun setUp() {
    repository = TestMediaRepository()
  }

  private val favorites = MediaItemFactory.MoviesList(1..6).map { movie ->
    movie.toWizard {
      withFavorite(true)
    }
  }

  @Test
  fun `successfully fetch favorite movies test`() = runTest {
    val expectedResult = Result.success(favorites)

    repository.mockFetchFavorites(
      response = Result.success(favorites),
    )

    val useCase = GetFavoriteMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(Unit).first()
    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `getFavoriteMovies failure test`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Something went wrong."))

    repository.mockFetchFavorites(
      response = Result.failure(Exception("Oops")),
    )

    val useCase = GetFavoriteMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(Unit).first()
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
