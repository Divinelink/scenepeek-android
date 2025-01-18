package com.divinelink.feature.details.person.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.feature.details.person.ui.credits.KnownForSection
import com.divinelink.feature.details.person.ui.provider.PersonUiStatePreviewParameterProvider

@Composable
fun PersonContent(
  modifier: Modifier = Modifier,
  uiState: PersonUiState,
  onMediaClick: (MediaItem) -> Unit,
) {
  uiState.personDetails as PersonDetailsUiState.Data

  ScenePeekLazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag(TestTags.Person.CONTENT_LIST),
  ) {
    item {
      Text(
        modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_12),
        style = MaterialTheme.typography.displaySmall,
        text = uiState.personDetails.personDetails.person.name,
      )
    }

    item {
      PersonalDetails(uiState.personDetails)
    }

    if (!uiState.credits.isNullOrEmpty()) {
      item {
        KnownForSection(
          list = uiState.credits,
          onMediaClick = onMediaClick,
        )
      }
    }
  }
}

@Previews
@Composable
fun PersonContentPreview(
  @PreviewParameter(PersonUiStatePreviewParameterProvider::class)
  uiState: PersonUiState,
) {
  AppTheme {
    Surface {
      PersonContent(
        uiState = uiState,
        onMediaClick = { /* Do nothing */ },
      )
    }
  }
}
