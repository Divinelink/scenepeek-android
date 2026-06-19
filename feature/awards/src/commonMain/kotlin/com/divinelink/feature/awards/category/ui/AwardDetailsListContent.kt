package com.divinelink.feature.awards.category.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.text.style.TextAlign
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.mediaCardSize
import com.divinelink.core.model.ItemState
import com.divinelink.core.model.LoadingUiItem
import com.divinelink.core.model.awards.AwardNominee
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.utilities.toRoute
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.credit.PersonItem
import com.divinelink.core.ui.skeleton.MediaItemSkeleton
import com.divinelink.feature.awards.category.AwardCategoryAction
import com.divinelink.feature.awards.category.AwardCategoryUiState

@Composable
fun AwardCategoriesListContent(
  uiState: AwardCategoryUiState,
  action: (AwardCategoryAction) -> Unit,
  onNavigate: (Navigation) -> Unit,
) {
  val state = rememberLazyGridState()

  LazyVerticalGrid(
    state = state,
    modifier = Modifier,
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_8,
      top = MaterialTheme.dimensions.keyline_8,
      bottom = LocalBottomNavigationPadding.current,
    ),
    columns = GridCells.Adaptive(mediaCardSize()),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    uiState.awards.entries.forEach { award ->
      stickyHeader(key = award.key) {
        Text(
          modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.dimensions.keyline_8),
          text = award.key,
          style = MaterialTheme.typography.titleMedium,
        )
      }

      items(
        items = award.value,
        key = { award.key + it.item.media.mediaId },
      ) { item ->
        NomineeMediaItem(
          modifier = Modifier
            .onFirstVisible(
              minDurationMs = 100L,
              minFractionVisible = 0.1f,
            ) {
              action.invoke(AwardCategoryAction.FetchMediaItem(item))
            }
            .animateItem(),
          item = item,
          onClick = { it.toRoute()?.let { route -> onNavigate(route) } },
          onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
        )
      }
    }
  }
}

@Composable
fun LazyGridItemScope.NomineeMediaItem(
  modifier: Modifier = Modifier,
  item: LoadingUiItem<AwardNominee>,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  when (val state = item.mediaState) {
    is ItemState.Data -> when (state.item) {
      is MediaItem.Media -> MediaItem(
        modifier = Modifier
          .animateItem(),
        media = state.item as MediaItem.Media,
        onClick = { onClick(state.item as MediaItem.Media) },
        onLongClick = { onLongClick(state.item as MediaItem.Media) },
        contentAboveTitle = {
          Box(modifier = Modifier.fillMaxWidth()) {
            Text(
              modifier = Modifier
                .align(Alignment.Center)
                .padding(
                  top = MaterialTheme.dimensions.keyline_4,
                  bottom = MaterialTheme.dimensions.keyline_12,
                )
                .clip(MaterialTheme.shapes.large)
                .background(
                  if (item.item.winner) {
                    MaterialTheme.colors.emeraldGreen
                  } else {
                    MaterialTheme.colors.gray
                  },
                )
                .padding(
                  vertical = MaterialTheme.dimensions.keyline_4,
                  horizontal = MaterialTheme.dimensions.keyline_12,
                ),
              style = MaterialTheme.typography.bodySmall,
              textAlign = TextAlign.Center,
              text = if (item.item.winner) {
                "Winner"
              } else {
                "Nominee"
              },
              color = Color.White,
            )
          }
        },
      )
      is MediaItem.Person -> Unit
      MediaItem.Unknown -> Unit
    }

    ItemState.Loading -> MediaItemSkeleton(
      modifier = modifier,
    )

    null -> Unit
  }
}

