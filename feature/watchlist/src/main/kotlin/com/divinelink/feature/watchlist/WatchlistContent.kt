package com.divinelink.feature.watchlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.DetailedMediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.ScrollToTopButton
import com.divinelink.core.ui.components.extensions.EndlessScrollHandler
import com.divinelink.core.ui.getString
import kotlinx.coroutines.launch

@Composable
fun WatchlistContent(
  list: List<MediaItem.Media>,
  totalResults: UIText?,
  onMediaClick: (MediaItem.Media) -> Unit,
  onLoadMore: () -> Unit,
) {
  val scrollState = rememberLazyListState()
  val scope = rememberCoroutineScope()
  val showScrollToTop = remember { derivedStateOf { scrollState.firstVisibleItemIndex > 6 } }

  scrollState.EndlessScrollHandler(
    buffer = 4,
    onLoadMore = onLoadMore,
  )
  Box(Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.testTag(TestTags.Watchlist.WATCHLIST_CONTENT),
      state = scrollState,
      contentPadding = PaddingValues(MaterialTheme.dimensions.keyline_12),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      totalResults?.let {
        item {
          Text(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = MaterialTheme.dimensions.keyline_8),
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.End,
            text = totalResults.getString(),
          )
        }
      }

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
    AnimatedContent(
      modifier = Modifier
        .padding(MaterialTheme.dimensions.keyline_16)
        .align(Alignment.BottomEnd),
      targetState = showScrollToTop.value,
      label = "Show scroll to top button animation",
    ) { isVisible ->
      if (isVisible) {
        ScrollToTopButton {
          scope.launch {
            scrollState.animateScrollToItem(0)
          }
        }
      }
    }
  }
}

@Previews
@Composable
private fun WatchlistContentPreview() {
  val movie = MediaItem.Media.Movie(
    id = 0,
    posterPath = "",
    releaseDate = "2020-07-02",
    name = "Flight Club",
    rating = "9.4",
    overview = LoremIpsum(50).values.joinToString(),
    isFavorite = false,
  )
  val list = (1..10).map {
    movie.copy(id = it)
  }

  AppTheme {
    Surface {
      WatchlistContent(
        list = list,
        totalResults = UIText.StringText("You have 264 movies in your watchlist"),
        onMediaClick = {},
        onLoadMore = {},
      )
    }
  }
}
