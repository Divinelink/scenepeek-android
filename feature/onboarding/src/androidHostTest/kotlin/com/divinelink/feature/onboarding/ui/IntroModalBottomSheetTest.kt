package com.divinelink.feature.onboarding.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.model.UIText
import com.divinelink.core.model.onboarding.IntroSection
import com.divinelink.core.model.onboarding.IntroType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.testing.usecase.FakeGetAccountDetailsUseCase
import com.divinelink.core.testing.usecase.FakeGetJellyseerrDetailsUseCase
import com.divinelink.core.testing.usecase.TestMarkOnboardingCompleteUseCase
import com.divinelink.feature.onboarding.manager.IntroSections
import com.divinelink.feature.onboarding.resources.feature_onboarding_changelog
import com.divinelink.feature.onboarding.resources.v56_add_company_discover_filter
import com.divinelink.feature.onboarding.resources.v56_add_sorting_options_on_lists
import com.divinelink.feature.onboarding.resources.v56_make_companies_clickable
import com.divinelink.feature.onboarding.resources.v58_allow_home_customization
import com.divinelink.feature.onboarding.resources.v58_allow_plex_authorization
import com.divinelink.feature.onboarding.resources.v58_seerr_recent_requests
import com.divinelink.feature.onboarding.resources.v58_wikipedia_link
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import com.divinelink.feature.onboarding.resources.Res as R

class IntroModalBottomSheetTest : ComposeTest() {

  private lateinit var viewModel: IntroViewModel

  private val markOnboardingCompleteUseCase = TestMarkOnboardingCompleteUseCase()
  private val getAccountDetailsUseCase = FakeGetAccountDetailsUseCase()
  private val getJellyseerrAccountDetailsUseCase = FakeGetJellyseerrDetailsUseCase()

  @Test
  fun `test onboarding screen with initial pages`() = uiTest {
    viewModel = IntroViewModel(
      type = IntroType.Onboarding,
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        sections = IntroSections.onboardingSections,
      ),
    )

    setContentWithTheme {
      IntroModalBottomSheet(
        type = IntroType.Onboarding,
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    onNodeWithText("TMDB").assertIsDisplayed()
    onNodeWithText("Seerr").assertIsDisplayed()
  }

  @Test
  fun `test perform connect tmdb action`() = uiTest {
    viewModel = IntroViewModel(
      type = IntroType.Onboarding,
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
        type = IntroType.Onboarding,
        onNavigate = {
          if (it is Navigation.TMDBAuthRoute) {
            tmdbLoginClicked = true
          }
        },
        viewModel = viewModel,
      )
    }

    onNodeWithText("TMDB").performClick()

    tmdbLoginClicked shouldBe true
  }

  @Test
  fun `test perform connect jellyseerr action`() = uiTest {
    viewModel = IntroViewModel(
      type = IntroType.Onboarding,
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
        type = IntroType.Onboarding,
        onNavigate = {
          if (it is Navigation.JellyseerrSettingsRoute) {
            jellyseerrRoute = it
          }
        },
        viewModel = viewModel,
      )
    }
    onNodeWithText("Seerr").performClick()

    jellyseerrRoute shouldBe Navigation.JellyseerrSettingsRoute(withNavigationBar = false)
  }

  @Test
  fun `test modal with type changelog`() = uiTest {
    viewModel = IntroViewModel(
      type = IntroType.Changelog,
      markOnboardingCompleteUseCase = markOnboardingCompleteUseCase.mock,
      getAccountDetailsUseCase = getAccountDetailsUseCase.mock,
      getJellyseerrProfileUseCase = getJellyseerrAccountDetailsUseCase.mock,
      onboardingManager = TestOnboardingManager(
        sections = listOf(),
        changelog = listOf(
          IntroSection.Header(UIText.ResourceText(R.string.feature_onboarding_changelog)),
          IntroSection.WhatsNew("v0.38.0"),
          IntroSection.SecondaryHeader.Added,
          IntroSection.Text(UIText.ResourceText(R.string.v58_allow_home_customization)),
          IntroSection.Text(UIText.ResourceText(R.string.v58_wikipedia_link)),
          IntroSection.Text(UIText.ResourceText(R.string.v58_seerr_recent_requests)),
          IntroSection.Text(UIText.ResourceText(R.string.v58_allow_plex_authorization)),
          IntroSection.WhatsNew("v0.37.0"),
          IntroSection.SecondaryHeader.Added,
          IntroSection.Text(UIText.ResourceText(R.string.v56_add_sorting_options_on_lists)),
          IntroSection.Text(UIText.ResourceText(R.string.v56_add_company_discover_filter)),
          IntroSection.SecondaryHeader.Features,
          IntroSection.Text(UIText.ResourceText(R.string.v56_make_companies_clickable)),
        ),
      ),
    )

    setContentWithTheme {
      IntroModalBottomSheet(
        type = IntroType.Onboarding,
        onNavigate = {},
        viewModel = viewModel,
      )
    }

    onNodeWithText("Changelog").assertIsDisplayed()
    onNodeWithText("What's new in ScenePeek for v0.38.0").assertIsDisplayed()
    onNodeWithText("What's new in ScenePeek for v0.37.0").assertIsDisplayed()
  }
}
