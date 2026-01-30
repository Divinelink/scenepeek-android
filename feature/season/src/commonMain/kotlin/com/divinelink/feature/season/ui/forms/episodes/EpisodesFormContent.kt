package com.divinelink.feature.season.ui.forms.episodes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.commons.extensions.toLocalDate
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.season.SeasonData
import com.divinelink.core.ui.extension.localizeFull

@Composable
fun EpisodesFormContent(
  modifier: Modifier = Modifier,
  data: SeasonData.Episodes,
) {
  ScenePeekLazyColumn(
    modifier = modifier.fillMaxSize(),
  ) {
    items(
      items = data.episodes,
      key = { it.id },
    ) { episode ->
      EpisodeItem(
        episode = episode,
        onClick = {
          // TODO Navigate to episode details
        },
      )
    }

    item {
      Spacer(modifier = Modifier.height(LocalBottomNavigationPadding.current))
    }
  }
}

@Composable
fun EpisodeItem(
  episode: Episode,
  onClick: (Episode) -> Unit,
) {
  Column(
    modifier = Modifier
      .clip(MaterialTheme.shapes.medium)
      .clickable { onClick(episode) }
      .padding(top = MaterialTheme.dimensions.keyline_16)
      .padding(horizontal = MaterialTheme.dimensions.keyline_16),
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text(
        text = episode.number.toString(),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Text(
          text = episode.name,
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.primary,
        )

        episode.airDate?.toLocalDate().localizeFull()?.let { airDate ->
          Text(
            text = buildString {
              append(airDate)

              episode.runtime?.let { runtime ->
                append(" • $runtime")
              }
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }

    HorizontalDivider(
      modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
    )
  }
}
