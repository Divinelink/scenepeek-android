package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.commons.Constants
import com.divinelink.core.designsystem.theme.ListPaddingValues
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.shimmer.ShimmerHalfLine
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_personal_info_section
import org.jetbrains.compose.resources.stringResource

@Composable
fun PersonalDetails(data: PersonDetailsUiState.Data) {
  val isLoading = data is PersonDetailsUiState.Data.Prefetch

  Row(
    modifier = Modifier
      .testTag(TestTags.Person.PERSONAL_DETAILS)
      .padding(paddingValues = ListPaddingValues)
      .fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_12),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .weight(3f),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      Text(
        text = stringResource(Res.string.feature_details_personal_info_section),
        style = MaterialTheme.typography.titleMedium,
      )

      data.personDetails.toUiSections().forEach { section ->
        PersonalInfoSection(section = section, isLoading = isLoading)
      }
    }
  }

  PersonBiography(
    biography = data.personDetails.biography,
    isLoading = isLoading,
    name = data.personDetails.person.name,
  )
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
      color = MaterialTheme.colorScheme.tertiary,
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
