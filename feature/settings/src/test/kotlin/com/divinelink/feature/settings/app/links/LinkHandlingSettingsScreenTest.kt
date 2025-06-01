package com.divinelink.feature.settings.app.links

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setVisibilityScopeContent
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import kotlin.test.Test

class LinkHandlingSettingsScreenTest : ComposeTest() {

  @Test
  fun `test LinkHandlingSettingsScreen`() {
    setVisibilityScopeContent {
      LinkHandlingSettingsScreen(
        navigateUp = {},
        animatedVisibilityScope = this,
      )
    }

    with(composeTestRule) {
      onNodeWithText("Link handling").assertIsDisplayed()
      onNodeWithText("Allow DebugPeek to handle specific URLs?").assertIsDisplayed()

      onNodeWithTag(TestTags.Settings.LinkHandling.DIRECTIONS_TEXT)
        .performScrollTo()
        .assertIsDisplayed()

      onNodeWithText(getString(R.string.feature_settings_open_settings)).assertIsDisplayed()
    }
  }
}
