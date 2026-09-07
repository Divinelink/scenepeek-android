package com.divinelink.feature.season.ui.forms.episodes

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.details.Episode
import com.divinelink.core.model.details.season.SeasonData
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.button.MarkAsCompleteIconButton
import com.divinelink.core.ui.components.details.AirDateWithRuntime
import com.divinelink.core.ui.extension.getColorRating
import com.divinelink.feature.season.SeasonAction

@Composable
fun EpisodesFormContent(
  modifier: Modifier = Modifier,
  showTitle: String,
  seasonTitle: String,
  data: SeasonData.Episodes,
  watchedEpisodes: List<Long>,
  onNavigate: (Navigation.EpisodeRoute) -> Unit,
  action: (SeasonAction) -> Unit,
) {
  ScenePeekLazyColumn(
    modifier = modifier.fillMaxSize(),
  ) {
    itemsIndexed(
      items = data.episodes,
      key = { _, episode -> episode.id },
    ) { index, episode ->
      EpisodeItem(
        episode = episode,
        action = action,
        watched = episode.id.toLong() in watchedEpisodes,
        onClick = {
          onNavigate(
            Navigation.EpisodeRoute(
              showId = episode.showId,
              showTitle = showTitle,
              seasonTitle = seasonTitle,
              seasonNumber = episode.seasonNumber,
              episodeIndex = index,
            ),
          )
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
  watched: Boolean,
  onClick: (Episode) -> Unit,
  action: (SeasonAction) -> Unit,
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
        modifier = Modifier.weight(5f),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Text(
          text = episode.name,
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.primary,
        )

        AirDateWithRuntime(
          airDate = episode.airDate,
          runtime = episode.runtime,
          style = MaterialTheme.typography.bodySmall,
        )
      }

      AnimatedContent(
        targetState = episode.accountRating != null,
      ) {
        Text(
          modifier = Modifier.padding(end = MaterialTheme.dimensions.keyline_8),
          text = episode.accountRating?.toString() ?: "",
          color = episode.accountRating?.toDouble().getColorRating(),
        )
      }

      MarkAsCompleteIconButton(
        modifier = Modifier.weight(1f),
        onClick = {
          action(
            SeasonAction.MarkEpisodeAsWatched(
              episodeId = episode.id.toLong(),
              watched = watched,
            ),
          )
        },
        isWatched = watched,
      )
    }

    HorizontalDivider(
      modifier = Modifier.padding(top = MaterialTheme.dimensions.keyline_16),
    )
  }
}
