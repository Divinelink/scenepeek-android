package com.divinelink.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.model.details.MediaDetailsFactory
import com.divinelink.core.testing.getString
import kotlin.test.Test

class DetailsDropDownMenuTest : ComposeTest() {

  @Test
  fun `test show details drop down menu with share only option`() {
    composeTestRule.setContent {
      DetailsDropdownMenu(
        mediaDetails = MediaDetailsFactory.FightClub(),
        expanded = true,
        options = listOf(DetailsMenuOptions.SHARE),
        onDismissDropdown = {},
      )
    }

    composeTestRule.onNodeWithTag(
      TestTags.Menu.MENU_ITEM.format(getString(R.string.core_ui_share)),
    ).assertIsDisplayed()
  }
}
