package com.divinelink.feature.settings.app.links

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.settings.R
import kotlin.test.Test

class LinkHandlingSettingsScreenTest : ComposeTest() {

  @Test
  fun `test LinkHandlingSettingsScreen`() {
    setContentWithTheme {
      LinkHandlingSettingsScreen(
        navigateUp = {},
      )
    }

    with(composeTestRule) {
      onNodeWithText("Link handling").assertIsDisplayed()
      onNodeWithText("Allow DebugPeek to handle specific URLs?").assertIsDisplayed()

      onNodeWithTag(TestTags.Settings.LinkHandling.DIRECTIONS_TEXT).performScrollTo()
        .assertIsDisplayed()
      onNodeWithTag(TestTags.LAZY_COLUMN).performScrollToIndex(2)
      onNodeWithText(getString(R.string.feature_settings_open_settings)).assertIsDisplayed()
    }
  }
}
