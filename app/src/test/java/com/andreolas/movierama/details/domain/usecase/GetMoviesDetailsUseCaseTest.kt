package com.andreolas.movierama.details.domain.usecase

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.base.data.remote.movies.dto.details.DetailsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.reviews.ReviewsRequestApi
import com.andreolas.movierama.base.data.remote.movies.dto.details.similar.SimilarRequestApi
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.SimilarMovie
import com.andreolas.movierama.fakes.repository.FakeDetailsRepository
import com.andreolas.movierama.fakes.repository.FakeMoviesRepository
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
class GetMoviesDetailsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val testDispatcher = mainDispatcherRule.testDispatcher

    private lateinit var repository: FakeDetailsRepository
    private lateinit var moviesRepository: FakeMoviesRepository

    private val request = DetailsRequestApi(movieId = 555)
    private val movieDetails = MovieDetails(
        id = 0,
        title = "",
        posterPath = "",
        overview = null,
        director = null,
        genres = listOf(),
        cast = listOf(),
        releaseDate = "",
        rating = "",
        isFavorite = false,
        runtime = "50m",
    )

    private val reviewsList = (1..10).map {
        Review(
            authorName = "Lorem Ipsum name $it",
            rating = it,
            content = "Lorame Ipsum content $it",
            date = "2022-10-10"
        )
    }.toList()

    private val similarList = (1..10).map {
        SimilarMovie(id = 0, posterPath = null, releaseDate = "", title = "", rating = "", overview = "")
    }.toList()

    @Before
    fun setUp() {
        repository = FakeDetailsRepository()
        moviesRepository = FakeMoviesRepository()
    }

    @Test
    fun `execute should return Loading state`() = runTest {
        repository.mockFetchMovieDetails(request, Result.Loading)
        repository.mockFetchMovieReviews(ReviewsRequestApi(555), Result.Loading)
        repository.mockFetchSimilarMovies(SimilarRequestApi(movieId = 555), Result.Loading)
        val flow = GetMovieDetailsUseCase(
            repository = repository.mock,
            moviesRepository = moviesRepository.mock,
            dispatcher = testDispatcher,
        )

        val result = flow(request).first()

        assertThat(result).isEqualTo(Result.Loading)
    }

    @Test
    fun `successfully get movie details`() = runTest {
        moviesRepository.mockCheckFavorite(555, Result.Success(true))
        repository.mockFetchMovieDetails(request, Result.Success(movieDetails))
        repository.mockFetchMovieReviews(ReviewsRequestApi(555), Result.Loading)
        repository.mockFetchSimilarMovies(SimilarRequestApi(movieId = 555), Result.Loading)
        val flow = GetMovieDetailsUseCase(
            repository = repository.mock,
            moviesRepository = moviesRepository.mock,
            dispatcher = testDispatcher,
        )

        val result = flow(request).first()

        assertThat(result).isEqualTo(Result.Success(MovieDetailsResult.DetailsSuccess(movieDetails.copy(isFavorite = true))))
    }

    @Test
    fun `successfully get movie details with false favorite status`() = runTest {
        moviesRepository.mockCheckFavorite(555, Result.Success(false))
        repository.mockFetchMovieDetails(request, Result.Success(movieDetails))
        repository.mockFetchMovieReviews(ReviewsRequestApi(555), Result.Loading)
        repository.mockFetchSimilarMovies(SimilarRequestApi(movieId = 555), Result.Loading)
        val flow = GetMovieDetailsUseCase(
            repository = repository.mock,
            moviesRepository = moviesRepository.mock,
            dispatcher = testDispatcher,
        )

        val result = flow(request).first()

        assertThat(result).isEqualTo(Result.Success(MovieDetailsResult.DetailsSuccess(movieDetails)))
    }

    @Test
    fun `successfully fetch movie reviews`() = runTest {
        repository.mockFetchMovieDetails(request, Result.Loading)
        repository.mockFetchMovieReviews(ReviewsRequestApi(movieId = 555), Result.Success(reviewsList))
        repository.mockFetchSimilarMovies(SimilarRequestApi(movieId = 555), Result.Loading)
        val flow = GetMovieDetailsUseCase(
            repository = repository.mock,
            moviesRepository = moviesRepository.mock,
            dispatcher = testDispatcher,
        )

        val result = flow(request).last()

        assertThat(result).isEqualTo(Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)))
    }

    @Test
    fun `successfully fetch similar movies`() = runTest {
        repository.mockFetchMovieDetails(request, Result.Loading)
        repository.mockFetchMovieReviews(ReviewsRequestApi(movieId = 555), Result.Success(reviewsList))
        repository.mockFetchSimilarMovies(SimilarRequestApi(movieId = 555), Result.Success(similarList))
        val flow = GetMovieDetailsUseCase(
            repository = repository.mock,
            moviesRepository = moviesRepository.mock,
            dispatcher = testDispatcher,
        )

        val result = flow(request).last()

        assertThat(result).isEqualTo(Result.Success(MovieDetailsResult.SimilarSuccess(similarList)))
    }

    @Test
    fun `given error result, I expect error result`() = runTest {
        val expectedResult = Result.Error(Exception("Oops."))

        repository.mockFetchMovieDetails(
            request = request,
            response = Result.Error(Exception("Oops."))
        )

        val useCase = GetMovieDetailsUseCase(
            repository = repository.mock,
            moviesRepository = moviesRepository.mock,
            dispatcher = testDispatcher,
        )
        val result = useCase(request).last()

        assertThat(result).isInstanceOf(expectedResult::class.java)
    }
}
