package com.divinelink.scenepeek.details.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import com.divinelink.core.commons.exception.InvalidStatusException
import com.divinelink.core.data.details.model.MediaDetailsException
import com.divinelink.core.data.session.model.SessionException
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.fixtures.model.details.rating.RatingDetailsFactory
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.model.details.rating.RatingCount
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.expectUiStates
import com.divinelink.core.testing.factories.details.credits.AggregatedCreditsFactory
import com.divinelink.core.testing.factories.model.media.MediaItemFactory
import com.divinelink.core.testing.factories.model.media.MediaItemFactory.toWizard
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.snackbar.SnackbarMessage
import com.divinelink.factories.ReviewFactory
import com.divinelink.factories.VideoFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory
import com.divinelink.factories.details.domain.model.account.AccountMediaDetailsFactory.toWizard
import com.divinelink.feature.details.R
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class DetailsViewModelTest {

  private val testRobot = DetailsViewModelRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val mediaId = 1234

  private val similarMovies = MediaItemFactory.MoviesList()

  private val movieDetails = MediaDetailsFactory.FightClub()
  private val tvDetails = MediaDetailsFactory.TheOffice()

  private val reviewsList = ReviewFactory.ReviewList()

  private fun defaultDetails(
    result: MediaDetailsResult,
    accountDetails: AccountMediaDetails = AccountMediaDetailsFactory.NotRated(),
  ) = flowOf(
    Result.success(result),
    Result.success(MediaDetailsResult.AccountDetailsSuccess(accountDetails)),
  )

  @Test
  fun `successful initialise viewModel`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          ratingSource = RatingSource.TMDB,
        ),
      )
  }

  @Test
  fun `given success details response then I expect MovieDetails`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          isLoading = false,
          mediaDetails = movieDetails,
          ratingSource = RatingSource.TMDB,
        ),
      )
  }

  @Test
  fun `given success details response updates rating source`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.IMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          isLoading = false,
          mediaDetails = movieDetails,
          ratingSource = RatingSource.IMDB,
        ),
      )
  }

  @Test
  fun `given success reviews response then I expect ReviewsList`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(MediaDetailsResult.ReviewsSuccess(reviewsList)),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = true,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          reviews = reviewsList,
        ),
      )
  }

  @Test
  fun `given success details and reviews response then I expect combined flows`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
          Result.success(MediaDetailsResult.ReviewsSuccess(reviewsList)),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.NotRated(),
            ),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
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
        ),
      )
  }

  @Test
  fun `given success details and similar response then I expect Loading State`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.ReviewsSuccess(
              reviewsList,
            ),
          ),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.NotRated(),
            ),
          ),
          Result.success(
            MediaDetailsResult.SimilarSuccess(
              similarMovies,
            ),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          isLoading = true,
          reviews = reviewsList,
          similarMovies = similarMovies,
        ),
      )
  }

  @Test
  fun `given error I expect FatalError`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(MediaDetailsResult.Failure.FatalError()),
          Result.success(
            MediaDetailsResult.SimilarSuccess(
              similarMovies,
            ),
          ),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.NotRated(),
            ),
          ),
          Result.success(
            MediaDetailsResult.ReviewsSuccess(
              reviewsList,
            ),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          reviews = reviewsList,
          similarMovies = similarMovies,
          error = MediaDetailsResult.Failure.FatalError().message,
          userDetails = AccountMediaDetailsFactory.NotRated(),
        ),
      )
  }

  @Test
  fun `given unknown error I expect general error`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(MediaDetailsResult.Failure.Unknown),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.NotRated(),
            ),
          ),
          Result.success(
            MediaDetailsResult.SimilarSuccess(
              similarMovies,
            ),
          ),
          Result.success(
            MediaDetailsResult.ReviewsSuccess(
              reviewsList,
            ),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          reviews = reviewsList,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          similarMovies = similarMovies,
          error = MediaDetailsResult.Failure.Unknown.message,
        ),
      )
  }

  @Test
  fun `on MovieDetails Exception I expect Fatal Error`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.failure(MediaDetailsException()),
          Result.success(
            MediaDetailsResult.SimilarSuccess(
              similarMovies,
            ),
          ),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.NotRated(),
            ),
          ),
          Result.success(
            MediaDetailsResult.ReviewsSuccess(
              reviewsList,
            ),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          isLoading = false,
          reviews = reviewsList,
          similarMovies = similarMovies,
          error = MediaDetailsResult.Failure.FatalError().message,
        ),
      )
  }

  @Test
  fun `on some other exception I expect Unknown error`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.failure(Exception()),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.NotRated(),
            ),
          ),
          Result.success(
            MediaDetailsResult.SimilarSuccess(
              similarMovies,
            ),
          ),
          Result.success(
            MediaDetailsResult.ReviewsSuccess(
              reviewsList,
            ),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
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
          error = MediaDetailsResult.Failure.Unknown.message,
        ),
      )
  }

  @Test
  fun `given movie is liked when MaskAsFavorite clicked then I expect to un mark it`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails.copy(isFavorite = true),
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .mockMarkAsFavoriteUseCase(
        media = MediaItemFactory.FightClub().toWizard { withFavorite(true) },
        response = Result.success(Unit),
      )
      .withNavArguments(
        id = mediaId,
        mediaType = MediaType.MOVIE,
      )
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated().toWizard {
            withId(mediaId)
          },
          mediaDetails = movieDetails.copy(isFavorite = true),
        ),
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
        ),
      )
  }

  @Test
  fun `given movie is not favorite when MaskAsFavorite clicked then I expect to mark it`() =
    runTest {
      testRobot
        .mockFetchMediaDetails(
          response = defaultDetails(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
        )
        .mockMarkAsFavoriteUseCase(
          media = MediaItemFactory.FightClub(),
          response = Result.success(Unit),
        )
        .withNavArguments(
          id = mediaId,
          mediaType = MediaType.MOVIE,
        )
        .buildViewModel()
        .assertViewState(
          DetailsViewState(
            mediaType = MediaType.MOVIE,
            mediaId = mediaId,
            isLoading = false,
            userDetails = AccountMediaDetailsFactory.NotRated(),
            mediaDetails = movieDetails.copy(isFavorite = false),
          ),
        )
        .onMarkAsFavorite()
        .assertViewState(
          DetailsViewState(
            mediaType = MediaType.MOVIE,
            mediaId = mediaId,
            isLoading = false,
            userDetails = AccountMediaDetailsFactory.NotRated(),
            mediaDetails = movieDetails.copy(isFavorite = true),
          ),
        )
    }

  @Test
  fun `given success details and movies response then I expect combined flows`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
          Result.success(
            MediaDetailsResult.VideosSuccess(
              VideoFactory.Youtube(),
            ),
          ),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.NotRated(),
            ),
          ),
        ),
      )
      .withNavArguments(
        id = mediaId,
        mediaType = MediaType.MOVIE,
      )
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          trailer = VideoFactory.Youtube(),
        ),
      )
  }

  @Test
  fun `given account media details with rated I expect user rating`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
          Result.success(
            MediaDetailsResult.AccountDetailsSuccess(
              AccountMediaDetailsFactory.Rated(),
            ),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated(),
        ),
      )
  }

  @Test
  fun `given non rated media I expect no user rating`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
        ),
      )
  }

  @Test
  fun `given success submit rate, when I submit rate, then I expect success message`() {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .mockSubmitRate(
        response = flowOf(Result.success(Unit)),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
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
          showRateDialog = true,
        ),
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
              movieDetails.title,
            ),
          ),
          userDetails = AccountMediaDetailsFactory.Rated().toWizard {
            withId(mediaId)
            withRating(5.0f)
          },
          showRateDialog = false,
        ),
      )
  }

  @Test
  fun `given NoSession error submit rate, when I submit, then I expect error message`() {
    lateinit var viewModel: DetailsViewModel
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .mockSubmitRate(
        response = flowOf(Result.failure(SessionException.Unauthenticated())),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel().also {
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
            onSnackbarResult = viewModel::navigateToLogin,
          ),
          showRateDialog = false,
        ),
      )
  }

  @Test
  fun `given NoSession error, when login action clicked, then I expect navigation to login`() {
    lateinit var viewModel: DetailsViewModel
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .mockSubmitRate(
        response = flowOf(Result.failure(SessionException.Unauthenticated())),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel().also {
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
            onSnackbarResult = viewModel::navigateToLogin,
          ),
          showRateDialog = false,
        ),
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
          navigateToLogin = true,
        ),
      )
  }

  @Test
  fun `given navigation to login, when I consume it, then I expect navigation to be null`() {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onNavigateToLogin(SnackbarResult.ActionPerformed)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          navigateToLogin = true,
        ),
      )
      .consumeNavigation()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          navigateToLogin = null,
        ),
      )
  }

  @Test
  fun `given snackbar message, when I consume it I expect snackbar message null`() {
    testRobot
      .mockSubmitRate(
        response = flowOf(Result.success(Unit)),
      )
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
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
          showRateDialog = true,
        ),
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
              movieDetails.title,
            ),
          ),
          userDetails = AccountMediaDetailsFactory.Rated().toWizard {
            withId(mediaId)
            withRating(5.0f)
          },
          showRateDialog = false,
        ),
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
          showRateDialog = false,
        ),
      )
  }

  @Test
  fun `test onAddRateClicked opens bottom sheet`() {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onAddRateClicked()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          showRateDialog = true,
        ),
      )
  }

  @Test
  fun `test onDismissRateDialog hides dialog`() {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onAddRateClicked()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          showRateDialog = true,
        ),
      )
      .onDismissRateDialog()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          showRateDialog = false,
        ),
      )
  }

  @Test
  fun `given rated movie when I delete rating then I expect no user rating`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          result = MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
          accountDetails = AccountMediaDetailsFactory.Rated(),
        ),
      )
      .mockDeleteRating(
        response = flowOf(Result.success(Unit)),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated(),
        ),
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
              movieDetails.title,
            ),
          ),
        ),
      )
  }

  @Test
  fun `given invalid accountId when I add to watchlist I expect error`() = runTest {
    lateinit var viewModel: DetailsViewModel

    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .mockAddToWatchlist(
        response = flowOf(Result.failure(SessionException.InvalidAccountId())),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel().also {
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
            onSnackbarResult = viewModel::navigateToLogin,
          ),
        ),
      )
  }

  @Test
  fun `given error when I add to watchlist I expect general error`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .mockAddToWatchlist(
        response = flowOf(Result.failure(Exception())),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onAddToWatchlist()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(uiR.string.core_ui_error_retry),
          ),
        ),
      )
  }

  @Test
  fun `given item on watchlist when I add to watchlist I expect removed message`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          result = MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
          accountDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(true) },
        ),
      )
      .mockAddToWatchlist(flowOf(Result.success(Unit)))
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(true) },
        ),
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
              movieDetails.title,
            ),
          ),
        ),
      )
  }

  @Test
  fun `given item not on watchlist when I add to watchlist I expect added message`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          result = MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
          accountDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(false) },
        ),
      )
      .mockAddToWatchlist(flowOf(Result.success(Unit)))
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          mediaDetails = movieDetails,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.Rated().toWizard { withWatchlist(false) },
        ),
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
              movieDetails.title,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test MediaDetailsResult MenuOption updates menu items`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
          Result.success(
            MediaDetailsResult.MenuOptionsSuccess(listOf(DetailsMenuOptions.SHARE)),
          ),
        ),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = movieDetails,
          menuOptions = listOf(DetailsMenuOptions.SHARE),
        ),
      )
  }

  @Test
  fun `test on CreditsSuccess MediaDetailsResult update tvCredits`() = runTest {
    val credits = AggregatedCreditsFactory.credits()
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(MediaDetailsResult.DetailsSuccess(tvDetails, RatingSource.TMDB)),
          Result.success(MediaDetailsResult.CreditsSuccess(credits)),
        ),
      )
      .withNavArguments(mediaId, MediaType.TV)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.TV,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = tvDetails,
          tvCredits = credits,
        ),
      )
  }

  @Test
  fun `test request movie with null success message`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
        ),
      )
      .mockRequestMedia(
        response = flowOf(Result.success(JellyseerrMediaRequest(null))),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onRequestMedia(emptyList())
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = movieDetails,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_details_jellyseerr_success_media_request,
              movieDetails.title,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test request movie with success message`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
        ),
      )
      .mockRequestMedia(
        response = flowOf(Result.success(JellyseerrMediaRequest("Success"))),
      )
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onRequestMedia(emptyList())
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = movieDetails,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.StringText("Success"),
          ),
        ),
      )
  }

  @Test
  fun `test request with 403 prompts to re-login`() = runTest {
    val viewModel: DetailsViewModel
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
        ),
      )
      .mockRequestMedia(flowOf(Result.failure(InvalidStatusException(403))))
      .withNavArguments(
        id = mediaId,
        mediaType = MediaType.MOVIE,
      )
      .buildViewModel().also {
        viewModel = it.getViewModel()
      }
      .onRequestMedia(emptyList())
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = movieDetails,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(uiR.string.core_ui_jellyseerr_session_expired),
            actionLabelText = UIText.ResourceText(uiR.string.core_ui_login),
            duration = SnackbarDuration.Long,
            onSnackbarResult = viewModel::navigateToLogin,
          ),
        ),
      )
  }

  @Test
  fun `test request with 401 prompts to re-login`() = runTest {
    val viewModel: DetailsViewModel
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
        ),
      )
      .mockRequestMedia(flowOf(Result.failure(InvalidStatusException(401))))
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel().also {
        viewModel = it.getViewModel()
      }
      .onRequestMedia(emptyList())
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = movieDetails,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(uiR.string.core_ui_jellyseerr_session_expired),
            actionLabelText = UIText.ResourceText(uiR.string.core_ui_login),
            duration = SnackbarDuration.Long,
            onSnackbarResult = viewModel::navigateToLogin,
          ),
        ),
      )
  }

  @Test
  fun `test request with 409 informs that movie already exists`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
        ),
      )
      .mockRequestMedia(flowOf(Result.failure(InvalidStatusException(409))))
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onRequestMedia(emptyList())
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = movieDetails,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(R.string.feature_details_jellyseerr_request_exists),
          ),
        ),
      )
  }

  @Test
  fun `test request with generic error show generic message`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = flowOf(
          Result.success(
            MediaDetailsResult.DetailsSuccess(
              mediaDetails = movieDetails,
              ratingSource = RatingSource.TMDB,
            ),
          ),
        ),
      )
      .mockRequestMedia(flowOf(Result.failure(InvalidStatusException(500))))
      .withNavArguments(mediaId, MediaType.MOVIE)
      .buildViewModel()
      .onRequestMedia(emptyList())
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = mediaId,
          isLoading = false,
          userDetails = null,
          mediaDetails = movieDetails,
          snackbarMessage = SnackbarMessage.from(
            text = UIText.ResourceText(
              R.string.feature_details_jellyseerr_request_failed,
              movieDetails.title,
            ),
          ),
        ),
      )
  }

  @Test
  fun `test obfuscateSpoilers with initially hidden should show them`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(MediaDetailsResult.DetailsSuccess(tvDetails, RatingSource.TMDB)),
      )
      .mockSpoilersObfuscation(false)
      .withNavArguments(mediaId, MediaType.TV)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.TV,
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = tvDetails,
          spoilersObfuscated = false,
        ),
      )
      .onObfuscateSpoilers()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.TV,
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = tvDetails,
          spoilersObfuscated = true,
        ),
      )
  }

  @Test
  fun `test onFetchAllRatings updates rating source`() = runTest {
    val allRatingsChannel = Channel<Result<Pair<RatingSource, RatingDetails>>>()

    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(MediaDetailsResult.DetailsSuccess(tvDetails, RatingSource.TMDB)),
      )
      .withNavArguments(mediaId, MediaType.TV)
      .buildViewModel()
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.TV,
          mediaId = mediaId,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = tvDetails,
          spoilersObfuscated = false,
        ),
      )
      .mockFetchAllRatingsUseCase(allRatingsChannel)
      .expectUiStates(
        action = {
          onFetchAllRatings()

          launch {
            allRatingsChannel.send(
              Result.success(RatingSource.TRAKT to RatingDetailsFactory.trakt()),
            )
          }

          launch {
            allRatingsChannel.send(
              Result.success(RatingSource.IMDB to RatingDetailsFactory.imdb()),
            )
          }
        },
        uiStates = listOf(
          DetailsViewState(
            mediaType = MediaType.TV,
            mediaId = mediaId,
            isLoading = false,
            userDetails = AccountMediaDetailsFactory.NotRated(),
            mediaDetails = tvDetails.copy(
              ratingCount = RatingCount(
                ratings = mapOf(
                  RatingSource.TMDB to RatingDetails.Score(
                    voteAverage = 9.5,
                    voteCount = 12_345,
                  ),
                  RatingSource.IMDB to RatingDetails.Initial,
                  RatingSource.TRAKT to RatingDetails.Initial,
                ),
              ),
            ),
            spoilersObfuscated = false,
          ),
          DetailsViewState(
            mediaType = MediaType.TV,
            mediaId = mediaId,
            isLoading = false,
            userDetails = AccountMediaDetailsFactory.NotRated(),
            mediaDetails = tvDetails.copy(
              ratingCount = RatingCount(
                ratings = mapOf(
                  RatingSource.TMDB to RatingDetails.Score(
                    voteAverage = 9.5,
                    voteCount = 12_345,
                  ),
                  RatingSource.IMDB to RatingDetails.Initial,
                  RatingSource.TRAKT to RatingDetailsFactory.trakt(),
                ),
              ),
            ),
            spoilersObfuscated = false,
          ),
          DetailsViewState(
            mediaType = MediaType.TV,
            mediaId = mediaId,
            isLoading = false,
            userDetails = AccountMediaDetailsFactory.NotRated(),
            mediaDetails = tvDetails.copy(
              ratingCount = RatingCount(
                ratings = mapOf(
                  RatingSource.TMDB to RatingDetails.Score(
                    voteAverage = 9.5,
                    voteCount = 12_345,
                  ),
                  RatingSource.IMDB to RatingDetailsFactory.imdb(),
                  RatingSource.TRAKT to RatingDetailsFactory.trakt(),
                ),
              ),
            ),
            spoilersObfuscated = false,
          ),
        ),
      )
  }

  @Test
  fun `test onMediaSourceClick for TV show emits openUrl`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(MediaDetailsResult.DetailsSuccess(tvDetails, RatingSource.TMDB)),
      )
      .withNavArguments(2316, MediaType.TV)
      .buildViewModel()
      .onMediaSourceClick(RatingSource.TMDB)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.TV,
          mediaId = 2316,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = tvDetails,
          spoilersObfuscated = false,
        ),
      )
      .assertOpenUrlTab {
        assertThat(awaitItem()).isEqualTo(
          "https://www.themoviedb.org/tv/2316-the-office",
        )
      }
      .onMediaSourceClick(RatingSource.IMDB)
      .assertOpenUrlTab {
        assertThat(awaitItem()).isEqualTo("https://www.imdb.com/title/tt0386676")
      }
      .onMediaSourceClick(RatingSource.TRAKT)
      .assertOpenUrlTab {
        assertThat(awaitItem()).isEqualTo("https://trakt.tv/shows/tt0386676")
      }
  }

  @Test
  fun `test onMediaSourceClick for movie emits openUrl`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails,
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(movieDetails.id, MediaType.MOVIE)
      .buildViewModel()
      .onMediaSourceClick(RatingSource.TMDB)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = movieDetails.id,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails,
          spoilersObfuscated = false,
        ),
      )
      .assertOpenUrlTab {
        assertThat(awaitItem()).isEqualTo(
          "https://www.themoviedb.org/movie/1123-flight-club",
        )
      }
      .onMediaSourceClick(RatingSource.IMDB)
      .assertOpenUrlTab {
        assertThat(awaitItem()).isEqualTo("https://www.imdb.com/title/tt0137523")
      }
      .onMediaSourceClick(RatingSource.TRAKT)
      .assertOpenUrlTab {
        assertThat(awaitItem()).isEqualTo("https://trakt.tv/movies/tt0137523")
      }
  }

  @Test
  fun `test onMediaSourceClick for movie emits openUrls`() = runTest {
    testRobot
      .mockFetchMediaDetails(
        response = defaultDetails(
          MediaDetailsResult.DetailsSuccess(
            mediaDetails = movieDetails.copy(imdbId = null),
            ratingSource = RatingSource.TMDB,
          ),
        ),
      )
      .withNavArguments(movieDetails.id, MediaType.MOVIE)
      .buildViewModel()
      .onMediaSourceClick(RatingSource.TMDB)
      .assertViewState(
        DetailsViewState(
          mediaType = MediaType.MOVIE,
          mediaId = movieDetails.id,
          isLoading = false,
          userDetails = AccountMediaDetailsFactory.NotRated(),
          mediaDetails = movieDetails.copy(imdbId = null),
          spoilersObfuscated = false,
        ),
      )
      .assertOpenUrlTab {
        assertThat(awaitItem()).isEqualTo(
          "https://www.themoviedb.org/movie/1123-flight-club",
        )
      }
      .onMediaSourceClick(RatingSource.IMDB)
      .assertOpenUrlTab { expectNoEvents() }
      .onMediaSourceClick(RatingSource.TRAKT)
      .assertOpenUrlTab { expectNoEvents() }
  }
}
