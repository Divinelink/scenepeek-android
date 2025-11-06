package com.divinelink.core.ui.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.fixtures.model.media.MediaItemFactory
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.canScrollToTop
import com.divinelink.core.ui.list.ScrollableMediaContent
import kotlinx.coroutines.launch

@Composable
fun MediaListContent(
  list: List<MediaItem.Media>,
  onClick: (MediaItem.Media) -> Unit,
  onLongClick: (MediaItem.Media) -> Unit,
  onLoadMore: () -> Unit,
  onSwitchViewMode: (ViewableSection) -> Unit,
) {
  val scrollState = rememberLazyListState()
  val scope = rememberCoroutineScope()

  Box(Modifier.fillMaxSize()) {
    ScrollableMediaContent(
      items = list,
      section = ViewableSection.USER_DATA,
      onLoadMore = onLoadMore,
      onSwitchViewMode = onSwitchViewMode,
      onClick = onClick,
      onLongClick = onLongClick,
    )

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
        onClick = {},
        onLongClick = {},
        onLoadMore = {},
        onSwitchViewMode = {},
      )
    }
  }
}
