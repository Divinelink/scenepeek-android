package com.divinelink.feature.settings.app.about

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import com.divinelink.core.commons.provider.BuildConfigProvider
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.app.SettingsScreen
import com.divinelink.feature.settings.feature_settings_about
import com.divinelink.feature.settings.feature_settings_about__developer_github_url
import com.divinelink.feature.settings.feature_settings_about__privacy_policy
import com.divinelink.feature.settings.feature_settings_about__repository_url
import com.divinelink.feature.settings.feature_settings_about__source_code
import com.divinelink.feature.settings.feature_settings_about__version
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import com.divinelink.feature.settings.Res as R

class AboutSettingsScreenTest : ComposeTest() {

  private val releaseBuildConfigProvider = object : BuildConfigProvider {
    override val isDebug: Boolean = false
    override val buildType: String = "release"
    override val versionCode: Int = 25
    override val versionName: String = "0.17.0"
    override val versionData: String = "0.17.0 25"
  }

  private val debugBuildConfigProvider = object : BuildConfigProvider {
    override val isDebug: Boolean = true
    override val buildType: String = "debug"
    override val versionCode: Int = 25
    override val versionName: String = "0.17.0 debug"
    override val versionData: String = "0.17.0 debug 25"
  }

  @Test
  fun `test version with debug build`() = uiTest {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        buildConfigProvider = debugBuildConfigProvider,
        onNavigate = {},
        animatedVisibilityScope = this,
      )
    }

    val version = "0.17.0 debug"

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

  @Test
  fun `test version with release`() = uiTest {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = {},
        buildConfigProvider = releaseBuildConfigProvider,
        animatedVisibilityScope = this,
      )
    }

    val version = "0.17.0"

    onNodeWithText(
      getString(
        R.string.feature_settings_about__version,
        version,
      ),
    ).assertIsDisplayed()
  }

  @Test
  fun `test about card is visible`() = uiTest {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = {},
        buildConfigProvider = releaseBuildConfigProvider,
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Settings.About.CARD).assertIsDisplayed()
  }

  @Test
  fun `test privacy policy is visible`() = uiTest {
    setVisibilityScopeContent {
      AboutSettingsScreen(
        onNavigate = {},
        buildConfigProvider = releaseBuildConfigProvider,
        animatedVisibilityScope = this,
      )
    }

    onNodeWithTag(TestTags.Settings.About.SCROLLABLE_CONTENT).performScrollToIndex(1)
    onNodeWithText(getString(R.string.feature_settings_about__privacy_policy)).assertIsDisplayed()
  }

  @Test
  fun `test navigateUp`() = uiTest {
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

    onNodeWithTag(TestTags.Settings.SCREEN_CONTENT).performScrollToIndex(5)

    onNodeWithText(getString(R.string.feature_settings_about)).performClick()

    onNodeWithTag(TestTags.Settings.NAVIGATION_ICON).performClick().assertIsDisplayed()

    navigatedUp shouldBe true
    navigatedToAbout shouldBe true
  }

  @Test
  fun `test click on view source code`() = uiTest {
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

    onNodeWithTag(TestTags.Settings.About.SCROLLABLE_CONTENT).performScrollToNode(
      hasText(getString(R.string.feature_settings_about__privacy_policy)),
    )

    onNodeWithText(getString(R.string.feature_settings_about__source_code)).performClick()

    val url = getString(R.string.feature_settings_about__repository_url)
    navigationRoute shouldBe Navigation.WebViewRoute(
      url = url,
      title = "Github",
    )
  }

  @Test
  fun `test click on developed by field`() = uiTest {
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

    val url = getString(R.string.feature_settings_about__developer_github_url, "Divinelink")

    onNodeWithTag(TestTags.Settings.About.SCROLLABLE_CONTENT).performScrollToNode(
      hasText(getString(R.string.feature_settings_about__privacy_policy)),
    )

    onNodeWithTag(TestTags.Settings.About.SCROLLABLE_CONTENT).performTouchInput {
      val center = centerY

      swipeDown(
        startY = center,
        endY = center + 100,
      )
    }

    onNodeWithText("Divinelink").assertIsDisplayed().performClick()

    navigationRoute shouldBe Navigation.WebViewRoute(
      url = url,
      title = "Github",
    )
  }
}
