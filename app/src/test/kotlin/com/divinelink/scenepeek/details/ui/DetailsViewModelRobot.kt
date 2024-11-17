package com.divinelink.scenepeek.details.ui

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.model.jellyseerr.request.JellyseerrMediaRequest
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.usecase.FakeRequestMediaUseCase
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.scenepeek.fakes.usecase.FakeGetMediaDetailsUseCase
import com.divinelink.scenepeek.fakes.usecase.FakeMarkAsFavoriteUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeAddToWatchlistUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeDeleteRatingUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeSubmitRatingUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class DetailsViewModelRobot {

  private lateinit var viewModel: DetailsViewModel

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeMarkAsFavoriteUseCase = FakeMarkAsFavoriteUseCase()
  private val fakeGetMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
  private val fakeSubmitRatingUseCase = FakeSubmitRatingUseCase()
  private val fakeDeleteRatingUseCase = FakeDeleteRatingUseCase()
  private val fakeAddToWatchListUseCase = FakeAddToWatchlistUseCase()
  private val fakeRequestMediaUseCase = FakeRequestMediaUseCase()
  private val spoilersObfuscationUseCase = SpoilersObfuscationUseCase(
    preferenceStorage = FakePreferenceStorage(),
    dispatcherProvider = mainDispatcherRule.testDispatcher,
  )

  fun buildViewModel(
    id: Int,
    mediaType: MediaType,
  ) = apply {
    viewModel = DetailsViewModel(
      onMarkAsFavoriteUseCase = fakeMarkAsFavoriteUseCase,
      getMediaDetailsUseCase = fakeGetMovieDetailsUseCase.mock,
      submitRatingUseCase = fakeSubmitRatingUseCase.mock,
      deleteRatingUseCase = fakeDeleteRatingUseCase.mock,
      addToWatchlistUseCase = fakeAddToWatchListUseCase.mock,
      requestMediaUseCase = fakeRequestMediaUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to id,
          "mediaType" to mediaType.value,
          "isFavorite" to false,
        ),
      ),
    )
  }

  fun getViewModel() = viewModel

  fun assertViewState(expectedViewState: DetailsViewState) = apply {
    assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
  }

  fun mockFetchMediaDetails(response: Flow<Result<MediaDetailsResult>>) = apply {
    fakeGetMovieDetailsUseCase.mockFetchMediaDetails(
      response = response,
    )
  }

  fun mockRequestMedia(response: Flow<Result<JellyseerrMediaRequest>>) = apply {
    fakeRequestMediaUseCase.mockSuccess(response = response)
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

  fun onAddToWatchlist() = apply {
    viewModel.onAddToWatchlist()
  }

  fun onDismissRateDialog() = apply {
    viewModel.onDismissRateDialog()
  }

  fun onNavigateToLogin(snackbarResult: SnackbarResult) = apply {
    viewModel.navigateToLogin(snackbarResult)
  }

  fun onMarkAsFavorite() = apply {
    viewModel.onMarkAsFavorite()
  }

  fun onRequestMedia(seasons: List<Int>) = apply {
    viewModel.onRequestMedia(seasons)
  }

  fun onObfuscateSpoilers() = apply {
    viewModel.onObfuscateSpoilers()
  }

  fun consumeSnackbar() = apply {
    viewModel.consumeSnackbarMessage()
  }

  fun consumeNavigation() = apply {
    viewModel.consumeNavigateToLogin()
  }

  // Mock Functions

  fun mockMarkAsFavoriteUseCase(
    media: MediaItem.Media,
    response: Result<Unit>,
  ) = apply {
    fakeMarkAsFavoriteUseCase.mockMarkAsFavoriteResult(
      media = media,
      result = response,
    )
  }

  fun mockSubmitRate(response: Flow<Result<Unit>>) = apply {
    fakeSubmitRatingUseCase.mockSubmitRate(
      response = response,
    )
  }

  fun mockDeleteRating(response: Flow<Result<Unit>>) = apply {
    fakeDeleteRatingUseCase.mockDeleteRating(
      response = response,
    )
  }

  fun mockAddToWatchlist(response: Flow<Result<Unit>>) = apply {
    fakeAddToWatchListUseCase.mockAddToWatchlist(
      response = response,
    )
  }

  suspend fun mockSpoilersObfuscation(obfuscated: Boolean) = apply {
    spoilersObfuscationUseCase.setSpoilersObfuscation(obfuscated)
  }
  // End Mock Functions
}
