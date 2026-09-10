package com.divinelink.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.commons.extensions.zeroPad
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.shape
import com.divinelink.core.model.DataState
import com.divinelink.core.model.media.MediaType
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.MovieImage
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.conditional
import com.divinelink.core.ui.rememberIsLandscape
import com.divinelink.core.ui.resources.no_next_up_episodes
import com.divinelink.core.ui.resources.season_episode_format
import com.divinelink.core.ui.resources.you_are_all_caught_up
import com.divinelink.feature.home.nextup.NextUpViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NextUpContent(
  modifier: Modifier = Modifier,
  viewModel: NextUpViewModel = koinViewModel(),
  onNavigate: (Navigation) -> Unit,
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  LazyRow(
    modifier = modifier.fillMaxWidth(),
    contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_4),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
  ) {
    when (state.data) {
      DataState.Initial -> Unit
      is DataState.Data -> {
        val progress = (state.data as DataState.Data).data

        if (progress.isEmpty()) {
          item {
            Column(
              modifier = Modifier
                .padding(MaterialTheme.dimensions.keyline_4)
                .conditional(
                  condition = rememberIsLandscape(),
                  ifTrue = { fillParentMaxWidth(0.5f) },
                  ifFalse = { fillParentMaxWidth(0.85f) },
                ),
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
            ) {
              Text(
                text = stringResource(UiString.you_are_all_caught_up),
                style = MaterialTheme.typography.titleMedium,
              )
              Text(
                text = stringResource(UiString.no_next_up_episodes),
                style = MaterialTheme.typography.bodyMedium,
              )
            }
          }
        } else {
          items(
            items = (state.data as DataState.Data).data,
            key = { it.episode.id },
          ) { upNext ->
            Row(
              modifier = Modifier
                .clip(CardDefaults.shape)
                .clickable {
                  onNavigate(
                    Navigation.DetailsRoute(
                      id = upNext.episode.showId,
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
                path = upNext.showPosterPath,
              )

              Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
              ) {
                Text(
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  text = upNext.showTitle,
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
                      upNext.episode.seasonNumber.zeroPad(),
                      upNext.episode.number.zeroPad(),
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
                    text = upNext.episode.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }

                IconButton(
                  onClick = { viewModel.markEpisodeAsWatched(episode = upNext.episode) },
                ) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
