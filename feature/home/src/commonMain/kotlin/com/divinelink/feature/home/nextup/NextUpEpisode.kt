package com.divinelink.feature.home.nextup

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonGroupDefaults.connectedMiddleButtonPressShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonShapes
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.commons.extensions.daysFromToday
import com.divinelink.core.commons.extensions.zeroPad
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.model.track.NextUpSection
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.LocalClockProvider
import com.divinelink.core.ui.resources.days_left_to_air
import com.divinelink.core.ui.resources.season_episode_format
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LazyItemScope.NextUpEpisode(
  boxModifier: Modifier = Modifier,
  modifier: Modifier = Modifier,
  nextUp: NextUpEpisode,
  section: NextUpSection,
  action: (NextUpAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val clock = LocalClockProvider.current

  Box(
    modifier = boxModifier.animateItem(),
  ) {
    Row(
      modifier = modifier
        .clip(CardDefaults.shape)
        .clickable {
          onNavigate(
            Navigation.DetailsRoute(
              id = nextUp.episode.showId,
              mediaType = MediaType.TV.value,
            ),
          )
        }
        .padding(MaterialTheme.dimensions.keyline_4),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      MovieImage(
        modifier = Modifier.height(MaterialTheme.dimensions.posterSizeSmall),
        path = nextUp.showPosterPath,
      )

      Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
      ) {
        Text(
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          text = nextUp.showTitle,
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
              nextUp.episode.seasonNumber.zeroPad(),
              nextUp.episode.number.zeroPad(),
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
            text = nextUp.episode.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
        ) {
          Text(
            modifier = Modifier,
            text = "${nextUp.watchedEpisodes}/${nextUp.totalEpisodes}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )

          LinearProgressIndicator(
            modifier = Modifier
              .fillMaxWidth()
              .height(MaterialTheme.dimensions.keyline_8),
            progress = {
              nextUp.watchedEpisodes.toFloat() / nextUp.totalEpisodes.toFloat()
            },
            drawStopIndicator = {},
          )
        }
      }
    }
    when (section) {
      NextUpSection.Available -> FilledTonalIconButton(
        modifier = Modifier.align(Alignment.BottomEnd),
        shapes = IconButtonShapes(
          shape = ShapeDefaults.ExtraExtraLarge,
          pressedShape = connectedMiddleButtonPressShape,
        ),
        onClick = { action(NextUpAction.MarkEpisodeAsWatched(nextUp.episode)) },
      ) {
        Icon(
          imageVector = Icons.Default.Check,
          contentDescription = null,
        )
      }

      NextUpSection.Upcoming -> {
        val daysLeft = nextUp.episode.airDate?.daysFromToday(clock) ?: 0
        val resource = pluralStringResource(
          UiPlurals.days_left_to_air,
          daysLeft,
          daysLeft,
        )

        Text(
          modifier = Modifier
            .padding(MaterialTheme.dimensions.keyline_8)
            .align(Alignment.BottomEnd),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          text = resource,
        )
      }
    }
  }
}
