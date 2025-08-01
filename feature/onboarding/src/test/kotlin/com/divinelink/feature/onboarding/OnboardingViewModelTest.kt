package com.divinelink.feature.onboarding

import com.divinelink.core.fixtures.model.account.TMDBAccountFactory
import com.divinelink.core.fixtures.model.jellyseerr.JellyseerrAccountDetailsFactory
import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.OnboardingAction
import com.divinelink.core.model.onboarding.OnboardingPage
import com.divinelink.core.testing.MainDispatcherRule
import com.divinelink.core.testing.assertUiState
import com.divinelink.feature.onboarding.manager.OnboardingPages
import com.divinelink.feature.onboarding.manager.OnboardingPages.jellyseerrPage
import com.divinelink.feature.onboarding.manager.OnboardingPages.linkHandlingPage
import com.divinelink.feature.onboarding.manager.OnboardingPages.tmdbPage
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
      .mockOnboardingPages(OnboardingPages.initialPages)
      .buildViewModel()
      .assertUiState(
        OnboardingUiState.initial().copy(pages = OnboardingPages.initialPages),
      )
  }

  @Test
  fun `test onPageScroll with initial onboarding starts fetch account job`() {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(1)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 1,
          pages = OnboardingPages.initialPages,
          startedJobs = listOf("tmdb"),
        ),
      )
  }

  @Test
  fun `test onPageScroll starts fetch account job only once`() {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(1)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 1,
          pages = OnboardingPages.initialPages,
          startedJobs = listOf("tmdb"),
        ),
      )
      .onPageScroll(0)
      .onPageScroll(1)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 1,
          pages = OnboardingPages.initialPages,
          startedJobs = listOf("tmdb"),
        ),
      )
  }

  @Test
  fun `test onPageScroll to jellyseerr page starts fetch jellyseerr account`() {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = OnboardingPages.initialPages,
          startedJobs = listOf("jellyseerr"),
        ),
      )
  }

  @Test
  fun `test onPageScroll to jellyseerr page starts fetch jellyseerr account only once`() {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = OnboardingPages.initialPages,
          startedJobs = listOf("jellyseerr"),
        ),
      )
      .onPageScroll(0)
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = OnboardingPages.initialPages,
          startedJobs = listOf("jellyseerr"),
        ),
      )
  }

  @Test
  fun `test onFetchAccount with success updates page action`() {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
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
            tmdbPage.copy(action = OnboardingAction.NavigateToTMDBLogin(isComplete = true)),
            jellyseerrPage,
            linkHandlingPage,
          ),
          startedJobs = listOf("tmdb"),
        ),
      )
  }

  @Test
  fun `test onFetchJellyseerrAccountDetails with success updates page action`() {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
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
            tmdbPage,
            jellyseerrPage.copy(
              action = OnboardingAction.NavigateToJellyseerrLogin(isComplete = true),
            ),
            linkHandlingPage,
          ),
          startedJobs = listOf("jellyseerr"),
        ),
      )
  }

  @Test
  fun `test on onboardingComplete navigates up`() = runTest {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
      .mockIsInitialOnboarding(true)
      .buildViewModel()
      .onboardingComplete()
      .assertNavigateUp()
  }

  @Test
  fun `test on not initial onboarding does not update started jobs`() {
    robot
      .mockOnboardingPages(OnboardingPages.initialPages)
      .mockIsInitialOnboarding(false)
      .mockGetJellyseerrAccountDetails(Result.success(JellyseerrAccountDetailsFactory.jellyseerr()))
      .buildViewModel()
      .onPageScroll(2)
      .assertUiState(
        OnboardingUiState.initial().copy(
          selectedPageIndex = 2,
          pages = OnboardingPages.initialPages,
        ),
      )
  }
}
