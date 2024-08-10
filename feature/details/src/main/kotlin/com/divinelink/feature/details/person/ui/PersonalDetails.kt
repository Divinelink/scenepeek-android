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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.divinelink.core.commons.Constants
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.person.Gender
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.expanding.ExpandingComponents
import com.divinelink.core.ui.components.expanding.ExpandingText
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.shimmer.ShimmerHalfLine
import com.divinelink.core.ui.shimmer.ShimmerLine
import com.divinelink.feature.details.R
import com.divinelink.core.ui.R as uiR

@Composable
fun PersonalDetails(data: PersonDetailsUiState.Data) {
  val isLoading = data is PersonDetailsUiState.Data.Prefetch

  Row(
    modifier = Modifier
      .testTag(TestTags.Person.PERSONAL_DETAILS)
      .padding(paddingValues = ListPaddingValues)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_12),
  ) {
    MovieImage(
      modifier = Modifier.weight(1f),
      path = data.personDetails.person.profilePath,
      errorPlaceHolder = if (data.personDetails.person.gender == Gender.FEMALE) {
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
        text = stringResource(id = R.string.feature_details_personal_info_section),
        style = MaterialTheme.typography.titleMedium,
      )

      data.personDetails.toUiSections().forEach { section ->
        PersonalInfoSection(section = section, isLoading = isLoading)
      }
    }
  }

  val biography = if (data.personDetails.biography.isNullOrBlank()) {
    stringResource(
      id = R.string.feature_details_person_blank_biography,
      data.personDetails.person.name,
    )
  } else {
    data.personDetails.biography!!
  }

  Column {
    Text(
      modifier = Modifier.padding(MaterialTheme.dimensions.keyline_12),
      text = stringResource(id = R.string.feature_details_biography_section),
      style = MaterialTheme.typography.titleSmall,
    )
    if (isLoading) {
      Column(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_12),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        (0..2).forEach { _ ->
          ShimmerLine(tag = stringResource(id = R.string.feature_details_biography_section))
        }
      }
    } else {
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
}

@Composable
private fun PersonalInfoSection(
  section: PersonalInfoSectionData,
  isLoading: Boolean,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Text(
      text = section.title.getString(),
      style = MaterialTheme.typography.titleSmall,
    )
    if (isLoading && section.value.getString() == Constants.String.EMPTY_DASH) {
      ShimmerHalfLine(tag = section.title.getString())
    } else {
      Text(
        text = section.value.getString(),
        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }
}
