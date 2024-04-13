package com.andreolas.movierama.popular.domain.usecase

import com.andreolas.factories.MediaItemFactory
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.popular.PopularRequestApi
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.home.domain.usecase.GetPopularMoviesUseCase
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
class GetPopularMoviesUseCaseTest {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()
  private val testDispatcher = mainDispatcherRule.testDispatcher

  private lateinit var repository: FakeMoviesRepository

  private val request = PopularRequestApi(page = 0)

  // Movies with id 1, 3, 5 are marked as favorite.
  private val localFavoriteMovies = MediaItemFactory.MoviesList(1..6 step 2)

  private val remoteMovies = MediaItemFactory.MoviesList(1..6)

  @Before
  fun setUp() {
    repository = FakeMoviesRepository()
  }

  @Test
  fun `successfully fetch popular movies`() =
    runTest {
      val expectedResult = Result.Success(
        remoteMovies.mapIndexed { index, movie ->
          movie.copy(isFavorite = index % 2 == 0)
        }
      )

      repository.mockFetchPopularMovies(
        request = PopularRequestApi(page = 0),
        response = Result.Success(remoteMovies)
      )

      repository.mockFetchFavoriteMoviesIds(
        response = Result.Success(
          localFavoriteMovies.map {
            Pair(it.id, it.mediaType)
          }
        )
      )

      val useCase = GetPopularMoviesUseCase(
        moviesRepository = repository.mock,
        dispatcher = testDispatcher,
      )
      val result = useCase(request).last()

      assertThat(result).isEqualTo(expectedResult)
    }

  @Test
  fun `given local data failed then I expect remote data`() = runTest {
    val expectedResult = Result.Success<List<MediaItem>>(remoteMovies)

    repository.mockFetchFavoriteMoviesIds(
      Result.Error(Exception())
    )

    repository.mockFetchPopularMovies(
      request = PopularRequestApi(page = 0),
      response = Result.Success(remoteMovies)
    )

    remoteMovies.forEach {
      repository.mockCheckFavorite(
        id = it.id,
        mediaType = it.mediaType,
        response = Result.Success(false),
      )
    }

    val useCase = GetPopularMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()
    assertThat(result).isEqualTo(expectedResult)
  }

  @Test
  fun `given remote data failed then I expect Error Result`() = runTest {
    val expectedResult = Result.Error(Exception("Something went wrong."))

    repository.mockFetchFavoriteMovies(
      Result.Success(localFavoriteMovies)
    )

    repository.mockFetchPopularMovies(
      request = PopularRequestApi(page = 0),
      response = Result.Error(Exception())
    )

    val useCase = GetPopularMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `given both data resources failed then I expect Error Results`() = runTest {
    val expectedResult = Result.Error(Exception("Something went wrong."))

    repository.mockFetchFavoriteMovies(
      Result.Error(Exception())
    )

    repository.mockFetchPopularMovies(
      request = PopularRequestApi(page = 0),
      response = Result.Error(Exception())
    )

    val useCase = GetPopularMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).last()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }

  @Test
  fun `success loading`() = runTest {
    val expectedResult = Result.Loading

    repository.mockFetchPopularMovies(
      request = PopularRequestApi(page = 0),
      response = Result.Loading
    )

    repository.mockFetchFavoriteMoviesIds(
      Result.Loading
    )

    val useCase = GetPopularMoviesUseCase(
      moviesRepository = repository.mock,
      dispatcher = testDispatcher,
    )
    val result = useCase(request).first()

    assertThat(result).isInstanceOf(expectedResult::class.java)
  }
}
