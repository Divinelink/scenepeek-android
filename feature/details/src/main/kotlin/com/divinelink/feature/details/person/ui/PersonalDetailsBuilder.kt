package com.divinelink.feature.details.person.ui

import com.divinelink.core.commons.Constants
import com.divinelink.core.model.UIText
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.feature.details.R

fun PersonDetails.toUiSections() = buildList {
  // Known for section
  add(
    PersonalInfoSectionData(
      title = UIText.ResourceText(R.string.feature_details_known_for_section),
      value = UIText.StringText(person.knownForDepartment ?: Constants.String.EMPTY_DASH),
    ),
  )

  // Gender section
  add(
    PersonalInfoSectionData(
      title = UIText.ResourceText(R.string.feature_details_gender_section),
      value = UIText.ResourceText(person.gender.stringRes),
    ),
  )

  // Birthday section
  add(
    PersonalInfoSectionData(
      title = UIText.ResourceText(R.string.feature_details_birthday_section),
      value = if (isAlive) {
        UIText.ResourceText(
          R.string.feature_details_person_birthday,
          birthday!!,
          currentAge!!,
        )
      } else {
        UIText.StringText(birthday ?: Constants.String.EMPTY_DASH)
      },
    ),
  )

  // Deathday section
  deathday?.let { deathday ->
    add(
      PersonalInfoSectionData(
        title = UIText.ResourceText(R.string.feature_details_day_of_death_section),
        value = ageAtDeath?.let { ageAtDeath ->
          UIText.ResourceText(
            R.string.feature_details_person_birthday,
            deathday,
            ageAtDeath,
          )
        } ?: UIText.StringText(deathday),
      ),
    )
  }

  // Place of birth section
  add(
    PersonalInfoSectionData(
      title = UIText.ResourceText(R.string.feature_details_place_of_birth_section),
      value = UIText.StringText(placeOfBirth ?: Constants.String.EMPTY_DASH),
    ),
  )
}
