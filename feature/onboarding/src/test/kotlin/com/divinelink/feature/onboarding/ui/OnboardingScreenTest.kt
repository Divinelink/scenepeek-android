package com.divinelink.feature.onboarding.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.manager.TestOnboardingManager
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.TestMarkOnboardingCompleteUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.onboarding.R
import com.divinelink.feature.onboarding.manager.OnboardingPages
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import com.divinelink.core.model.R as modelR

class OnboardingScreenTest : ComposeTest() {

  private lateinit var viewModel: OnboardingViewModel

  private val markOnboardingCompleteUseCase = TestMarkOnboardingCompleteUseCase()
  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val getJellyseerrAccountDetailsUseCase = FakeGetJellyseerrDetailsUseCase()

  @Test
  fun `test onboarding screen with initial pages`() {
    viewModel = OnboardingViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrAccountDetailsUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        onboardingPages = OnboardingPages.initialPages,
      ),
    )

    setContentWithTheme {
      OnboardingScreen(
        onNavigateUp = {},
        onNavigateToJellyseerrSettings = {},
        onNavigateToTMDBLogin = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.feature_onboarding_skip)).assertIsDisplayed()

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(OnboardingPages.initialPages[0].tag))
        .assertIsDisplayed()
        .performTouchInput {
          swipeLeft()
        }

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(OnboardingPages.initialPages[1].tag))
        .assertIsDisplayed()
    }
  }

  @Test
  fun `test onboarding pages swipe gesture`() {
    viewModel = OnboardingViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrAccountDetailsUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        onboardingPages = OnboardingPages.initialPages,
      ),
    )

    setContentWithTheme {
      OnboardingScreen(
        onNavigateUp = {},
        onNavigateToJellyseerrSettings = {},
        onNavigateToTMDBLogin = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(OnboardingPages.initialPages[0].tag))
        .assertIsDisplayed()
        .performTouchInput {
          swipeLeft()
        }

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(OnboardingPages.tmdbPage.tag))
        .assertIsDisplayed()
        .performTouchInput {
          swipeLeft()
        }

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(OnboardingPages.jellyseerrPage.tag))
        .assertIsDisplayed()
    }
  }

  @Test
  fun `test perform connect tmdb action`() {
    viewModel = OnboardingViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrAccountDetailsUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        onboardingPages = listOf(OnboardingPages.tmdbPage),
      ),
    )

    var tmdbLoginClicked = false

    setContentWithTheme {
      OnboardingScreen(
        onNavigateUp = {},
        onNavigateToJellyseerrSettings = {},
        onNavigateToTMDBLogin = {
          tmdbLoginClicked = true
        },
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Onboarding.ONBOARDING_PAGE.format(OnboardingPages.tmdbPage.tag),
      ).performScrollToNode(
        hasText(getString(modelR.string.core_model_onboarding_tmdb_page_action)),
      )

      onNodeWithText(
        getString(modelR.string.core_model_onboarding_tmdb_page_action),
      ).performClick()
    }

    assertThat(tmdbLoginClicked).isTrue()
  }

  @Test
  fun `test perform connect jellyseerr action`() {
    viewModel = OnboardingViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrAccountDetailsUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        onboardingPages = listOf(OnboardingPages.jellyseerrPage),
      ),
    )

    var jellyseerrLoginClick = false

    setContentWithTheme {
      OnboardingScreen(
        onNavigateUp = {},
        onNavigateToJellyseerrSettings = {
          jellyseerrLoginClick = true
        },
        onNavigateToTMDBLogin = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Onboarding.ONBOARDING_PAGE.format(OnboardingPages.jellyseerrPage.tag),
      ).performScrollToNode(
        hasText(getString(modelR.string.core_model_onboarding_jellyseerr_page_action)),
      )

      onNodeWithText(
        getString(modelR.string.core_model_onboarding_jellyseerr_page_action),
      ).performClick()
    }

    assertThat(jellyseerrLoginClick).isTrue()
  }
}
