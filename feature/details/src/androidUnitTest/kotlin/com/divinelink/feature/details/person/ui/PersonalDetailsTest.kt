package com.divinelink.feature.details.person.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.core.testing.ComposeTest
import com.divinelink.core.testing.setContentWithTheme
import com.divinelink.core.testing.uiTest
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_read_more
import com.divinelink.core.ui.core_ui_show_less
import com.divinelink.feature.details.feature_details_biography_section
import com.divinelink.feature.details.feature_details_person_blank_biography
import com.divinelink.feature.details.feature_details_personal_info_section
import org.jetbrains.compose.resources.getString
import kotlin.test.BeforeTest
import kotlin.test.Test
import com.divinelink.feature.details.Res as R

class PersonalDetailsTest : ComposeTest() {

  private lateinit var personalDetails: PersonDetails

  @BeforeTest
  fun setup() {
    personalDetails = PersonDetailsFactory.steveCarell()
  }

  @Test
  fun `test personal info is displayed`() = uiTest {
    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Visible(personalDetails))
    }
    onNodeWithText(
      getString(R.string.feature_details_personal_info_section),
    ).assertIsDisplayed()
  }

  @Test
  fun `test blank biography`() = uiTest {
    personalDetails = personalDetails.copy(biography = "")

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Visible(personalDetails))
    }
    onNodeWithText(
      getString(R.string.feature_details_person_blank_biography, personalDetails.person.name),
    ).assertIsDisplayed()
  }

  @Test
  fun `test biography when is loading`() = uiTest {
    personalDetails = personalDetails.copy(biography = null)

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Prefetch(personalDetails.person))
    }
    onNodeWithTag(TestTags.Person.SHIMMERING_BIOGRAPHY_CONTENT).assertIsDisplayed()
    onNodeWithText(
      getString(R.string.feature_details_person_blank_biography, personalDetails.person.name),
    ).assertIsNotDisplayed()
  }

  @Test
  fun `test null biography`() = uiTest {
    personalDetails = personalDetails.copy(biography = null)

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Visible(personalDetails))
    }
    onNodeWithText(
      getString(R.string.feature_details_person_blank_biography, personalDetails.person.name),
    ).assertIsDisplayed()
  }

  @Test
  fun `test biography with long content is expandable`() = uiTest {
    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Visible(personalDetails))
    }

    onNodeWithText(
      getString(R.string.feature_details_biography_section),
    ).assertIsDisplayed()

    onNodeWithText(getString(UiString.core_ui_show_less)).assertIsNotDisplayed()

    onNodeWithText(getString(UiString.core_ui_read_more))
      .assertIsDisplayed()
      .performClick()
      .assertIsNotDisplayed()

    onNodeWithText(getString(UiString.core_ui_show_less))
      .assertIsDisplayed()
      .performClick()

    onNodeWithText(getString(UiString.core_ui_show_less)).assertIsNotDisplayed()

    onNodeWithText(getString(UiString.core_ui_read_more))
      .assertIsDisplayed()
  }

  @Test
  fun `test biography with short content is not expandable`() = uiTest {
    personalDetails = personalDetails.copy(
      biography = "Short biography",
    )

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Visible(personalDetails))
    }

    onNodeWithText(
      getString(R.string.feature_details_biography_section),
    ).assertIsDisplayed()

    onNodeWithText(getString(UiString.core_ui_show_less)).assertIsNotDisplayed()
    onNodeWithText(getString(UiString.core_ui_read_more)).assertIsNotDisplayed()
  }

  @Test
  fun `test knownForDepartment when is loading`() = uiTest {
    val person = personalDetails.person.copy(knownForDepartment = null)
    personalDetails = PersonDetailsFactory.steveCarell().copy(person = person)

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Prefetch(personalDetails.person))
    }

    onNodeWithTag(TestTags.Shimmer.HALF_LINE.format("Known For")).assertIsDisplayed()
  }

  @Test
  fun `test gender when is loading`() = uiTest {
    val person = personalDetails.person.copy(gender = Gender.NOT_SET)
    personalDetails = PersonDetailsFactory.steveCarell().copy(person = person)

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Prefetch(personalDetails.person))
    }

    onNodeWithTag(TestTags.Shimmer.HALF_LINE.format("Gender")).assertIsDisplayed()
  }

  @Test
  fun `test place of birth when is loading`() = uiTest {
    personalDetails = PersonDetailsFactory.steveCarell().copy(placeOfBirth = null)

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Prefetch(personalDetails.person))
    }

    onNodeWithTag(TestTags.Shimmer.HALF_LINE.format("Place of Birth")).assertIsDisplayed()
  }

  @Test
  fun `test birthday when is loading`() = uiTest {
    personalDetails = PersonDetailsFactory.steveCarell().copy(birthday = null)

    setContentWithTheme {
      PersonalDetails(PersonDetailsUiState.Data.Prefetch(personalDetails.person))
    }

    onNodeWithTag(TestTags.Shimmer.HALF_LINE.format("Birthday")).assertIsDisplayed()
  }
}
