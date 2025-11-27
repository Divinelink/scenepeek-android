package com.divinelink.core.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.divinelink.core.fixtures.model.details.MediaDetailsFactory
import com.divinelink.core.model.details.DetailsMenuOptions
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

class DetailsDropDownMenuTest : ComposeTest() {

  @Test
  fun `test show details drop down menu with share only option`() = uiTest {
    setContentWithTheme {
      DetailsDropdownMenu(
        mediaDetails = MediaDetailsFactory.FightClub(),
        expanded = true,
        options = listOf(DetailsMenuOptions.SHARE),
        onDismissDropdown = {},
        spoilersObfuscated = false,
        onObfuscateClick = {},
      )
    }

    onNodeWithTag(
      TestTags.Menu.item(getString(UiString.core_ui_share)),
    ).assertIsDisplayed()
  }

  @Test
  fun `test show details drop down menu with share and spoiler option with obfuscated`() = uiTest {
    setContentWithTheme {
      DetailsDropdownMenu(
        mediaDetails = MediaDetailsFactory.FightClub(),
        expanded = true,
        options = listOf(DetailsMenuOptions.SHARE, DetailsMenuOptions.OBFUSCATE_SPOILERS),
        onDismissDropdown = {},
        spoilersObfuscated = true,
        onObfuscateClick = {},
      )
    }

    onNodeWithTag(
      TestTags.Menu.item(getString(UiString.core_ui_share)),
    ).assertIsDisplayed()

    onNodeWithTag(
      TestTags.Menu.item(getString(UiString.core_ui_show_total_episodes_item)),
    ).assertIsDisplayed()
  }

  @Test
  fun `test show details drop down menu with share and spoiler option without obfuscated`() =
    uiTest {
      setContentWithTheme {
        DetailsDropdownMenu(
          mediaDetails = MediaDetailsFactory.FightClub(),
          expanded = true,

          options = listOf(DetailsMenuOptions.SHARE, DetailsMenuOptions.OBFUSCATE_SPOILERS),
          onDismissDropdown = {},
          spoilersObfuscated = false,
          onObfuscateClick = {},
        )
      }

      onNodeWithTag(
        TestTags.Menu.item(getString(UiString.core_ui_share)),
      ).assertIsDisplayed()

      onNodeWithTag(
        TestTags.Menu.item(getString(UiString.core_ui_hide_total_episodes_item)),
      ).assertIsDisplayed()
    }
}
