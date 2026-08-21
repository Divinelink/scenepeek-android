package com.divinelink.feature.requests.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiPlurals
import com.divinelink.core.ui.coil.OpaqueBackdropImage
import com.divinelink.core.ui.components.modal.jellyseerr.manage.SeasonPill
import com.divinelink.core.ui.media.MediaImage
import com.divinelink.core.ui.resources.core_ui_season
import com.divinelink.core.ui.skeleton.RequestItemSkeleton
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.ui.buttons.group.GroupRequestActionButtons
import org.jetbrains.compose.resources.pluralStringResource

@Composable
fun LazyItemScope.HomeRequestMediaItem(
  modifier: Modifier = Modifier,
  item: RequestUiItem,
  onAction: (RequestsAction) -> Unit,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  when (val state = item.mediaState) {
    is ItemState.Data -> Card(
      modifier = modifier
        .animateItem()
        .combinedClickable(
          onClick = { onClick(state.item) },
          onLongClick = { onLongClick(state.item) },
        )
        .fillMaxWidth(),
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        OpaqueBackdropImage(
          modifier = Modifier.matchParentSize(),
          path = state.item.backdropPath,
        )

        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimensions.keyline_8),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
          ) {
            Column(
              modifier = Modifier
                .weight(1f)
                .padding(end = MaterialTheme.dimensions.keyline_8),
              verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
            ) {
              Text(
                text = state.item.releaseDate.take(4),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
              )
              Text(
                text = state.item.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
              )

              Text(
                text = item.request.requester.displayName,
                style = MaterialTheme.typography.bodyMedium,
              )

              if (item.request.seasons.isNotEmpty()) {
                Row(
                  horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Text(
                    text = pluralStringResource(
                      UiPlurals.core_ui_season,
                      item.request.seasons.size,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                  LazyRow(
                    contentPadding = PaddingValues(horizontal = MaterialTheme.dimensions.keyline_8),
                    horizontalArrangement = Arrangement.spacedBy(
                      space = MaterialTheme.dimensions.keyline_4,
                    ),
                  ) {
                    items(item.request.seasons) { season ->
                      SeasonPill(season.seasonNumber)
                    }
                  }
                }
              }

              StatusSection(item)
            }

            MediaImage(
              media = state.item,
              modifier = Modifier.height(MaterialTheme.dimensions.posterSizeSmall),
            )
          }

          GroupRequestActionButtons(
            item = item,
            enabled = !state.loading,
            onAction = onAction,
          )

          if (state.loading) {
            LinearProgressIndicator(
              modifier = Modifier
                .testTag(TestTags.LINEAR_LOADING_INDICATOR)
                .fillMaxWidth(),
            )
          }
        }
      }
    }

    ItemState.Loading -> RequestItemSkeleton(modifier = modifier)
    null -> Unit
  }
}
