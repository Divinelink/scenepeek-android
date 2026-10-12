package com.divinelink.feature.home.history

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.commons.extensions.zeroPad
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.track.WatchedEpisode
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.season_episode_format
import com.divinelink.core.ui.resources.show_episode_details_content_description
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LazyItemScope.WatchedEpisodeItem(
  boxModifier: Modifier = Modifier,
  modifier: Modifier = Modifier,
  episode: WatchedEpisode,
  onNavigate: (Navigation) -> Unit,
) {
  Box(
    modifier = boxModifier.animateItem(),
  ) {
    Row(
      modifier = modifier
        .fillMaxWidth()
        .clip(CardDefaults.shape)
        .clickable(
          onClick = {
            onNavigate(
              Navigation.DetailsRoute(
                id = episode.episode.showId,
                mediaType = MediaType.TV.value,
              ),
            )
          },
        )
        .padding(MaterialTheme.dimensions.keyline_4),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      MovieImage(
        modifier = Modifier.height(MaterialTheme.dimensions.posterSizeSmall),
        path = episode.showPosterPath,
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Text(
          maxLines = 1,
          modifier = Modifier.basicMarquee(),
          overflow = TextOverflow.Ellipsis,
          text = episode.showTitle,
          style = MaterialTheme.typography.titleSmall,
        )

        Row(
          verticalAlignment = Alignment.Top,
        ) {
          Text(
            modifier = Modifier
              .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shape.medium,
              )
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_4,
                vertical = MaterialTheme.dimensions.keyline_2,
              ),
            text = stringResource(
              UiString.season_episode_format,
              episode.episode.seasonNumber.zeroPad(),
              episode.episode.number.zeroPad(),
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          Text(
            modifier = Modifier
              .padding(
                horizontal = MaterialTheme.dimensions.keyline_4,
                vertical = MaterialTheme.dimensions.keyline_2,
              ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = episode.episode.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }
    }
    Row(
      modifier = Modifier.align(Alignment.BottomEnd),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      IconButton(
        onClick = {
          onNavigate(
            Navigation.Episode.ModalRoute(
              showId = episode.episode.showId,
              seasonNumber = episode.episode.seasonNumber,
              showTitle = episode.showTitle,
              episode = episode.episode,
            ),
          )
        },
      ) {
        Icon(
          imageVector = Icons.Default.MoreHoriz,
          tint = MaterialTheme.colorScheme.secondary,
          contentDescription = stringResource(
            UiString.show_episode_details_content_description,
            episode.episode.name,
          ),
        )
      }
    }
  }
}
