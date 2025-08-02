package com.divinelink.feature.onboarding.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.TestMarkOnboardingCompleteUseCase
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.onboarding.R
import com.divinelink.feature.onboarding.manager.IntroSections
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
        pages = IntroSections.onboardingSections,
      ),
    )

    setContentWithTheme {
      IntroModalBottomSheet(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithText(getString(R.string.feature_onboarding_skip)).assertIsDisplayed()

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(IntroSections.onboardingSections[0].tag))
        .assertIsDisplayed()
        .performTouchInput {
          swipeLeft()
        }

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(IntroSections.onboardingSections[1].tag))
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
        pages = IntroSections.onboardingSections,
      ),
    )

    setContentWithTheme {
      IntroModalBottomSheet(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(IntroSections.onboardingSections[0].tag))
        .assertIsDisplayed()
        .performTouchInput {
          swipeLeft()
        }

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(IntroSections.tmdb.tag))
        .assertIsDisplayed()
        .performTouchInput {
          swipeLeft()
        }

      onNodeWithTag(TestTags.Onboarding.ONBOARDING_PAGE.format(IntroSections.jellyseerr.tag))
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
        pages = listOf(IntroSections.tmdb),
      ),
    )

    var tmdbLoginClicked = false

    setContentWithTheme {
      IntroModalBottomSheet(
        onNavigate = {
          if (it is Navigation.TMDBAuthRoute) {
            tmdbLoginClicked = true
          }
        },
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Onboarding.ONBOARDING_PAGE.format(IntroSections.tmdb.tag),
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
        pages = listOf(IntroSections.jellyseerr),
      ),
    )

    var jellyseerrLoginClick = false

    setContentWithTheme {
      IntroModalBottomSheet(
        onNavigate = {
          if (it is Navigation.JellyseerrSettingsRoute) {
            jellyseerrLoginClick = true
          }
        },
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(
        TestTags.Onboarding.ONBOARDING_PAGE.format(IntroSections.jellyseerr.tag),
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
