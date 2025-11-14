package com.divinelink.feature.settings.app.about

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.SettingsScreen
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class AboutSettingsScreenTest : ComposeTest() {

  private val releaseBuildConfigProvider = object : BuildConfigProvider {
    override val isDebug: Boolean = false
    override val buildType: String = "release"
    override val versionCode: String = "0.17.0"
    override val versionData: String = "0.17.0 25"
  }

  private val debugBuildConfigProvider = object : BuildConfigProvider {
    override val isDebug: Boolean = true
    override val buildType: String = "debug"
    override val versionCode: String = "0.17.0 debug"
    override val versionData: String = "0.17.0 debug 25"
  }

  @Test
  fun `test version with debug build`() {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        buildConfigProvider = debugBuildConfigProvider,
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    val version = "0.17.0 debug"

    with(composeTestRule) {
      onNodeWithText(getString(R.string.feature_settings_about))
        .assertIsDisplayed()
        .assertTextEquals("About")

      onNodeWithText(
        getString(
          R.string.feature_settings_about__version,
          version,
        ),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test version with release`() {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = {},
        buildConfigProvider = releaseBuildConfigProvider,
        animatedVisibilityScope = this,
      )
    }

    val version = "0.17.0"

    with(composeTestRule) {
      onNodeWithText(
        getString(
          R.string.feature_settings_about__version,
          version,
        ),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test about card is visible`() {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = {},
        buildConfigProvider = releaseBuildConfigProvider,
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.About.CARD).assertIsDisplayed()
    }
  }

  @Test
  fun `test privacy policy is visible`() {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = {},
        buildConfigProvider = releaseBuildConfigProvider,
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.About.SCROLLABLE_CONTENT).performScrollToIndex(1)
      onNodeWithText(getString(R.string.feature_settings_about__privacy_policy)).assertIsDisplayed()
    }
  }

  @Test
  fun `test navigateUp`() {
    var navigatedUp = false
    var navigatedToAbout = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.AboutSettingsRoute) {
            navigatedToAbout = true
          }
          if (route is Navigation.Back) {
            navigatedUp = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.SCREEN_CONTENT).performScrollToIndex(5)

      onNodeWithText(getString(R.string.feature_settings_about)).performClick()

      onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick().assertIsDisplayed()
    }

    assertThat(navigatedUp).isTrue()
    assertThat(navigatedToAbout).isTrue()
  }

  @Test
  fun `test click on view source code`() {
    var navigationRoute: Navigation? = null
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = { route ->
          navigationRoute = route
        },
        animatedVisibilityScope = this,
        buildConfigProvider = releaseBuildConfigProvider,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.About.SCROLLABLE_CONTENT).performScrollToNode(
        hasText(getString(R.string.feature_settings_about__privacy_policy)),
      )

      onNodeWithText(getString(R.string.feature_settings_about__source_code)).performClick()

      val url = getString(R.string.feature_settings_about__repository_url)
      assertThat(navigationRoute).isEqualTo(
        Navigation.WebViewRoute(
          url = url,
          title = "Github",
        ),
      )
    }
  }

  @Test
  fun `test click on developed by field`() {
    var navigationRoute: Navigation? = null
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = { route ->
          navigationRoute = route
        },
        animatedVisibilityScope = this,
        buildConfigProvider = releaseBuildConfigProvider,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.About.SCROLLABLE_CONTENT).performScrollToNode(
        hasText(getString(R.string.feature_settings_about__privacy_policy)),
      )

      onNodeWithContentDescription("GitHub Account").performClick()

      val url = getString(R.string.feature_settings_about__developer_github_url, "Divinelink")
      assertThat(navigationRoute).isEqualTo(
        Navigation.WebViewRoute(
          url = url,
          title = "Github",
        ),
      )
    }
  }
}
