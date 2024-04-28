package com.andreolas.movierama.details.ui

import com.andreolas.factories.MediaDetailsFactory
import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.MediaItemFactory.toWizard
import com.andreolas.factories.ReviewFactory
import com.andreolas.factories.VideoFactory
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.details.domain.model.MovieDetailsException
import com.andreolas.movierama.details.domain.model.MovieDetailsResult
import com.andreolas.movierama.home.domain.model.MediaType
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

  private val mediaId = 5

  private val similarMovies = MediaItemFactory.MoviesList()

  private val movieDetails = MediaDetailsFactory.FightClub()

  private val reviewsList = ReviewFactory.ReviewList()

  @Test
  fun `successful initialise viewModel`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
          isLoading = false,
          movieDetails = movieDetails,
        )
      )
  }

  @Test
  fun `given success details response then I expect MovieDetails`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
          isLoading = false,
          movieDetails = movieDetails,
        )
      )
  }

  @Test
  fun `given success reviews response then I expect ReviewsList`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
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
          Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
          Result.success(MovieDetailsResult.DetailsSuccess(movieDetails))
        )
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
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
          Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
          Result.success(MovieDetailsResult.SimilarSuccess(similarMovies)),
        )
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
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
          Result.success(MovieDetailsResult.Failure.FatalError()),
          Result.success(MovieDetailsResult.SimilarSuccess(similarMovies)),
          Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
        )
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
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
          Result.success(MovieDetailsResult.Failure.Unknown),
          Result.success(MovieDetailsResult.SimilarSuccess(similarMovies)),
          Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
        )
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
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
          Result.failure(MovieDetailsException()),
          Result.success(MovieDetailsResult.SimilarSuccess(similarMovies)),
          Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
        )
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
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
          Result.failure(Exception()),
          Result.success(MovieDetailsResult.SimilarSuccess(similarMovies)),
          Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
        )
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
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
        response = flowOf(
          Result.success(
            MovieDetailsResult.DetailsSuccess(
              movieDetails.copy(
                isFavorite = true
              )
            )
          )
        )
      )
      .mockMarkAsFavoriteUseCase(
        media = MediaItemFactory.FightClub().toWizard { withFavorite(true) },
        response = Result.success(Unit)
      )
      .buildViewModel(
        id = mediaId,
        mediaType = MediaType.MOVIE
      )
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
          isLoading = false,
          movieDetails = movieDetails.copy(isFavorite = true),
        )
      )
      .onMarkAsFavorite()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
          isLoading = false,
          movieDetails = movieDetails.copy(isFavorite = false),
        )
      )
  }

  @Test
  fun `given movie is not favorite when MaskAsFavorite clicked then I expect to mark it`() =
    runTest {
      testRobot
        .mockFetchMovieDetails(
          response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
        )
        .mockMarkAsFavoriteUseCase(
          media = MediaItemFactory.FightClub(),
          response = Result.success(Unit),
        )
        .buildViewModel(
          id = mediaId,
          mediaType = MediaType.MOVIE
        )
        .assertViewState(
          DetailsViewState(
            mediaType = MediaType.MOVIE,
            movieId = mediaId,
            isLoading = false,
            movieDetails = movieDetails.copy(isFavorite = false),
          )
        )
        .onMarkAsFavorite()
        .assertViewState(
          DetailsViewState(
            mediaType = MediaType.MOVIE,
            movieId = mediaId,
            isLoading = false,
            movieDetails = movieDetails.copy(isFavorite = true),
          )
        )
    }

  @Test
  fun `given success details and movies response then I expect combined flows`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(
          Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)),
          Result.success(MovieDetailsResult.VideosSuccess(VideoFactory.Youtube())),
        )
      )
      .buildViewModel(
        id = mediaId,
        mediaType = MediaType.MOVIE
      )
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          movieId = mediaId,
          isLoading = false,
          movieDetails = movieDetails,
          trailer = VideoFactory.Youtube(),
        )
      )
  }
}
