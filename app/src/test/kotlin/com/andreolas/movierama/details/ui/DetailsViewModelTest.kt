@file:Suppress("LargeClass")

package com.andreolas.movierama.details.ui

import androidx.compose.material3.SnackbarResult
import com.andreolas.factories.MediaDetailsFactory
import com.andreolas.factories.MediaItemFactory
import com.andreolas.factories.MediaItemFactory.toWizard
import com.andreolas.factories.ReviewFactory
import com.andreolas.factories.VideoFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.andreolas.factories.details.domain.model.account.AccountMediaDetailsFactory.toWizard
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.snackbar.SnackbarMessage
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.model.media.MediaType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

  private val testRobot = DetailsViewModelRobot().apply {
    mockFetchAccountMediaDetails(
      response = flowOf(Result.success(AccountMediaDetailsFactory.NotRated()))
    )
  }

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val mediaId = 1234

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
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
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
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          isLoading = false,
          mediaDetails = movieDetails,
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
          mediaId = mediaId,
          isLoading = true,
          userDetails = AccountMediaDetailsFactory.NotRated(),
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
          mediaId = mediaId,
          isLoading = false,
          reviews = reviewsList,
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withId(mediaId)
          },
          mediaDetails = movieDetails,
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
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
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
          mediaId = mediaId,
          isLoading = false,
          reviews = reviewsList,
          similarMovies = similarMovies,
          error = MovieDetailsResult.Failure.FatalError().message,
          userDetails = AccountMediaDetailsFactory.NotRated()
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
          mediaId = mediaId,
          isLoading = false,
          reviews = reviewsList,
          userDetails = AccountMediaDetailsFactory.NotRated(),
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
          Result.failure(MediaDetailsException()),
          Result.success(MovieDetailsResult.SimilarSuccess(similarMovies)),
          Result.success(MovieDetailsResult.ReviewsSuccess(reviewsList)),
        )
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
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
          mediaId = mediaId,
          isLoading = false,
          reviews = reviewsList,
          similarMovies = similarMovies,
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withId(mediaId)
          },
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
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withId(mediaId)
          },
          mediaDetails = movieDetails.copy(isFavorite = true),
        )
      )
      .onMarkAsFavorite()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withId(mediaId)
          },
          mediaDetails = movieDetails.copy(isFavorite = false),
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
            mediaId = mediaId,
            isLoading = false,
            userDetails = AccountMediaDetailsFactory.NotRated(),
            mediaDetails = movieDetails.copy(isFavorite = false),
          )
        )
        .onMarkAsFavorite()
        .assertViewState(
          DetailsViewState(
            mediaType = MediaType.MOVIE,
            mediaId = mediaId,
            isLoading = false,
            userDetails = AccountMediaDetailsFactory.NotRated(),
            mediaDetails = movieDetails.copy(isFavorite = true),
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
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          trailer = VideoFactory.Youtube(),
        )
      )
  }

  @Test
  fun `given account media details with rated I expect user rating`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockFetchAccountMediaDetails(
        response = flowOf(Result.success(AccountMediaDetailsFactory.Rated()))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated(),
        )
      )
  }

  @Test
  fun `given non rated media I expect no user rating`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockFetchAccountMediaDetails(
        response = flowOf(Result.success(AccountMediaDetailsFactory.NotRated()))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
        )
      )
  }

  @Test
  fun `given success submit rate, when I submit rate, then I expect success message`() {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockSubmitRate(
        response = flowOf(Result.success(Unit))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .onAddRateClicked()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withId(mediaId)
          },
          showRateDialog = true
        )
      )
      .onSubmitRate(5)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(
              R.string.details__rating_submitted_successfully,
              movieDetails.title
            )
          ),
          userDetails = AccountMediaDetailsFactory.Rated().toWizard {
            withId(mediaId)
            withRating(5.0f)
          },
          showRateDialog = false
        )
      )
  }

  @Test
  fun `given NoSession error submit rate, when I submit, then I expect error message`() {
    lateinit var viewModel: DetailsViewModel
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockSubmitRate(
        response = flowOf(Result.failure(SessionException.NoSession()))
      )
      .buildViewModel(mediaId, MediaType.MOVIE).also {
        viewModel = it.getViewModel()
      }
      .onSubmitRate(5)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(R.string.details__must_be_logged_in_to_rate),
            actionLabelText = UIText.ResourceText(R.string.login),
            onSnackbarResult = viewModel::navigateToLogin
          ),
          showRateDialog = false
        )
      )
  }

  @Test
  fun `given NoSession error, when login action clicked, then I expect navigation to login`() {
    lateinit var viewModel: DetailsViewModel
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockSubmitRate(
        response = flowOf(Result.failure(SessionException.NoSession()))
      )
      .buildViewModel(mediaId, MediaType.MOVIE).also {
        viewModel = it.getViewModel()
      }
      .onSubmitRate(5)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          isLoading = false,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(R.string.details__must_be_logged_in_to_rate),
            actionLabelText = UIText.ResourceText(R.string.login),
            onSnackbarResult = viewModel::navigateToLogin
          ),
          showRateDialog = false
        )
      )
      .onNavigateToLogin(SnackbarResult.ActionPerformed)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          isLoading = false,
          snackbarMessage = null,
          navigateToLogin = true
        )
      )
  }

  @Test
  fun `given navigation to login, when I consume it, then I expect navigation to be null`() {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .onNavigateToLogin(SnackbarResult.ActionPerformed)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          navigateToLogin = true
        )
      )
      .consumeNavigation()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          navigateToLogin = null
        )
      )
  }

  @Test
  fun `given snackbar message, when I consume it I expect snackbar message null`() {
    testRobot
      .mockSubmitRate(
        response = flowOf(Result.success(Unit))
      )
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .onAddRateClicked()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withId(mediaId)
          },
          showRateDialog = true
        )
      )
      .onSubmitRate(5)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          snackbarMessage = SnackbarMessage.from(
            UIText.ResourceText(
              R.string.details__rating_submitted_successfully,
              movieDetails.title
            )
          ),
          userDetails = AccountMediaDetailsFactory.Rated().toWizard {
            withId(mediaId)
            withRating(5.0f)
          },
          showRateDialog = false
        )
      )
      .consumeSnackbar()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard {
            withId(mediaId)
            withRating(5.0f)
          },
          showRateDialog = false
        )
      )
  }

  @Test
  fun `test onAddRateClicked opens bottom sheet`() {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .onAddRateClicked()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          showRateDialog = true
        )
      )
  }

  @Test
  fun `test onDismissRateDialog hides dialog`() {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .onAddRateClicked()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          showRateDialog = true
        )
      )
      .onDismissRateDialog()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          showRateDialog = false
        )
      )
  }

  @Test
  fun `given rated movie when I delete rating then I expect no user rating`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockFetchAccountMediaDetails(
        response = flowOf(Result.success(AccountMediaDetailsFactory.Rated()))
      )
      .mockDeleteRating(
        response = flowOf(Result.success(Unit))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated(),
        )
      )
      .onDeleteRating()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard {
            withRating(null)
          },
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.details__rating_deleted_successfully,
              movieDetails.title
            )
          )
        )
      )
  }

  @Test
  fun `given invalid accountId when I add to watchlist I expect error`() = runTest {
    lateinit var viewModel: DetailsViewModel

    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockAddToWatchlist(
        response = flowOf(Result.failure(SessionException.InvalidAccountId()))
      )
      .buildViewModel(mediaId, MediaType.MOVIE).also {
        viewModel = it.getViewModel()
      }
      .onAddToWatchlist()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(R.string.details__must_be_logged_in_to_watchlist),
            actionLabelText = UIText.ResourceText(R.string.login),
            onSnackbarResult = viewModel::navigateToLogin
          )
        )
      )
  }

  @Test
  fun `given error when I add to watchlist I expect general error`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockAddToWatchlist(
        response = flowOf(Result.failure(Exception()))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .onAddToWatchlist()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(R.string.error_retry)
          )
        )
      )
  }

  @Test
  fun `given item on watchlist when I add to watchlist I expect removed message`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockFetchAccountMediaDetails(
        flowOf(
          Result.success(AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(true) })
        )
      )
      .mockAddToWatchlist(flowOf(Result.success(Unit)))
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(true) },
        )
      )
      .onAddToWatchlist()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(false) },
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.details__removed_from_watchlist,
              movieDetails.title
            )
          )
        )
      )
  }

  @Test
  fun `given item not on watchlist when I add to watchlist I expect added message`() = runTest {
    testRobot
      .mockFetchMovieDetails(
        flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .mockFetchAccountMediaDetails(
        flowOf(
          Result.success(AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(false) })
        )
      )
      .mockAddToWatchlist(flowOf(Result.success(Unit)))
      .buildViewModel(mediaId, MediaType.MOVIE)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(false) },
        )
      )
      .onAddToWatchlist()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(true) },
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.details__added_to_watchlist,
              movieDetails.title
            )
          )
        )
      )
  }

  @Test
  fun `test open share dialog`() {
    testRobot
      .mockFetchMovieDetails(
        response = flowOf(Result.success(MovieDetailsResult.DetailsSuccess(movieDetails)))
      )
      .buildViewModel(mediaId, MediaType.MOVIE)
      .onShareClicked(openShareDialog = true)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          openShareDialog = true
        )
      )
      .onShareClicked(openShareDialog = false)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          openShareDialog = false
        )
      )
  }
}
