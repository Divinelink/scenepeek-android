package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.person.PersonDetails
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.expanding.ExpandingComponents
import com.divinelink.core.ui.components.expanding.ExpandingText
import com.divinelink.core.ui.getString
import com.divinelink.feature.details.R
import com.divinelink.core.ui.R as uiR

fun PersonDetails.toUiSections() = listOf(
  // Known for
  PersonalInfoSectionData(
    title = UIText.StringText("Known for"),
    value = UIText.StringText(knownForDepartment ?: "-"),
  ),

  // Gender
  PersonalInfoSectionData(
    title = UIText.StringText("Gender"),
    value = UIText.ResourceText(this.person.gender.stringRes),
  ),

  // Birthday
  PersonalInfoSectionData(
    title = UIText.StringText("Birthday"),
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
      title = UIText.StringText("Day of death"),
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
    title = UIText.StringText("Place of birth"),
    value = UIText.StringText(placeOfBirth ?: "-"),
  ),
)

@Composable
fun PersonalDetails(personalDetails: PersonDetails) {
  Row(
    modifier = Modifier
      .padding(paddingValues = ListPaddingValues)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_12),
  ) {
    MovieImage(
      modifier = Modifier.weight(1f),
      path = personalDetails.person.profilePath,
      errorPlaceHolder = if (personalDetails.person.gender == Gender.FEMALE) {
        painterResource(id = uiR.drawable.core_ui_ic_female_person_placeholder)
      } else {
        painterResource(id = uiR.drawable.core_ui_ic_person_placeholder)
      },
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(3f),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      Text(
        text = "Personal info",
        style = MaterialTheme.typography.titleMedium,
      )

      personalDetails.toUiSections().forEach { section ->
        if (section != null) {
          PersonalInfoSection(section)
        }
      }
    }
  }

  val biography = if (personalDetails.biography.isNullOrBlank()) {
    stringResource(
      id = R.string.feature_details_person_blank_biography,
      personalDetails.person.name,
    )
  } else {
    personalDetails.biography!!
  }

  Column {
    Text(
      modifier = Modifier.padding(MaterialTheme.dimensions.keyline_12),
      text = "Biography",
      style = MaterialTheme.typography.titleSmall,
    )
    ExpandingText(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = MaterialTheme.dimensions.keyline_12),
      text = biography,
      style = MaterialTheme.typography.bodyMedium,
      expandComponent = { modifier ->
        ExpandingComponents.InlineEdgeFadingEffect(
          modifier = modifier,
          text = stringResource(id = uiR.string.core_ui_read_more),
        )
      },
      shrinkComponent = { modifier, onClick ->
        ExpandingComponents.ShowLess(modifier = modifier, onClick = onClick)
      },
    )
  }
}

data class PersonalInfoSectionData(
  val title: UIText,
  val value: UIText,
)

@Composable
private fun PersonalInfoSection(section: PersonalInfoSectionData) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Text(
      text = section.title.getString(),
      style = MaterialTheme.typography.titleSmall,
    )
    Text(
      text = section.value.getString(),
      style = MaterialTheme.typography.bodyMedium,
    )
  }
}
