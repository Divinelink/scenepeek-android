package com.divinelink.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.testing.ComposeTest
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
        spoilersObfuscated = false,
        onObfuscateClick = {},
      )
    }

    composeTestRule.onNodeWithTag(
      TestTags.Menu.MENU_ITEM.format(getString(UiString.core_ui_share)),
    ).assertIsDisplayed()
  }

  @Test
  fun `test show details drop down menu with share and spoiler option with obfuscated`() {
    composeTestRule.setContent {
      DetailsDropdownMenu(
        mediaDetails = MediaDetailsFactory.FightClub(),
        expanded = true,
        options = listOf(DetailsMenuOptions.SHARE, DetailsMenuOptions.OBFUSCATE_SPOILERS),
        onDismissDropdown = {},
        spoilersObfuscated = true,
        onObfuscateClick = {},
      )
    }

    composeTestRule.onNodeWithTag(
      TestTags.Menu.MENU_ITEM.format(getString(UiString.core_ui_share)),
    ).assertIsDisplayed()

    composeTestRule.onNodeWithTag(
      TestTags.Menu.MENU_ITEM.format(getString(UiString.core_ui_show_total_episodes_item)),
    ).assertIsDisplayed()
  }

  @Test
  fun `test show details drop down menu with share and spoiler option without obfuscated`() {
    composeTestRule.setContent {
      DetailsDropdownMenu(
        mediaDetails = MediaDetailsFactory.FightClub(),
        expanded = true,
        options = listOf(DetailsMenuOptions.SHARE, DetailsMenuOptions.OBFUSCATE_SPOILERS),
        onDismissDropdown = {},
        spoilersObfuscated = false,
        onObfuscateClick = {},
      )
    }

    composeTestRule.onNodeWithTag(
      TestTags.Menu.MENU_ITEM.format(getString(UiString.core_ui_share)),
    ).assertIsDisplayed()

    composeTestRule.onNodeWithTag(
      TestTags.Menu.MENU_ITEM.format(getString(UiString.core_ui_hide_total_episodes_item)),
    ).assertIsDisplayed()
  }
}
