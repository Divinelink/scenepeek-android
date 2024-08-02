package com.andreolas.movierama.popular.domain.usecase

import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.usecase.RemoveFavoriteUseCase
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.media.MediaItemFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class RemoveFavoriteUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: FakeMoviesRepository

  @Before
  fun setUp() {
    repository = FakeMoviesRepository()
  }

  private val remoteMovies = MediaItemFactory.MoviesList()

  @Test
  fun `given movies is successfully saved I expect Success Result`() = runTest {
    val expectedResult = Result.success(Unit)

    repository.mockRemoveFavorite(
      id = remoteMovies[2].id,
      mediaType = MediaType.MOVIE,
      response = Result.success(Unit),
    )

    val useCase = RemoveFavoriteUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(remoteMovies[2].id)
    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `given movie failed to save I expect Error Result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Failed to mark as favorite."))

    repository.mockRemoveFavorite(
      id = remoteMovies[2].id,
      mediaType = MediaType.MOVIE,
      response = Result.failure(Exception()),
    )

    val useCase = RemoveFavoriteUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(remoteMovies[2].id)
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
