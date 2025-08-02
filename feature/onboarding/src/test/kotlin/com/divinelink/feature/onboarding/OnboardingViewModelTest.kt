package com.divinelink.feature.onboarding

import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.model.onboarding.OnboardingPage
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

class OnboardingViewModelTest {

  private val robot: OnboardingViewModelTestRobot = OnboardingViewModelTestRobot()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  @Test
  fun `test viewModel init with onboarding pages`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .buildViewModel()
      .assertUiState(
        OnboardingUiState.initial().copy(pages = IntroSections.onboardingSections),
      )
  }

  @Test
  fun `test onPageScroll with initial onboarding starts fetch account job`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(1)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 1,
          pages = IntroSections.onboardingSections,
          startedJobs = listOf("tmdb"),
        ),
      )
  }

  @Test
  fun `test onPageScroll starts fetch account job only once`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(1)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 1,
          pages = IntroSections.onboardingSections,
          startedJobs = listOf("tmdb"),
        ),
      )
      .onPageScroll(0)
      .onPageScroll(1)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 1,
          pages = IntroSections.onboardingSections,
          startedJobs = listOf("tmdb"),
        ),
      )
  }

  @Test
  fun `test onPageScroll to jellyseerr page starts fetch jellyseerr account`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = IntroSections.onboardingSections,
          startedJobs = listOf("jellyseerr"),
        ),
      )
  }

  @Test
  fun `test onPageScroll to jellyseerr page starts fetch jellyseerr account only once`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = IntroSections.onboardingSections,
          startedJobs = listOf("jellyseerr"),
        ),
      )
      .onPageScroll(0)
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = IntroSections.onboardingSections,
          startedJobs = listOf("jellyseerr"),
        ),
      )
  }

  @Test
  fun `test onFetchAccount with success updates page action`() {
    robot
      .mockOnboardingPages(IntroSections.onboardingSections)
      .mockIsInitialOnboarding(true)
      .mockGetAccountDetails(flowOf(Result.success(TMDBAccountFactory.loggedIn())))
      .buildViewModel()
      .onPageScroll(1)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 1,
          pages = listOf(
            OnboardingPage(
              tag = "welcome",
              title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
              description = UIText.ResourceText(
                R.string.feature_onboarding_welcome_page_description,
              ),
              image = null,
              showSkipButton = true,
            ),
            tmdb.copy(action = OnboardingAction.NavigateToTMDBLogin(isComplete = true)),
            jellyseerr,
            linkHandling,
          ),
          startedJobs = listOf("tmdb"),
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
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = listOf(
            OnboardingPage(
              tag = "welcome",
              title = UIText.ResourceText(R.string.feature_onboarding_welcome_page_title),
              description = UIText.ResourceText(
                R.string.feature_onboarding_welcome_page_description,
              ),
              image = null,
              showSkipButton = true,
            ),
            tmdb,
            jellyseerr.copy(
              action = OnboardingAction.NavigateToJellyseerrLogin(isComplete = true),
            ),
            linkHandling,
          ),
          startedJobs = listOf("jellyseerr"),
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
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = IntroSections.onboardingSections,
        ),
      )
  }
}
