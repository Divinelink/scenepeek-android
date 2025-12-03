package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.Genre
import com.divinelink.core.ui.components.details.genres.GenreLabel
import com.divinelink.feature.details.resources.Res
import com.divinelink.feature.details.resources.feature_details_genres
import org.jetbrains.compose.resources.stringResource

@Composable
fun GenresSection(
  genres: List<Genre>,
  onGenreClick: (Genre) -> Unit,
) {
  Column {
    Text(
      modifier = Modifier.padding(bottom = MaterialTheme.dimensions.keyline_8),
      text = stringResource(Res.string.feature_details_genres),
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
