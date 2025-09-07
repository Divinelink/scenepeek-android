package com.divinelink.scenepeek.details.ui

import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import com.divinelink.core.domain.credits.SpoilersObfuscationUseCase
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.model.jellyseerr.media.JellyseerrMediaInfo
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.storage.FakePreferenceStorage
import com.divinelink.core.testing.usecase.TestDeleteMediaUseCase
import com.divinelink.core.testing.usecase.TestDeleteRequestUseCase
import com.divinelink.core.testing.usecase.TestFetchAllRatingsUseCase
import com.divinelink.core.testing.usecase.TestMarkAsFavoriteUseCase
import com.divinelink.feature.details.media.ui.DetailsViewModel
import com.divinelink.feature.details.media.ui.DetailsViewState
import com.divinelink.feature.details.media.ui.MediaDetailsResult
import com.divinelink.scenepeek.fakes.usecase.FakeGetMediaDetailsUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeAddToWatchlistUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeDeleteRatingUseCase
import com.divinelink.scenepeek.fakes.usecase.details.FakeSubmitRatingUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Rule

class DetailsViewModelRobot : ViewModelTestRobot<DetailsViewState>() {

  private lateinit var navArgs: DetailsRoute
  private lateinit var viewModel: DetailsViewModel

  override val actualUiState: Flow<DetailsViewState>
    get() = viewModel.viewState

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fakeMarkAsFavoriteUseCase = TestMarkAsFavoriteUseCase()
  private val fakeGetMovieDetailsUseCase = FakeGetMediaDetailsUseCase()
  private val fakeSubmitRatingUseCase = FakeSubmitRatingUseCase()
  private val fakeDeleteRatingUseCase = FakeDeleteRatingUseCase()
  private val fakeAddToWatchListUseCase = FakeAddToWatchlistUseCase()
  private val testFetchAllRatingsUseCase = TestFetchAllRatingsUseCase()
  private val testDeleteRequestUseCase = TestDeleteRequestUseCase()
  private val testDeleteMediaUseCase = TestDeleteMediaUseCase()
  private val spoilersObfuscationUseCase = SpoilersObfuscationUseCase(
    preferenceStorage = FakePreferenceStorage(),
    dispatcherProvider = mainDispatcherRule.testDispatcher,
  )

  override fun buildViewModel() = apply {
    viewModel = DetailsViewModel(
      onMarkAsFavoriteUseCase = fakeMarkAsFavoriteUseCase,
      getMediaDetailsUseCase = fakeGetMovieDetailsUseCase.mock,
      submitRatingUseCase = fakeSubmitRatingUseCase.mock,
      deleteRatingUseCase = fakeDeleteRatingUseCase.mock,
      addToWatchlistUseCase = fakeAddToWatchListUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
      fetchAllRatingsUseCase = testFetchAllRatingsUseCase.mock,
      deleteRequestUseCase = testDeleteRequestUseCase.mock,
      deleteMediaUseCase = testDeleteMediaUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
          "mediaType" to navArgs.mediaType,
          "isFavorite" to false,
        ),
      ),
    )
  }

  fun getViewModel() = viewModel

  fun assertViewState(expectedViewState: DetailsViewState) = apply {
    assertThat(viewModel.viewState.value).isEqualTo(expectedViewState)
  }

  fun assertOpenUrlTab(validate: suspend TurbineTestContext<String>.() -> Unit) = apply {
    runTest {
      viewModel.openUrlTab.test {
        validate()
      }
    }
  }

  fun mockFetchMediaDetails(response: Flow<Result<MediaDetailsResult>>) = apply {
    fakeGetMovieDetailsUseCase.mockFetchMediaDetails(
      response = response,
    )
  }

  fun mockFetchMediaDetails(response: Channel<Result<MediaDetailsResult>>) = apply {
    fakeGetMovieDetailsUseCase.mockFetchMediaDetails(
      response = response,
    )
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

  fun onNavigateToLogin(snackbarResult: SnackbarResult) = apply {
    viewModel.navigateToLogin(snackbarResult)
  }

  fun onMarkAsFavorite() = apply {
    viewModel.onMarkAsFavorite()
  }

  fun onUpdateMediaInfo(mediaInfo: JellyseerrMediaInfo) = apply {
    viewModel.onUpdateMediaInfo(mediaInfo)
  }

  fun onFetchAllRatings() = apply {
    viewModel.onFetchAllRatings()
  }

  fun onObfuscateSpoilers() = apply {
    viewModel.onObfuscateSpoilers()
  }

  fun onDeleteRequest(id: Int) = apply {
    viewModel.onDeleteRequest(id)
  }

  fun onDeleteMedia(deleteFile: Boolean) = apply {
    viewModel.onDeleteMedia(deleteFile)
  }

  fun consumeSnackbar() = apply {
    viewModel.consumeSnackbarMessage()
  }

  fun consumeNavigation() = apply {
    viewModel.consumeNavigateToLogin()
  }

  fun withNavArguments(
    id: Int,
    mediaType: MediaType,
    isFavorite: Boolean = false,
  ) = apply {
    navArgs = DetailsRoute(
      id = id,
      mediaType = mediaType,
      isFavorite = isFavorite,
    )
  }

  fun onTabSelected(index: Int) = apply {
    viewModel.onTabSelected(index)
  }

  // Mock Functions

  fun mockMarkAsFavoriteUseCase(
    media: MediaItem.Media,
    response: Result<Boolean>,
  ) = apply {
    fakeMarkAsFavoriteUseCase.mockMarkAsFavoriteResult(
      media = media,
      result = response,
    )
  }

  suspend fun mockSubmitRate(response: Result<Unit>) = apply {
    fakeSubmitRatingUseCase.mockSubmitRate(
      response = response,
    )
  }

  suspend fun mockDeleteRating(response: Result<Unit>) = apply {
    fakeDeleteRatingUseCase.mockDeleteRating(
      response = response,
    )
  }

  suspend fun mockAddToWatchlist(response: Result<Unit>) = apply {
    fakeAddToWatchListUseCase.mockAddToWatchlist(
      response = response,
    )
  }

  fun mockDeleteRequest(response: Flow<Result<JellyseerrMediaInfo>>) = apply {
    testDeleteRequestUseCase.mockSuccess(
      response = response,
    )
  }

  suspend fun mockDeleteMedia(response: Result<Unit>) = apply {
    testDeleteMediaUseCase.mockSuccess(
      response = response,
    )
  }

  fun onMediaSourceClick(source: RatingSource) = apply {
    viewModel.onMediaSourceClick(source)
  }

  suspend fun mockSpoilersObfuscation(obfuscated: Boolean) = apply {
    spoilersObfuscationUseCase.setSpoilersObfuscation(obfuscated)
  }

  fun mockFetchAllRatingsUseCase(response: Channel<Result<Pair<RatingSource, RatingDetails>>>) =
    apply { testFetchAllRatingsUseCase.mockSuccess(response) }
  // End Mock Functions
}
