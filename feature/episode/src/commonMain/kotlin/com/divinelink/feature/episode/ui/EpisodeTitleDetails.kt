package com.divinelink.feature.episode.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.rating.RatingDetails
import com.divinelink.core.model.details.rating.RatingSource
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.components.details.AirDateWithRuntime
import com.divinelink.core.ui.rating.MediaRatingItem

@Composable
fun EpisodeTitleDetails(
  modifier: Modifier = Modifier,
  onNavigate: (Navigation) -> Unit,
  title: String,
  season: String,
  episode: Episode,
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    Row {
      Text(
        modifier = Modifier.clickable { onNavigate(Navigation.TwiceBack) },
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        text = title,
      )

      Text(
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        text = " / ",
      )

      Text(
        modifier = Modifier.clickable { onNavigate(Navigation.Back) },
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        text = season,
      )
    }

    Text(
      style = MaterialTheme.typography.titleMedium,
      text = episode.name,
    )

    AirDateWithRuntime(
      airDate = episode.airDate,
      runtime = episode.runtime,
      style = MaterialTheme.typography.titleSmall,
    )

    MediaRatingItem(
      ratingDetails = RatingDetails.Score(
        voteAverage = episode.voteAverage?.toDouble() ?: 0.0,
        voteCount = episode.voteCount ?: 0,
      ),
      source = RatingSource.TMDB,
    )
  }
}
