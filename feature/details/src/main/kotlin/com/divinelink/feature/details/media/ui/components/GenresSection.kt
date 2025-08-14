package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.components.details.genres.GenreLabel
import com.divinelink.feature.details.R

@Composable
fun GenresSection(
  genres: List<String>,
  onGenreClick: (String) -> Unit,
) {
  Column {
    Text(
      modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(R.string.feature_details_genres),
      style = MaterialTheme.typography.titleMedium,
    )
    FlowRow(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      genres.forEach { genre ->
        GenreLabel(
          genre = genre,
          onGenreClick = onGenreClick,
        )
      }
    }
  }
}
