package com.andreolas.movierama.popular.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.popular.dto.PopularRequestApi
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
import com.andreolas.movierama.home.domain.model.PopularMovie
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

    private val request = PopularRequestApi(apiKey = "", page = 0)

    // Movies with id 1, 3, 5 are marked as favorite.
    private val localFavoriteMovies = (1..6 step 2).map { index ->
        PopularMovie(
            id = index,
            posterPath = "",
            releaseDate = "2000",
            title = "Fight Club $index",
            isFavorite = true,
            rating = index.toString(),
        )
    }.toMutableList()

    private val remoteMovies = (1..6).map { index ->
        PopularMovie(
            id = index,
            posterPath = "",
            releaseDate = "2000",
            title = "Fight Club $index",
            isFavorite = false,
            rating = index.toString(),
        )
    }.toMutableList()

    private val mergedListResult = (1..6).map { index ->
        PopularMovie(
            id = index,
            posterPath = "",
            releaseDate = "2000",
            title = "Fight Club $index",
            isFavorite = index % 2 == 1,
            rating = index.toString(),
        )
    }.toMutableList()

    @Before
    fun setUp() {
        repository = FakeMoviesRepository()
    }

    @Test
    fun `given 3 favorite movies and 3 non favorites when I fetch Popular movies then I expect combined list with favorites`() =
        runTest {
            val expectedResult = Result.Success<List<PopularMovie>>(mergedListResult)

            repository.mockFetchFavoriteMovies(
                Result.Success(localFavoriteMovies)
            )

            repository.mockFetchPopularMovies(
                request = PopularRequestApi(apiKey = "", page = 0),
                response = Result.Success(remoteMovies)
            )

            val useCase = GetPopularMoviesUseCase(
                moviesRepository = repository.mock,
                dispatcher = testDispatcher,
            )
            val result = useCase(request).last()

            assertThat(result).isEqualTo(expectedResult)
        }

    @Test
    fun `favorite movie is not shown if it doesn't exist on popular movies`() = runTest {
        val remoteMovies = (1..2).map {
            PopularMovie(
                id = it,
                posterPath = "",
                releaseDate = "",
                title = "",
                isFavorite = false,
                rating = "1",
            )
        }.toMutableList()
        val favoriteMovie = (2..3).map {
            PopularMovie(
                id = it,
                posterPath = "",
                releaseDate = "",
                title = "",
                isFavorite = true,
                rating = "1",
            )
        }.toMutableList()

        val expectedMovies = listOf(
            PopularMovie(
                id = 1,
                posterPath = "",
                releaseDate = "",
                title = "",
                isFavorite = false,
                rating = "1",
            ),
            PopularMovie(
                id = 2,
                posterPath = "",
                releaseDate = "",
                title = "",
                isFavorite = true,
                rating = "1",
            )
        )

        val expectedResult = Result.Success(expectedMovies)

        repository.mockFetchFavoriteMovies(
            Result.Success(favoriteMovie)
        )

        repository.mockFetchPopularMovies(
            request = PopularRequestApi(apiKey = "", page = 0),
            response = Result.Success(remoteMovies)
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
        val expectedResult = Result.Success<List<PopularMovie>>(remoteMovies)

        repository.mockFetchFavoriteMovies(
            Result.Error(Exception())
        )

        repository.mockFetchPopularMovies(
            request = PopularRequestApi(apiKey = "", page = 0),
            response = Result.Success(remoteMovies)
        )

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
            request = PopularRequestApi(apiKey = "", page = 0),
            response = Result.Error(Exception())
        )

        val useCase = GetPopularMoviesUseCase(
            moviesRepository = repository.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).first()

        assertThat(result).isInstanceOf(expectedResult::class.java)
    }

    @Test
    fun `given both data resources failed then I expect Error Results`() = runTest {
        val expectedResult = Result.Error(Exception("Something went wrong."))

        repository.mockFetchFavoriteMovies(
            Result.Error(Exception())
        )

        repository.mockFetchPopularMovies(
            request = PopularRequestApi(apiKey = "", page = 0),
            response = Result.Error(Exception())
        )

        val useCase = GetPopularMoviesUseCase(
            moviesRepository = repository.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).first()

        assertThat(result).isInstanceOf(expectedResult::class.java)
    }
}
