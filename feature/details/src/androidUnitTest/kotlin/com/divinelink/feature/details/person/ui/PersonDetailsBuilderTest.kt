package com.divinelink.feature.details.person.ui

import com.divinelink.core.fixtures.details.person.PersonDetailsFactory
import com.divinelink.core.fixtures.details.person.PersonDetailsFactory.toWzd
import com.divinelink.core.model.UIText
import com.divinelink.feature.details.resources.feature_details_birthday_section
import com.divinelink.feature.details.resources.feature_details_day_of_death_section
import com.divinelink.feature.details.resources.feature_details_gender_section
import com.divinelink.feature.details.resources.feature_details_known_for_section
import com.divinelink.feature.details.resources.feature_details_person_birthday
import com.divinelink.feature.details.resources.feature_details_place_of_birth_section
import com.google.common.truth.Truth.assertThat
import kotlin.test.Test
import com.divinelink.feature.details.resources.Res as R

class PersonDetailsBuilderTest {

  @Test
  fun `test toUiSections with alive person`() {
    val personDetails = PersonDetailsFactory.steveCarell()

    val uiSections = personDetails.toUiSections()

    assertThat(uiSections).hasSize(4)
    assertThat(uiSections[0]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_known_for_section),
        value = UIText.StringText(personDetails.person.knownForDepartment!!),
      ),
    )
    assertThat(uiSections[1]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_gender_section),
        value = UIText.ResourceText(personDetails.person.gender.stringRes),
      ),
    )
    assertThat(uiSections[2]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_birthday_section),
        value = UIText.ResourceText(
          R.string.feature_details_person_birthday,
          personDetails.birthday!!,
          personDetails.currentAge!!,
        ),
      ),
    )
    assertThat(uiSections[3]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_place_of_birth_section),
        value = UIText.StringText(personDetails.placeOfBirth!!),
      ),
    )
  }

  @Test
  fun `test toUiSection know for section without known for department`() {
    val personDetails = PersonDetailsFactory.steveCarell().toWzd {
      withKnownForDepartment(null)
    }

    val uiSections = personDetails.toUiSections()

    assertThat(uiSections[0]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_known_for_section),
        value = UIText.StringText("-"),
      ),
    )
  }

  @Test
  fun `test toUiSection birthday section without birthday`() {
    val personDetails = PersonDetailsFactory.steveCarell().copy(
      birthday = null,
    )

    val uiSections = personDetails.toUiSections()

    assertThat(uiSections[2]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_birthday_section),
        value = UIText.StringText("-"),
      ),
    )
  }

  @Test
  fun `test toUiSection place of birth section without place of birth`() {
    val personDetails = PersonDetailsFactory.steveCarell().copy(
      placeOfBirth = null,
    )

    val uiSections = personDetails.toUiSections()

    assertThat(uiSections[3]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_place_of_birth_section),
        value = UIText.StringText("-"),
      ),
    )
  }

  @Test
  fun `test toUiSection deathday section without birthday`() {
    val personDetails = PersonDetailsFactory.steveCarell().copy(
      birthday = null,
      deathday = "2021-08-16",
    )

    val uiSections = personDetails.toUiSections()

    assertThat(uiSections).hasSize(5)
    assertThat(uiSections[3]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_day_of_death_section),
        value = UIText.StringText("2021-08-16"),
      ),
    )
  }

  @Test
  fun `test toUiSection with dead person`() {
    val personDetails = PersonDetailsFactory.steveCarell().copy(
      deathday = "2021-08-16",
    )

    val uiSections = personDetails.toUiSections()

    assertThat(uiSections).hasSize(5)
    assertThat(uiSections[0]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_known_for_section),
        value = UIText.StringText(personDetails.person.knownForDepartment!!),
      ),
    )
    assertThat(uiSections[1]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_gender_section),
        value = UIText.ResourceText(personDetails.person.gender.stringRes),
      ),
    )
    assertThat(uiSections[2]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_birthday_section),
        value = UIText.StringText("1962-08-16"),
      ),
    )
    assertThat(uiSections[3]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_day_of_death_section),
        value = UIText.ResourceText(
          R.string.feature_details_person_birthday,
          personDetails.deathday!!,
          59,
        ),
      ),
    )
    assertThat(uiSections[4]).isEqualTo(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_place_of_birth_section),
        value = UIText.StringText(personDetails.placeOfBirth!!),
      ),
    )
  }
}
