package com.divinelink.feature.details.media.ui.forms.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Person
import com.divinelink.core.ui.components.details.cast.CreatorsItem
import com.divinelink.core.ui.components.details.cast.DirectorItem
import com.divinelink.feature.details.media.DetailsData
import com.divinelink.feature.details.media.ui.components.GenresSection

// TODO Add Previews
@Composable
fun AboutFormContent(
  modifier: Modifier = Modifier,
  aboutData: DetailsData.About,
  onGenreClick: (String) -> Unit,
  onPersonClick: (Person) -> Unit,
) {
  Column(
    modifier = modifier
      .padding(top = MaterialTheme.dimensions.keyline_16)
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    aboutData.tagline?.let {
      Text(
        text = it,
        style = MaterialTheme.typography.bodySmall,
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }

    if (!aboutData.overview.isNullOrEmpty()) {
      Text(
        text = aboutData.overview,
        style = MaterialTheme.typography.bodyMedium,
      )
      HorizontalDivider()
    }

    aboutData.genres?.let { genres ->
      GenresSection(genres, onGenreClick)
    }

    aboutData.director?.let {
      DirectorItem(director = it, onClick = onPersonClick)
    }

    aboutData.creators?.let {
      CreatorsItem(it, onPersonClick)
    }
  }
}
