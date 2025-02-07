package com.divinelink.feature.settings.app.about

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.divinelink.core.commons.BuildConfigProvider
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import com.divinelink.feature.settings.app.SettingsScreen
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import com.divinelink.core.commons.R as CommonR

class AboutSettingsScreenTest : ComposeTest() {

  private val releaseBuildConfigProvider = object : BuildConfigProvider {
    override val isDebug: Boolean = false
    override val buildType: String = "release"
  }

  private val debugBuildConfigProvider = object : BuildConfigProvider {
    override val isDebug: Boolean = true
    override val buildType: String = "debug"
  }

  @Test
  fun `test version with debug build`() {
    setContentWithTheme {
      AboutSettingsScreen(
        buildConfigProvider = debugBuildConfigProvider,
        onNavigateUp = {},
      )
    }

    val version = getString(CommonR.string.version_name) + " debug"

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
    setContentWithTheme {
      AboutSettingsScreen(
        onNavigateUp = {},
        buildConfigProvider = releaseBuildConfigProvider,
      )
    }

    val version = getString(CommonR.string.version_name)

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
    setContentWithTheme {
      AboutSettingsScreen(
        onNavigateUp = {},
        buildConfigProvider = releaseBuildConfigProvider,
      )
    }

    with(composeTestRule) {
      onNodeWithTag(TestTags.Settings.About.CARD).assertIsDisplayed()
    }
  }

  @Test
  fun `test privacy policy is visible`() {
    setContentWithTheme {
      AboutSettingsScreen(
        onNavigateUp = {},
        buildConfigProvider = releaseBuildConfigProvider,
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
    setContentWithTheme {
      SettingsScreen(
        onNavigateUp = { navigatedUp = true },
        onNavigateToAccountSettings = {},
        onNavigateToAppearanceSettings = {},
        onNavigateToDetailPreferencesSettings = {},
        onNavigateToLinkHandling = {},
        onNavigateToAboutSettings = { navigatedToAbout = true },
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
}
