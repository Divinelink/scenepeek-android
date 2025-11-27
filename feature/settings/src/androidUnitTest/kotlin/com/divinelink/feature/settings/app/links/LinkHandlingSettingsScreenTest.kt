package com.divinelink.feature.settings.app.links

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.Res
import com.divinelink.feature.settings.feature_settings_open_settings
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class LinkHandlingSettingsScreenTest : ComposeTest() {

  @Test
  fun `test LinkHandlingSettingsScreen`() = uiTest {
    setVisibilityScopeContent {
      LinkHandlingSettingsScreen(
        navigateUp = {},
        animatedVisibilityScope = this,
      )
    }

    onNodeWithText("Link handling").assertIsDisplayed()
    onNodeWithText("Allow ScenePeek to handle specific URLs?").assertIsDisplayed()

    onNodeWithTag(TestTags.Settings.LinkHandling.DIRECTIONS_TEXT)
      .performScrollTo()
      .assertIsDisplayed()

    onNodeWithText(getString(Res.string.feature_settings_open_settings)).assertIsDisplayed()
  }
}
