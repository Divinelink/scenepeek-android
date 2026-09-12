package com.divinelink.feature.home.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.extensions.daysFromToday
import com.divinelink.core.commons.extensions.zeroPad
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.model.track.NextUpEpisode
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.DisplayMessageDuration
import com.divinelink.core.ui.components.DisplayMessageSection
import com.divinelink.core.ui.composition.LocalClockProvider
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.rememberIsLandscape
import com.divinelink.core.ui.resources.days_left_to_air
import com.divinelink.core.ui.resources.season_episode_format
import com.divinelink.core.ui.resources.show_progress_empty_state_subtitle
import com.divinelink.core.ui.resources.show_progress_empty_state_title
import com.divinelink.core.ui.resources.tomorrow
import com.divinelink.core.ui.resources.upcoming
import com.divinelink.feature.home.nextup.NextUpAction
import com.divinelink.feature.home.nextup.NextUpViewModel
import com.valentinilk.shimmer.shimmer
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

enum class NextUpSection {
  Available,
  Upcoming
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NextUpContent(
  modifier: Modifier = Modifier,
  viewModel: NextUpViewModel = koinViewModel(),
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  AnimatedVisibility(
    modifier = Modifier.fillMaxWidth(),
    visible = state.displayMessage != null,
  ) {
    DisplayMessageSection(
      message = state.displayMessage,
      duration = DisplayMessageDuration.INDEFINITE,
      onTimeout = { viewModel.onAction(NextUpAction.Retry) },
    )
  }

  if (state.loading) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(MaterialTheme.dimensions.posterSizeSmall)
        .padding(horizontal = MaterialTheme.dimensions.keyline_8)
        .shimmer()
        .clip(MaterialTheme.shapes.small)
        .background(MaterialTheme.colorScheme.onSurfaceVariant),
    )
  } else {
    Column(
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    ) {
      EpisodesList(
        modifier = modifier,
        hasError = state.errors.isNotEmpty(),
        episodes = state.available,
        action = viewModel::onAction,
        section = NextUpSection.Available,
        onNavigate = onNavigate,
      )

      EpisodesList(
        modifier = modifier,
        hasError = state.errors.isNotEmpty(),
        episodes = state.upcoming,
        section = NextUpSection.Upcoming,
        action = viewModel::onAction,
        onNavigate = onNavigate,
      )
    }
  }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun EpisodesList(
  modifier: Modifier,
  hasError: Boolean,
  section: NextUpSection,
  episodes: List<NextUpEpisode>,
  action: (NextUpAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val clock = LocalClockProvider.current

  when (section) {
    NextUpSection.Available -> Unit
    NextUpSection.Upcoming -> Text(
      modifier = Modifier.padding(horizontal = MaterialTheme.dimensions.keyline_8),
      text = stringResource(UiString.upcoming),
      style = MaterialTheme.typography.titleMedium,
    )
  }

  LazyRow(
    modifier = modifier.fillMaxWidth(),
    contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_4),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    if (episodes.isEmpty() && !hasError) {
      item {
        Column(
          modifier = Modifier
            .padding(horizontal = MaterialTheme.dimensions.keyline_8)
            .conditional(
              condition = rememberIsLandscape(),
              ifTrue = { fillParentMaxWidth(0.5f) },
              ifFalse = { fillParentMaxWidth(0.85f) },
            ),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          Text(
            text = stringResource(UiString.show_progress_empty_state_title),
            style = MaterialTheme.typography.titleSmall,
          )
          Text(
            text = stringResource(UiString.show_progress_empty_state_subtitle),
            style = MaterialTheme.typography.bodyMedium,
          )
        }
      }
    } else {
      items(
        items = episodes,
        key = { it.episode.showId },
      ) { nextUp ->
        Box(
          modifier = Modifier
            .animateItem(),
        ) {
          Row(
            modifier = Modifier
              .clip(CardDefaults.shape)
              .clickable {
                onNavigate(
                  Navigation.DetailsRoute(
                    id = nextUp.episode.showId,
                    mediaType = MediaType.TV.value,
                  ),
                )
              }
              .padding(MaterialTheme.dimensions.keyline_4)
              .conditional(
                condition = rememberIsLandscape(),
                ifTrue = { fillParentMaxWidth(0.5f) },
                ifFalse = { fillParentMaxWidth(0.85f) },
              ),
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

            NextUpSection.Upcoming -> when (
              val daysLeft = nextUp.episode.airDate?.daysFromToday(clock)
            ) {
              null -> null
              1 -> stringResource(UiString.tomorrow)
              else -> stringResource(UiString.days_left_to_air, daysLeft)
            }?.let { text ->
              Text(
                modifier = Modifier
                  .padding(MaterialTheme.dimensions.keyline_8)
                  .align(Alignment.BottomEnd),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = text,
              )
            }
          }
        }
      }
    }
  }
}
