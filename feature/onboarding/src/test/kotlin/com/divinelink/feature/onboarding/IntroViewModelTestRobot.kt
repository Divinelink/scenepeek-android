package com.divinelink.feature.onboarding

import app.cash.turbine.test
import com.divinelink.core.domain.jellyseerr.JellyseerrProfileResult
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.model.account.TMDBAccount
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.testing.ViewModelTestRobot
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.TestMarkOnboardingCompleteUseCase
import com.divinelink.feature.onboarding.ui.IntroViewModel
import com.divinelink.feature.onboarding.ui.OnboardingUiState
import kotlinx.coroutines.flow.Flow

class IntroViewModelTestRobot : ViewModelTestRobot<OnboardingUiState>() {

  private val markOnboardingCompleteUseCase = TestMarkOnboardingCompleteUseCase()
  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val getJellyseerrAccountDetailsUseCase = FakeGetJellyseerrDetailsUseCase()
  private val onboardingManager = TestOnboardingManager()

  private lateinit var viewModel: IntroViewModel

  override val actualUiState: Flow<OnboardingUiState>
    get() = viewModel.uiState

  override fun buildViewModel() = apply {
    viewModel = IntroViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
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

  // Mocks
  fun mockOnboardingPages(pages: List<IntroSection>) = apply {
    onboardingManager.setSections(pages)
  }

  fun mockIsInitialOnboarding(isInitialOnboarding: Boolean) = apply {
    onboardingManager.setIsInitialOnboarding(isInitialOnboarding)
  }

  fun mockGetAccountDetails(response: Flow<Result<TMDBAccount>>) = apply {
    getAccountDetailsUseCase.mockSuccess(response)
  }

  fun mockGetJellyseerrAccountDetails(response: Result<JellyseerrProfileResult>) = apply {
    getJellyseerrAccountDetailsUseCase.mockSuccess(response)
  }
}
