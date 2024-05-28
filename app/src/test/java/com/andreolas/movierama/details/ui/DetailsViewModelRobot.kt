package com.andreolas.movierama.details.ui

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import com.andreolas.movierama.MainDispatcherRule
import com.andreolas.movierama.fakes.usecase.FakeGetMoviesDetailsUseCase
import com.andreolas.movierama.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeAddToWatchlistUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeDeleteRatingUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeFetchAccountMediaDetailsUseCase
import com.andreolas.movierama.fakes.usecase.details.FakeSubmitRatingUseCase
import com.divinelink.core.model.account.AccountMediaDetails
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelRobot {

  private lateinit var viewModel: DetailsViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeMarkAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val fakeGetMovieDetailsUseCase = FakeGetMoviesDetailsUseCase()
  private val fakeFetchAccountMediaDetailsUseCase = FakeFetchAccountMediaDetailsUseCase()
  private val fakeSubmitRatingUseCase = FakeSubmitRatingUseCase()
  private val fakeDeleteRatingUseCase = FakeDeleteRatingUseCase()
  private val fakeAddToWatchListUseCase = FakeAddToWatchlistUseCase()

  fun buildViewModel(
    id: Int,
    mediaType: MediaType,
  ) = apply {
    viewModel = DetailsViewModel(
      onMarkAsFavoriteUseCase = fakeMarkAsFavoriteUseCase,
      getMovieDetailsUseCase = fakeGetMovieDetailsUseCase.mock,
      fetchAccountMediaDetailsUseCase = fakeFetchAccountMediaDetailsUseCase.mock,
      submitRatingUseCase = fakeSubmitRatingUseCase.mock,
      deleteRatingUseCase = fakeDeleteRatingUseCase.mock,
      addToWatchlistUseCase = fakeAddToWatchListUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to id,
          "mediaType" to mediaType.value,
          "isFavorite" to false,
        )
      ),
    )
  }

  fun getViewModel() = viewModel

  fun assertViewState(
    expectedViewState: DetailsViewState,
  ) = apply {
    assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
  }

  fun mockFetchMovieDetails(
    response: Flow<Result<MovieDetailsResult>>,
  ) = apply {
    fakeGetMovieDetailsUseCase.mockFetchMovieDetails(
      response = response,
    )
  }

  fun onAddRateClicked() = apply {
    viewModel.onAddRateClicked()
  }

  fun onSubmitRate(rating: Int) = apply {
    viewModel.onSubmitRate(rating)
  }

  fun onDeleteRating() = apply {
    viewModel.onClearRating()
  }

  fun onDismissRateDialog() = apply {
    viewModel.onDismissRateDialog()
  }

  fun onNavigateToLogin(
    snackbarResult: SnackbarResult
  ) = apply {
    viewModel.navigateToLogin(snackbarResult)
  }

  fun onMarkAsFavorite() = apply {
    viewModel.onMarkAsFavorite()
  }

  fun consumeSnackbar() = apply {
    viewModel.consumeSnackbarMessage()
  }

  fun consumeNavigation() = apply {
    viewModel.consumeNavigateToLogin()
  }

  fun mockMarkAsFavoriteUseCase(
    media: MediaItem.Media,
    response: Result<Unit>,
  ) = apply {
    fakeMarkAsFavoriteUseCase.mockMarkAsFavoriteResult(
      media = media,
      result = response
    )
  }

  fun mockFetchAccountMediaDetails(
    response: Flow<Result<AccountMediaDetails>>,
  ) = apply {
    fakeFetchAccountMediaDetailsUseCase.mockFetchAccountDetails(
      response = response
    )
  }

  fun mockSubmitRate(
    response: Flow<Result<Unit>>,
  ) = apply {
    fakeSubmitRatingUseCase.mockSubmitRate(
      response = response
    )
  }

  fun mockDeleteRating(
    response: Flow<Result<Unit>>,
  ) = apply {
    fakeDeleteRatingUseCase.mockDeleteRating(
      response = response
    )
  }
}
