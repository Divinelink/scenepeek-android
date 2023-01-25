package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.fakes.dao.FakeMovieDAO
import com.andreolas.movierama.fakes.repository.FakeDetailsRepository
import com.google.common.truth.Truth.assertThat
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetMoviesDetailsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var repository: FakeDetailsRepository
    private var movieDAO = FakeMovieDAO()

    private val request = DetailsRequestApi(movieId = "555")
    private val movieDetails = MovieDetails(
        id = 0,
        title = "",
        posterPath = "",
        overview = null,
        director = null,
        cast = listOf(),
        releaseDate = "",
        rating = "",
        isFavorite = false,
    )

    @Before
    fun setUp() {
        repository = FakeDetailsRepository()
    }

    @Test
    fun `successfully update favorite status when fetching a saved details movie`() = runTest {
        val expectedResult = Result.Success(movieDetails.copy(isFavorite = true))

        repository.mockFetchFavoriteMovies(
            request = request,
            response = Result.Success(movieDetails)
        )

        movieDAO.mockCheckIfFavorite(
            id = movieDetails.id,
            result = 1,
        )

        val useCase = GetMovieDetailsUseCase(
            repository = repository.mock,
            movieDAO = movieDAO.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).last()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `successfully set favorite status to false when movie is not saved`() = runTest {
        val expectedResult = Result.Success(movieDetails)

        repository.mockFetchFavoriteMovies(
            request = request,
            response = Result.Success(movieDetails)
        )

        movieDAO.mockCheckIfFavorite(
            id = movieDetails.id,
            result = 0,
        )

        val useCase = GetMovieDetailsUseCase(
            repository = repository.mock,
            movieDAO = movieDAO.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).last()

        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `given error result, I expect error result`() = runTest {
        val expectedResult = Result.Error(Exception("Oops."))

        repository.mockFetchFavoriteMovies(
            request = request,
            response = Result.Error(Exception("Oops."))
        )

        movieDAO.mockCheckIfFavorite(
            id = movieDetails.id,
            result = 0,
        )

        val useCase = GetMovieDetailsUseCase(
            repository = repository.mock,
            movieDAO = movieDAO.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).last()

        assertThat(result).isInstanceOf(expectedResult::class.java)
    }

    @Test
    fun `loading state`() = runTest {
        val expectedResult = Result.Loading

        repository.mockFetchFavoriteMovies(
            request = request,
            response = Result.Loading
        )

        movieDAO.mockCheckIfFavorite(
            id = movieDetails.id,
            result = 0,
        )

        val useCase = GetMovieDetailsUseCase(
            repository = repository.mock,
            movieDAO = movieDAO.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).last()

        assertThat(result).isEqualTo(expectedResult)
    }
}
