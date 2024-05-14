package com.andreolas.movierama.popular.domain.usecase

import com.andreolas.factories.MediaItemFactory
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.usecase.MarkAsFavoriteUseCase
import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MarkAsFavoriteUseCaseTest {

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

    repository.mockMarkAsFavorite(
      media = remoteMovies[2],
      response = Result.success(Unit)
    )

    repository.mockCheckFavorite(
      id = remoteMovies[2].id,
      mediaType = MediaType.MOVIE,
      response = Result.success(true),
    )

    val useCase = MarkAsFavoriteUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(remoteMovies[2])
    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `given movie failed to save I expect Error Result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Failed to mark as favorite."))

    repository.mockMarkAsFavorite(
      media = remoteMovies[2],
      response = Result.failure(Exception())
    )

    val useCase = MarkAsFavoriteUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )

    val result = useCase(remoteMovies[2])
    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
