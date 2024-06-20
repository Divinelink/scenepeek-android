package com.andreolas.movierama.popular.domain.usecase

import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.network.media.model.movie.MoviesRequestApi
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.factories.model.media.MediaItemFactory
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetPopularMoviesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: FakeMoviesRepository

  private val request = MoviesRequestApi(page = 0)

  // Movies with id 1, 3, 5 are marked as favorite.
  private val localFavoriteMovies = MediaItemFactory.MoviesList(1..6 step 2)

  private val remoteMovies = MediaItemFactory.MoviesList(1..6)

  @Before
  fun setUp() {
    repository = FakeMoviesRepository()
  }

  @Test
  fun `successfully fetch popular movies`() = runTest {
    val expectedResult = Result.success(
      remoteMovies.mapIndexed { index, movie ->
        movie.copy(isFavorite = index % 2 == 0)
      },
    )

    repository.mockFetchPopularMovies(
      request = MoviesRequestApi(page = 0),
      response = Result.success(remoteMovies),
    )

    repository.mockFetchFavoriteMoviesIds(
      response = Result.success(
        localFavoriteMovies.map {
          Pair(it.id, it.mediaType)
        },
      ),
    )

    val useCase = GetPopularMoviesUseCase(
      repository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `given local data failed then I expect remote data`() = runTest {
    val expectedResult = Result.success<List<MediaItem>>(remoteMovies)

    repository.mockFetchFavoriteMoviesIds(
      Result.failure(Exception()),
    )

    repository.mockFetchPopularMovies(
      request = MoviesRequestApi(page = 0),
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

    repository.mockFetchFavoriteMovies(
      Result.success(localFavoriteMovies),
    )

    repository.mockFetchPopularMovies(
      request = MoviesRequestApi(page = 0),
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

    repository.mockFetchFavoriteMovies(
      Result.failure(Exception()),
    )

    repository.mockFetchPopularMovies(
      request = MoviesRequestApi(page = 0),
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
