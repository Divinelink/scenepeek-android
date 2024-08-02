package com.divinelink.feature.details.person.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.factories.details.person.PersonDetailsFactory
import com.divinelink.core.testing.getString
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.feature.details.R
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.core.ui.R as uiR

class PersonalDetailsTest : ComposeTest() {

  private lateinit var personalDetails: PersonDetails

  @BeforeTest
  fun setup() {
    personalDetails = PersonDetailsFactory.steveCarell()
  }

  @Test
  fun `test error placeholder on female`() {
    personalDetails = personalDetails.copy(
      person = personalDetails.person.copy(gender = Gender.FEMALE),
    )

    setContentWithTheme {
      PersonalDetails(personalDetails)
    }
    with(composeTestRule) {
      onNodeWithContentDescription(
        getString(uiR.string.core_ui_movie_image_placeholder),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test error placeholder on male`() {
    setContentWithTheme {
      PersonalDetails(personalDetails)
    }
    with(composeTestRule) {
      onNodeWithContentDescription(
        getString(uiR.string.core_ui_movie_image_placeholder),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test personal info is displayed`() {
    setContentWithTheme {
      PersonalDetails(personalDetails)
    }
    with(composeTestRule) {
      onNodeWithText(
        getString(R.string.feature_details_personal_info_section),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test blank biography`() {
    personalDetails = personalDetails.copy(biography = "")

    setContentWithTheme {
      PersonalDetails(personalDetails)
    }
    with(composeTestRule) {
      onNodeWithText(
        getString(R.string.feature_details_person_blank_biography, personalDetails.person.name),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test null biography`() {
    personalDetails = personalDetails.copy(biography = null)

    setContentWithTheme {
      PersonalDetails(personalDetails)
    }
    with(composeTestRule) {
      onNodeWithText(
        getString(R.string.feature_details_person_blank_biography, personalDetails.person.name),
      ).assertIsDisplayed()
    }
  }

  @Test
  fun `test biography with long content is expandable`() {
    setContentWithTheme {
      PersonalDetails(personalDetails)
    }

    with(composeTestRule) {
      onNodeWithText(
        getString(R.string.feature_details_biography_section),
      ).assertIsDisplayed()

      onNodeWithText(getString(uiR.string.core_ui_show_less)).assertIsNotDisplayed()

      onNodeWithText(getString(uiR.string.core_ui_read_more))
        .assertIsDisplayed()
        .performClick()
        .assertIsNotDisplayed()

      onNodeWithText(getString(uiR.string.core_ui_show_less))
        .assertIsDisplayed()
        .performClick()

      onNodeWithText(getString(uiR.string.core_ui_show_less)).assertIsNotDisplayed()

      onNodeWithText(getString(uiR.string.core_ui_read_more))
        .assertIsDisplayed()
    }
  }

  @Test
  fun `test biography with short content is not expandable`() {
    personalDetails = personalDetails.copy(
      biography = "Short biography",
    )

    setContentWithTheme {
      PersonalDetails(personalDetails)
    }

    with(composeTestRule) {
      onNodeWithText(
        getString(R.string.feature_details_biography_section),
      ).assertIsDisplayed()

      onNodeWithText(getString(uiR.string.core_ui_show_less)).assertIsNotDisplayed()
      onNodeWithText(getString(uiR.string.core_ui_read_more)).assertIsNotDisplayed()
    }
  }
}
