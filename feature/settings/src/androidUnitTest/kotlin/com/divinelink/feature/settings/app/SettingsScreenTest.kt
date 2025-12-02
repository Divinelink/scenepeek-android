package com.divinelink.feature.settings.app

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_navigate_up_button_content_description
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.feature_settings_details_preferences
import com.divinelink.feature.settings.feature_settings_link_handling
import com.divinelink.feature.settings.preferences__appearance
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class SettingsScreenTest : ComposeTest() {

  @Test
  fun `test navigate to account screen and navigate up`() = uiTest {
    var navigatedToAccountSettings = false
    var navigatedUp = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.Back) {
            navigatedUp = true
          } else if (route is Navigation.AccountSettingsRoute) {
            navigatedToAccountSettings = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    onNodeWithText("Account").assertExists()
    onNodeWithText("Account").performClick()

    navigatedToAccountSettings shouldBe true

    val navigateUpContentDescription = getString(
      UiString.core_ui_navigate_up_button_content_description,
    )

    onNodeWithContentDescription(navigateUpContentDescription).performClick()
    navigatedUp shouldBe true
  }

  @Test
  fun `test navigate to appearance screen`() = uiTest {
    var navigatedToAppearanceSettings = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.AppearanceSettingsRoute) {
            navigatedToAppearanceSettings = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    val appearanceString = getString(Res.string.preferences__appearance)

    onNodeWithText(appearanceString).assertExists()
    onNodeWithText(appearanceString).performClick()

    navigatedToAppearanceSettings shouldBe true
  }

  @Test
  fun `test navigate to link handling screen`() = uiTest {
    var navigatedToLinkHandling = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.LinkHandlingSettingsRoute) {
            navigatedToLinkHandling = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    val linkHandlingSetting = getString(Res.string.feature_settings_link_handling)

    onNodeWithText(linkHandlingSetting).assertExists()
    onNodeWithText(linkHandlingSetting).performClick()

    navigatedToLinkHandling shouldBe true
  }

  @Test
  fun `test navigate to details preference screen`() = uiTest {
    var navigatedToDetailPreferencesSettings = false
    setVisibilityScopeContent {
      SettingsScreen(
        onNavigate = { route ->
          if (route is Navigation.DetailsPreferencesSettingsRoute) {
            navigatedToDetailPreferencesSettings = true
          }
        },
        animatedVisibilityScope = this,
      )
    }

    val detailsPreferencesString = getString(Res.string.feature_settings_details_preferences)

    onNodeWithText(detailsPreferencesString).assertExists()

    onNodeWithText(detailsPreferencesString).performClick()
    navigatedToDetailPreferencesSettings shouldBe true
  }
}
