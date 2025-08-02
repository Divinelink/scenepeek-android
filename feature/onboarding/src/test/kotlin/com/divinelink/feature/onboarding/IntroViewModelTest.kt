package com.divinelink.feature.onboarding

import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.feature.onboarding.manager.IntroSections
import com.divinelink.feature.onboarding.manager.IntroSections.jellyseerr
import com.divinelink.feature.onboarding.manager.IntroSections.linkHandling
import com.divinelink.feature.onboarding.manager.IntroSections.tmdb
import com.divinelink.feature.onboarding.ui.OnboardingUiState
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test

class IntroViewModelTest {

  private val robot: IntroViewModelTestRobot = IntroViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test viewModel init with onboarding pages`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .buildViewModel()
      .assertUiState(
        OnboardingUiState.initial().copy(sections = IntroSections.onboardingSections),
      )
  }

  @Test
  fun `test onFetchAccount with success updates feature action`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .mockGetAccountDetails(flowOf(Result.success(TMDBAccountFactory.loggedIn())))
      .buildViewModel()
      .assertUiState(
        OnboardingUiState.initial().copy(
          sections = listOf(
            IntroSection.Header(
              title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
              description = UIText.ResourceText(
                R.string.feature_onboarding_welcome_page_description,
              ),
            ),
            IntroSection.Spacer,
            IntroSection.SecondaryHeader.Features,
            tmdb.copy(action = OnboardingAction.NavigateToTMDBLogin(isComplete = true)),
            jellyseerr,
            linkHandling,
          ),
          isFirstLaunch = true,
        ),
      )
  }

  @Test
  fun `test onFetchJellyseerrAccountDetails with success updates page action`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .mockGetJellyseerrAccountDetails(Result.success(JellyseerrAccountDetailsFactory.jellyseerr()))
      .buildViewModel()
      .assertUiState(
        OnboardingUiState.initial().copy(
          sections = listOf(
            IntroSection.Header(
              title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
              description = UIText.ResourceText(
                R.string.feature_onboarding_welcome_page_description,
              ),
            ),
            IntroSection.Spacer,
            IntroSection.SecondaryHeader.Features,
            tmdb,
            jellyseerr.copy(
              action = OnboardingAction.NavigateToJellyseerrLogin(isComplete = true),
            ),
            linkHandling,
          ),
          isFirstLaunch = true,
        ),
      )
  }

  @Test
  fun `test on onboardingComplete navigates up`() = runTest {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onboardingComplete()
      .assertNavigateUp()
  }

  @Test
  fun `test on not initial onboarding does not update started jobs`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(false)
      .mockGetJellyseerrAccountDetails(Result.success(JellyseerrAccountDetailsFactory.jellyseerr()))
      .buildViewModel()
      .assertUiState(
        OnboardingUiState.initial().copy(
          sections = IntroSections.onboardingSections,
        ),
      )
  }
}
