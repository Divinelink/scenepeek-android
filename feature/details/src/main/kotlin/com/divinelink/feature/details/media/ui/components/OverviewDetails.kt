package com.divinelink.feature.details.media.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.MediaDetails
import com.divinelink.core.ui.components.details.genres.GenreLabel

@Composable
fun OverviewDetails(
  modifier: Modifier = Modifier,
  movieDetails: MediaDetails,
  genres: List<String>?,
  onGenreClicked: (String) -> Unit,
) {
  Column(
    modifier = modifier
      .padding(start = MaterialTheme.dimensions.keyline_12)
      .fillMaxWidth(),
  ) {
    if (movieDetails.genres?.isNotEmpty() == true) {
      genres?.let {
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
        ) {
          items(genres) { genre ->
            GenreLabel(
              genre = genre,
              onGenreClicked = { onGenreClicked(genre) },
            )
          }
        }
      }
    }
    if (!movieDetails.overview.isNullOrEmpty()) {
      Text(
        modifier = Modifier.padding(
          top = MaterialTheme.dimensions.keyline_16,
          bottom = MaterialTheme.dimensions.keyline_8,
        ),
        text = movieDetails.overview!!,
        style = MaterialTheme.typography.bodyMedium,
      )
    }
  }
}
