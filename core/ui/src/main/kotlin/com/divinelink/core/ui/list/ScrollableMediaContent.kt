package com.divinelink.core.ui.list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.mediaCardSize
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.ScreenSettingsRow
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.composition.rememberViewModePreferences

@Composable
fun ScrollableMediaContent(
  modifier: Modifier = Modifier,
  state: LazyGridState = rememberLazyGridState(),
  items: List<MediaItem.Media>,
  section: ViewableSection,
  onLoadMore: () -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
) {
  val viewMode = rememberViewModePreferences(section)

  val columns = when (viewMode) {
    ViewMode.GRID -> GridCells.Adaptive(mediaCardSize())
    ViewMode.LIST -> GridCells.Fixed(1)
  }

  state.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = onLoadMore,
  )

  LazyVerticalGrid(
    modifier = modifier
      .fillMaxSize()
      .testTag(TestTags.Components.MEDIA_LIST_CONTENT),
    columns = columns,
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_8,
      top = MaterialTheme.dimensions.keyline_4,
      bottom = LocalBottomNavigationPadding.current,
    ),
    state = state,
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    item(span = { GridItemSpan(maxLineSpan) }) {
      ScreenSettingsRow(
        section = section,
        onSwitchViewMode = onSwitchViewMode,
      )
    }

    items(
      items = items,
      key = { it.id },
    ) { media ->
      when (viewMode) {
        ViewMode.GRID -> MediaItem(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          media = media,
          onClick = onClick,
          onLongClick = onLongClick,
        )
        ViewMode.LIST -> DetailedMediaItem(
          modifier = Modifier
            .animateItem()
            .animateContentSize(),
          mediaItem = media,
          onClick = onClick,
          onLongClick = onLongClick,
        )
      }
    }
  }
}
