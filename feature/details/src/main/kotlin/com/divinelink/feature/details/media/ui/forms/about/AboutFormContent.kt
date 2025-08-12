package com.divinelink.feature.details.media.ui.forms.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Person
import com.divinelink.core.model.details.media.DetailsData
import com.divinelink.core.model.details.media.MediaDetailsInformation
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.details.cast.CreatorsItem
import com.divinelink.feature.details.media.ui.components.GenresSection
import com.divinelink.feature.details.media.ui.components.MovieInformationSection

@Composable
fun AboutFormContent(
  modifier: Modifier = Modifier,
  aboutData: DetailsData.About,
  onGenreClick: (String) -> Unit,
  onPersonClick: (Person) -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = modifier.testTag(TestTags.Details.About.FORM),
    contentPadding = PaddingValues(
      top = MaterialTheme.dimensions.keyline_16,
      start = MaterialTheme.dimensions.keyline_16,
      end = MaterialTheme.dimensions.keyline_16,
    ),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    aboutData.tagline?.let {
      item {
        Text(
          text = it,
          style = MaterialTheme.typography.bodySmall,
          fontStyle = FontStyle.Italic,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }

    if (!aboutData.overview.isNullOrEmpty()) {
      item {
        Text(
          text = aboutData.overview!!,
          style = MaterialTheme.typography.bodyMedium,
        )
      }
      item {
        HorizontalDivider()
      }
    }

    aboutData.genres?.let { genres ->
      item {
        GenresSection(genres, onGenreClick)
      }
    }

    aboutData.creators?.let { creators ->
      item {
        CreatorsItem(
          creators = creators,
          onClick = onPersonClick,
        )
      }
    }

    aboutData.information?.let { information ->
      item {
        HorizontalDivider()
      }

      item {
        when (information) {
          is MediaDetailsInformation.Movie -> MovieInformationSection(information)
          MediaDetailsInformation.TV -> {
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}
