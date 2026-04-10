package com.divinelink.feature.credits.ui

import com.divinelink.core.model.credits.AggregateCredits
import com.divinelink.core.navigation.route.Navigation.CreditsRoute
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.TestFetchCreditsUseCase
import com.divinelink.core.testing.usecase.TestSpoilersObfuscationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import org.junit.Rule

class CreditsViewModelTestRobot : ViewModelTestRobot<CreditsUiState>() {

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val fetchCreditsUseCase = TestFetchCreditsUseCase()
  private val spoilersObfuscationUseCase = TestSpoilersObfuscationUseCase().useCase()

  private lateinit var viewModel: CreditsViewModel
  private lateinit var navArgs: CreditsRoute

  override val actualUiState: Flow<CreditsUiState>
    get() = viewModel.uiState

  override fun buildViewModel() = apply {
    viewModel = CreditsViewModel(
      route = navArgs,
      fetchCreditsUseCase = fetchCreditsUseCase.mock,
      spoilersObfuscationUseCase = spoilersObfuscationUseCase,
    )
  }

  fun withNavArgs(navArgs: CreditsRoute) = apply {
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

  fun onObfuscateSpoilers() = apply {
    viewModel.onObfuscateSpoilers()
  }
}
