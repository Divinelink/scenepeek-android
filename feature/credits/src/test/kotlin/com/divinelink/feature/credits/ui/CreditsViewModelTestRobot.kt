package com.divinelink.feature.credits.ui

import androidx.lifecycle.SavedStateHandle
import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchCreditsUseCase
import com.divinelink.feature.credits.navigation.CreditsNavArguments
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow

class CreditsViewModelTestRobot : ViewModelTestRobot<CreditsUiState>() {

  private val fetchCreditsUseCase = TestFetchCreditsUseCase()

  private lateinit var viewModel: CreditsViewModel
  private lateinit var navArgs: CreditsNavArguments

  override val actualUiState: Flow<CreditsUiState>
    get() = viewModel.uiState

  override fun buildViewModel() = apply {
    viewModel = CreditsViewModel(
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      savedStateHandle = SavedStateHandle(
        mapOf(
          "id" to navArgs.id,
          "mediaType" to navArgs.mediaType,
        ),
      ),
    )
  }

  fun withNavArgs(navArgs: CreditsNavArguments) = apply {
    this.navArgs = navArgs
  }

  fun mockFetchCreditsUseCaseSuccess(result: Flow<Result<AggregateCredits>>) = apply {
    fetchCreditsUseCase.mockSuccess(result)
  }

  fun setupChannelForUseCase(result: Channel<Result<AggregateCredits>>) = apply {
    fetchCreditsUseCase.mockSuccess(result)
  }

  fun onTabSelected(tabIndex: Int) = apply {
    viewModel.onTabSelected(tabIndex)
  }
}
