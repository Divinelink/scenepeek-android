package com.divinelink.feature.details.media.ui.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.divinelink.core.model.UIText
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setScaffoldContent
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.components.expandablefab.FloatingActionButtonItem
import com.divinelink.core.ui.core_ui_eye
import com.divinelink.core.ui.core_ui_ic_jellyseerr
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test

class ExpandableFloatingActionButtonTest : ComposeTest() {

  @Test
  fun `test buttons are shown when fab button is clicked`() = uiTest {
    var extraActionIsClicked = false

    val actionButtons = listOf(
      FloatingActionButtonItem(
        icon = IconWrapper.Vector(Icons.Rounded.Add),
        label = UIText.StringText("Add"),
        contentDescription = UIText.StringText("Add Button"),
        onClick = { extraActionIsClicked = true },
      ),
      FloatingActionButtonItem(
        icon = IconWrapper.Image(UiDrawable.core_ui_ic_jellyseerr),
        label = UIText.StringText("Delete"),
        contentDescription = UIText.StringText("Delete Button"),
        onClick = { extraActionIsClicked = true },
      ),
      FloatingActionButtonItem(
        icon = IconWrapper.Icon(UiDrawable.core_ui_eye),
        label = UIText.StringText("Close"),
        contentDescription = UIText.StringText("Close Button"),
        onClick = { extraActionIsClicked = true },
      ),
    )

    setScaffoldContent {
      ExpandableFloatActionButton(
        buttons = actionButtons,
      )
    }

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).assertIsDisplayed()
    onNodeWithContentDescription("Add Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Close Button").assertIsNotDisplayed()

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).performClick()

    onNodeWithContentDescription("Add Button").assertIsDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsDisplayed()
    onNodeWithContentDescription("Close Button").assertIsDisplayed()

    assertThat(extraActionIsClicked).isFalse()

    onNodeWithContentDescription("Add Button").performClick()

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).assertIsDisplayed()

    onNodeWithContentDescription("Add Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Close Button").assertIsNotDisplayed()
    assertThat(extraActionIsClicked).isTrue()
  }

  @Test
  fun `test icons are displayed`() = uiTest {
    val actionButtons = listOf(
      FloatingActionButtonItem(
        icon = IconWrapper.Vector(Icons.Rounded.Add),
        label = UIText.StringText("Add"),
        contentDescription = UIText.StringText("Add Button"),
        onClick = { },
      ),
      FloatingActionButtonItem(
        icon = IconWrapper.Image(UiDrawable.core_ui_ic_jellyseerr),
        label = UIText.StringText("Delete"),
        contentDescription = UIText.StringText("Delete Button"),
        onClick = { },
      ),
      FloatingActionButtonItem(
        icon = IconWrapper.Icon(UiDrawable.core_ui_eye),
        label = UIText.StringText("Close"),
        contentDescription = UIText.StringText("Close Button"),
        onClick = { },
      ),
    )

    setScaffoldContent {
      ExpandableFloatActionButton(
        buttons = actionButtons,
      )
    }

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).assertIsDisplayed()

    onNodeWithContentDescription("Add Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Close Button").assertIsNotDisplayed()

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).performClick()

    onNodeWithContentDescription("Add Button").assertIsDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsDisplayed()
    onNodeWithContentDescription("Close Button").assertIsDisplayed()
  }

  @Test
  fun `test clicking on background dismisses actions`() = uiTest {
    val actionButtons = listOf(
      FloatingActionButtonItem(
        icon = IconWrapper.Vector(Icons.Rounded.Add),
        label = UIText.StringText("Add"),
        contentDescription = UIText.StringText("Add Button"),
        onClick = { },
      ),
      FloatingActionButtonItem(
        icon = IconWrapper.Image(UiDrawable.core_ui_ic_jellyseerr),
        label = UIText.StringText("Delete"),
        contentDescription = UIText.StringText("Delete Button"),
        onClick = { },
      ),
      FloatingActionButtonItem(
        icon = IconWrapper.Icon(UiDrawable.core_ui_eye),
        label = UIText.StringText("Close"),
        contentDescription = UIText.StringText("Close Button"),
        onClick = { },
      ),
    )

    setScaffoldContent {
      ExpandableFloatActionButton(
        buttons = actionButtons,
      )
    }

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).assertIsDisplayed()
    onNodeWithContentDescription("Add Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Close Button").assertIsNotDisplayed()

    onNodeWithTag(TestTags.Components.ExpandableFab.BUTTON).performClick()

    onNodeWithContentDescription("Add Button").assertIsDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsDisplayed()
    onNodeWithContentDescription("Close Button").assertIsDisplayed()

    onNodeWithTag(TestTags.Components.ExpandableFab.BACKGROUND).performClick()
    onNodeWithContentDescription("Add Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Delete Button").assertIsNotDisplayed()
    onNodeWithContentDescription("Close Button").assertIsNotDisplayed()
  }
}
