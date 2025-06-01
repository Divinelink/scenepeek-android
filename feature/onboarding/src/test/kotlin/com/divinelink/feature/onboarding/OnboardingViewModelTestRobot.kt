package com.divinelink.feature.onboarding

import app.cash.turbine.test
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrAccountDetails
import com.divinelink.core.model.onboarding.OnboardingPage
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.TestMarkOnboardingCompleteUseCase
import com.divinelink.feature.onboarding.ui.OnboardingUiState
import com.divinelink.feature.onboarding.ui.OnboardingViewModel
import kotlinx.coroutines.flow.Flow

class OnboardingViewModelTestRobot : ViewModelTestRobot<OnboardingUiState>() {

  private val markOnboardingCompleteUseCase = TestMarkOnboardingCompleteUseCase()
  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val getJellyseerrAccountDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
  private val onboardingManager = TestOnboardingManager()

  private lateinit var viewModel: OnboardingViewModel

  override val actualUiState: Flow<OnboardingUiState>
    get() = viewModel.uiState

  override fun buildViewModel() = apply {
    viewModel = OnboardingViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrAccountDetailsUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = onboardingManager,
    )
  }

  suspend fun assertNavigateUp() = apply {
    viewModel.onNavigateUp.test {
      this.awaitItem()
    }
  }

  fun onboardingComplete() = apply {
    viewModel.onboardingComplete()
  }

  fun onPageScroll(index: Int) = apply {
    viewModel.onPageScroll(index)
  }

  // Mocks
  fun mockOnboardingPages(pages: List<OnboardingPage>) = apply {
    onboardingManager.setOnboardingPages(pages)
  }

  fun mockIsInitialOnboarding(isInitialOnboarding: Boolean) = apply {
    onboardingManager.setIsInitialOnboarding(isInitialOnboarding)
  }

  fun mockGetAccountDetails(response: Result<AccountDetails>) = apply {
    getAccountDetailsUseCase.mockSuccess(response)
  }

  fun mockGetJellyseerrAccountDetails(response: Result<JellyseerrAccountDetails?>) = apply {
    getJellyseerrAccountDetailsUseCase.mockSuccess(response)
  }
}
