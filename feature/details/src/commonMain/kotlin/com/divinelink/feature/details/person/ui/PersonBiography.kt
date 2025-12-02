package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.expanding.ExpandingComponents
import com.divinelink.core.ui.components.expanding.ExpandingText
import com.divinelink.core.ui.core_ui_read_more
import com.divinelink.core.ui.shimmer.ShimmerLine
import com.divinelink.feature.details.Res
import com.divinelink.feature.details.feature_details_biography_section
import com.divinelink.feature.details.feature_details_person_blank_biography
import org.jetbrains.compose.resources.stringResource

@Composable
fun PersonBiography(
  biography: String?,
  name: String,
  isLoading: Boolean,
) {
  val personBiography = if (biography.isNullOrEmpty()) {
    stringResource(Res.string.feature_details_person_blank_biography, name)
  } else {
    biography
  }

  Column {
    Text(
      modifier = Modifier.padding(MaterialTheme.dimensions.keyline_12),
      text = stringResource(Res.string.feature_details_biography_section),
      style = MaterialTheme.typography.titleMedium,
    )
    if (isLoading) {
      Column(
        modifier = Modifier
          .testTag(TestTags.Person.SHIMMERING_BIOGRAPHY_CONTENT)
          .padding(horizontal = MaterialTheme.dimensions.keyline_12),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        for (i in 0..2) {
          ShimmerLine(tag = stringResource(Res.string.feature_details_biography_section))
        }
      }
    } else {
      ExpandingText(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = MaterialTheme.dimensions.keyline_12),
        text = personBiography,
        style = MaterialTheme.typography.bodyMedium,
        expandComponent = { modifier ->
          ExpandingComponents.InlineEdgeFadingEffect(
            modifier = modifier,
            text = stringResource(UiString.core_ui_read_more),
          )
        },
        shrinkComponent = { modifier, onClick ->
          ExpandingComponents.ShowLess(modifier = modifier, onClick = onClick)
        },
      )
    }
  }
}
