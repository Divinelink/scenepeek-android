package com.divinelink.feature.details.person.ui

import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.ui.UIText
import com.divinelink.feature.details.R

fun PersonDetails.toUiSections() = listOf(
  // Known for
  PersonalInfoSectionData(
    title = UIText.ResourceText(R.string.feature_details_known_for_section),
    value = UIText.StringText(knownForDepartment ?: "-"),
  ),

  // Gender
  PersonalInfoSectionData(
    title = UIText.ResourceText(R.string.feature_details_gender_section),
    value = UIText.ResourceText(this.person.gender.stringRes),
  ),

  // Birthday
  PersonalInfoSectionData(
    title = UIText.ResourceText(R.string.feature_details_birthday_section),
    value = if (isAlive) {
      UIText.ResourceText(
        R.string.feature_details_person_birthday,
        birthday!!,
        currentAge!!,
      )
    } else {
      UIText.StringText(birthday ?: "-")
    },
  ),

  deathday?.let { deathday ->
    PersonalInfoSectionData(
      title = UIText.ResourceText(R.string.feature_details_day_of_death_section),
      value = if (ageAtDeath != null) {
        UIText.ResourceText(
          R.string.feature_details_person_birthday,
          deathday,
          ageAtDeath ?: "-",
        )
      } else {
        UIText.StringText(deathday)
      },
    )
  },

  // Place of birth
  PersonalInfoSectionData(
    title = UIText.ResourceText(R.string.feature_details_place_of_birth_section),
    value = UIText.StringText(placeOfBirth ?: "-"),
  ),
)
