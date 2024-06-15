package com.andreolas.movierama.home.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromKoverReport
import com.andreolas.movierama.R
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.designsystem.theme.textColorDisabled
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.components.Material3CircularProgressIndicator
import com.divinelink.core.ui.components.MediaItem
import com.divinelink.core.ui.components.extensions.OnBottomReached

@Composable
fun MediaList(
  modifier: Modifier = Modifier,
  searches: List<MediaItem>,
  onMovieClicked: (MediaItem) -> Unit,
  onMarkAsFavoriteClicked: (MediaItem) -> Unit,
  onLoadNextPage: () -> Unit,
  isLoading: Boolean,
) {
  val scrollState = rememberLazyGridState()
  scrollState.OnBottomReached {
    onLoadNextPage()
  }

  LazyVerticalGrid(
    state = scrollState,
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      start = MaterialTheme.dimensions.keyline_8,
      end = MaterialTheme.dimensions.keyline_8,
      top = MaterialTheme.dimensions.keyline_8
    ),
    columns = GridCells.Adaptive(120.dp),
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
  ) {
    items(
      key = { it.id },
      items = searches
    ) { search ->
      when (search) {
        is MediaItem.Media.Movie -> MediaItem(
          movie = search,
          onMovieItemClick = { onMovieClicked(search) },
          onLikeMovieClick = { onMarkAsFavoriteClicked(search) }
        )
        is MediaItem.Media.TV -> MediaItem(
          movie = search,
          onMovieItemClick = { onMovieClicked(search) },
          onLikeMovieClick = { onMarkAsFavoriteClicked(search) }
        )
        is MediaItem.Person -> {
          // FIXME Do nothing yet
        }
        MediaItem.Unknown -> {
          // Do nothing
        }
      }
    }

    item(
      span = {
        GridItemSpan(maxLineSpan)
      }
    ) {
      AnimatedVisibility(
        visible = isLoading,
      ) {
        LoadMoreContent()
      }
    }
  }
}

@Composable
private fun LoadMoreContent(
  modifier: Modifier = Modifier,
) {
  Column(
    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .padding(vertical = MaterialTheme.dimensions.keyline_16)
      .fillMaxWidth(),
  ) {
    Material3CircularProgressIndicator(
      modifier = Modifier.wrapContentSize(),
    )

    Text(
      color = MaterialTheme.colorScheme.textColorDisabled(),
      text = stringResource(id = R.string.load_more)
    )
  }
}

@Composable
@ExcludeFromKoverReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_YES,
  device = "spec:width=1080px,height=2340px,dpi=640"
)
fun MoviesListScreenPreview() {
  @Suppress("MagicNumber")
  val movies = (1..8).map { index ->
    MediaItem.Media.Movie(
      id = index,
      posterPath = "original/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
      releaseDate = (2000 + index).toString(),
      name = "Fight Club $index",
      isFavorite = index % 2 == 0,
      rating = index.toString(),
      overview = "",
    )
  }.toMutableList()

  AppTheme {
    Surface {
      MediaList(
        searches = movies,
        onMovieClicked = {},
        onMarkAsFavoriteClicked = {},
        onLoadNextPage = {},
        isLoading = true,
      )
    }
  }
}
