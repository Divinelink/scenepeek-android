package com.divinelink.feature.onboarding.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.TestMarkOnboardingCompleteUseCase
import com.divinelink.feature.onboarding.manager.IntroSections
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class IntroModalBottomSheetTest : ComposeTest() {

  private lateinit var viewModel: IntroViewModel

  private val markOnboardingCompleteUseCase = TestMarkOnboardingCompleteUseCase()
  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val getJellyseerrAccountDetailsUseCase = FakeGetJellyseerrDetailsUseCase()

  @Test
  fun `test onboarding screen with initial pages`() {
    viewModel = IntroViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        sections = IntroSections.onboardingSections,
      ),
    )

    setContentWithTheme {
      IntroModalBottomSheet(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
      onNodeWithText("TMDB").assertIsDisplayed()
      onNodeWithText("Jellyseerr").assertIsDisplayed()
    }
  }

  @Test
  fun `test onboarding swipe down closes modal`() {
    viewModel = IntroViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        sections = IntroSections.onboardingSections,
      ),
    )

    setContentWithTheme {
      IntroModalBottomSheet(
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    with(composeTestRule) {
    }
  }

  @Test
  fun `test perform connect tmdb action`() {
    viewModel = IntroViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        sections = listOf(IntroSections.tmdb),
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
      onNodeWithText("TMDB").performClick()
    }

    assertThat(tmdbLoginClicked).isTrue()
  }

  @Test
  fun `test perform connect jellyseerr action`() {
    viewModel = IntroViewModel(
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        sections = listOf(IntroSections.jellyseerr),
      ),
    )

    var jellyseerrRoute: Navigation.JellyseerrSettingsRoute? = null

    setContentWithTheme {
      IntroModalBottomSheet(
        onNavigate = {
          if (it is Navigation.JellyseerrSettingsRoute) {
            jellyseerrRoute = it
          }
        },
        viewModel = viewModel,
      )
    }
    with(composeTestRule) {
      onNodeWithText("Jellyseerr").performClick()
    }

    assertThat(jellyseerrRoute).isEqualTo(
      Navigation.JellyseerrSettingsRoute(withNavigationBar = false),
    )
  }
}
