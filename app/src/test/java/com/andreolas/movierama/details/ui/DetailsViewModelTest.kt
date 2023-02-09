package com.andreolas.movierama.details.ui

import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.details.domain.model.MovieDetails
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.details.domain.model.Review
import com.andreolas.movierama.details.domain.model.SimilarMovie
import gr.divinelink.core.util.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testRobot = DetailsViewModelRobot()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val movieId = 5

    private val similarMovies = (1..10).map {
        SimilarMovie(
            id = 0,
            posterPath = null,
            releaseDate = "release test $it",
            title = "title test $it",
            rating = "$it",
            overview = "overview $it"
        )
    }.toMutableList()

    private val movieDetails = MovieDetails(
        id = 5,
        posterPath = "Lorem ipsum path",
        releaseDate = "Lorem ipsum date",
        title = "Lorem ipsum title",
        rating = "5",
        isFavorite = false,
        overview = "Lorem ipsum overview",
        genres = listOf(),
        director = null,
        cast = listOf(),
        runtime = null,
    )

    private val reviewsList = (1..10).map {
        Review(authorName = "$it", rating = it, content = "$it", date = "2022-22-11")
    }

    @Test
    fun `successful initialise viewModel`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(Result.Loading)
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = true,
                )
            )
    }

    @Test
    fun `given success details response then I expect MovieDetails`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(Result.Success(MovieDetailsResult.DetailsSuccess(movieDetails)))
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    movieDetails = movieDetails,
                )
            )
    }

    @Test
    fun `given success reviews response then I expect ReviewsList`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)))
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = true,
                    reviews = reviewsList,
                )
            )
    }

    @Test
    fun `given success details and reviews response then I expect combined flows`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(
                    Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
                    Result.Success(MovieDetailsResult.DetailsSuccess(movieDetails))
                )
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    reviews = reviewsList,
                    movieDetails = movieDetails,
                )
            )
    }

    @Test
    fun `given success details and similar response then I expect Loading State`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(
                    Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
                    Result.Success(MovieDetailsResult.SimilarSuccess(similarMovies)),
                )
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = true,
                    reviews = reviewsList,
                    similarMovies = similarMovies,
                )
            )
    }

    @Test
    fun `given error I expect FatalError`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(
                    Result.Success(MovieDetailsResult.Failure.FatalError()),
                    Result.Success(MovieDetailsResult.SimilarSuccess(similarMovies)),
                    Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
                )
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    reviews = reviewsList,
                    similarMovies = similarMovies,
                    error = MovieDetailsResult.Failure.FatalError().message
                )
            )
    }

    @Test
    fun `given unknown error I expect general error`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(
                    Result.Success(MovieDetailsResult.Failure.Unknown),
                    Result.Success(MovieDetailsResult.SimilarSuccess(similarMovies)),
                    Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
                )
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    reviews = reviewsList,
                    similarMovies = similarMovies,
                    error = MovieDetailsResult.Failure.Unknown.message
                )
            )
    }

    @Test
    fun `on MovieDetails Exception I expect Fatal Error`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(
                    Result.Error(MovieDetailsException()),
                    Result.Success(MovieDetailsResult.SimilarSuccess(similarMovies)),
                    Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
                )
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    reviews = reviewsList,
                    similarMovies = similarMovies,
                    error = MovieDetailsResult.Failure.FatalError().message
                )
            )
    }

    @Test
    fun `on some other exception I expect Unknown error`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(
                    Result.Error(Exception()),
                    Result.Success(MovieDetailsResult.SimilarSuccess(similarMovies)),
                    Result.Success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
                )
            )
            .buildViewModel(movieId)
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    reviews = reviewsList,
                    similarMovies = similarMovies,
                    error = MovieDetailsResult.Failure.Unknown.message
                )
            )
    }

    @Test
    fun `given movie is liked when MaskAsFavorite clicked then I expect to un mark it`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(Result.Success(MovieDetailsResult.DetailsSuccess(movieDetails.copy(isFavorite = true))))
            )
            .mockMarkAsFavoriteUseCase(
                response = Result.Success(Unit)
            )
            .buildViewModel(
                movieId
            )
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    movieDetails = movieDetails.copy(isFavorite = true),
                )
            )
            .onMarkAsFavorite()
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    movieDetails = movieDetails.copy(isFavorite = false),
                )
            )
    }

    @Test
    fun `given movie is not favorite when MaskAsFavorite clicked then I expect to mark it`() = runTest {
        testRobot
            .mockFetchMovieDetails(
                response = flowOf(Result.Success(MovieDetailsResult.DetailsSuccess(movieDetails)))
            )
            .mockMarkAsFavoriteUseCase(
                response = Result.Success(Unit)
            )
            .buildViewModel(
                movieId
            )
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    movieDetails = movieDetails.copy(isFavorite = false),
                )
            )
            .onMarkAsFavorite()
            .assertViewState(
                DetailsViewState(
                    movieId = movieId,
                    isLoading = false,
                    movieDetails = movieDetails.copy(isFavorite = true),
                )
            )
    }
}
