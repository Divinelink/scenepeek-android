package com.divinelink.scenepeek.popular.domain.usecase

import app.cash.turbine.test
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.repository.TestMediaRepository
import com.divinelink.core.domain.GetPopularMoviesUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class GetPopularMoviesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: TestMediaRepository

  private val request = MoviesRequestApi(page = 0)

  private val remoteMovies = MediaItemFactory.MoviesList(1..6)

  @Before
  fun setUp() {
    repository = TestMediaRepository()
  }

  @Test
  fun `successfully fetch popular movies`() = runTest {
    val expectedResult = remoteMovies.mapIndexed { index, movie ->
      movie.copy(isFavorite = index % 2 == 0)
    }

    repository.mockFetchPopularMovies(
      response = Result.success(expectedResult),
    )

    val useCase = GetPopularMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    useCase(request).test {
      assertThat(awaitItem()).isEqualTo(Result.success(expectedResult))
      awaitComplete()
    }
  }

  @Test
  fun `given local data failed then I expect remote data`() = runTest {
    val expectedResult = Result.success<List<MediaItem>>(remoteMovies)

    repository.mockFetchPopularMovies(
      response = Result.success(remoteMovies),
    )

    remoteMovies.forEach {
      repository.mockCheckFavorite(
        id = it.id,
        mediaType = it.mediaType,
        response = Result.success(false),
      )
    }

    val useCase = GetPopularMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()
    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `given remote data failed then I expect Error Result`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Something went wrong."))

    repository.mockFetchPopularMovies(
      response = Result.failure(Exception()),
    )

    val useCase = GetPopularMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `given both data resources failed then I expect Error Results`() = runTest {
    val expectedResult = Result.failure<Exception>(Exception("Something went wrong."))

    repository.mockFetchFavorites(
      Result.failure(Exception()),
    )

    repository.mockFetchPopularMovies(
      response = Result.failure(Exception()),
    )

    val useCase = GetPopularMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
