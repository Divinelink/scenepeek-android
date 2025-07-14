package com.divinelink.core.ui.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.component.ScenePeekLazyColumn
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.components.extensions.canScrollToTop
import kotlinx.coroutines.launch

@Composable
fun MediaListContent(
  list: List<MediaItem.Media>,
  onMediaClick: (MediaItem.Media) -> Unit,
  onLoadMore: () -> Unit,
) {
  val scrollState = rememberLazyListState()
  val scope = rememberCoroutineScope()

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = onLoadMore,
  )
  Box(Modifier.fillMaxSize()) {
    ScenePeekLazyColumn(
      modifier = Modifier.testTag(TestTags.Watchlist.WATCHLIST_CONTENT),
      state = scrollState,
      contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_12),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      items(
        items = list,
        key = { it.id },
      ) { media ->
        DetailedMediaItem(
          mediaItem = media,
          onClick = onMediaClick,
        )
      }
    }

    ScrollToTopButton(
      modifier = Modifier.align(Alignment.BottomCenter),
      visible = scrollState.canScrollToTop(),
      onClick = {
        scope.launch {
          scrollState.animateScrollToItem(0)
        }
      },
    )
  }
}

@Previews
@Composable
fun UserDataContentPreview() {
  AppTheme {
    Surface {
      MediaListContent(
        list = MediaItemFactory.MoviesList(range = 1..30),
        onMediaClick = {},
        onLoadMore = {},
      )
    }
  }
}
