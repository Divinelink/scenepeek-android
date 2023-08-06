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
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.R
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.home.domain.model.toMovie
import com.andreolas.movierama.ui.components.Material3CircularProgressIndicator
import com.andreolas.movierama.ui.components.MovieItem
import com.andreolas.movierama.ui.components.extensions.OnBottomReached
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.dimensions
import com.andreolas.movierama.ui.theme.textColorDisabled

@Composable
fun PopularMoviesList(
  modifier: Modifier = Modifier,
  movies: List<PopularMovie>,
  onMovieClicked: (PopularMovie) -> Unit,
  onMarkAsFavoriteClicked: (PopularMovie) -> Unit,
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
      items = movies
    ) { popularMovie ->
      MovieItem(
        movie = popularMovie.toMovie(),
        onMovieItemClick = { onMovieClicked(popularMovie) },
        onLikeMovieClick = { onMarkAsFavoriteClicked(popularMovie) }
      )
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
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(
  uiMode = Configuration.UI_MODE_NIGHT_YES,
  device = "spec:width=1080px,height=2340px,dpi=640"
)
fun MoviesListScreenPreview() {
  @Suppress("MagicNumber")
  val movies = (1..8).map { index ->
    PopularMovie(
      id = index,
      posterPath = "original/A81kDB6a1K86YLlcOtZB27jriJh.jpg",
      releaseDate = (2000 + index).toString(),
      title = "Fight Club $index",
      isFavorite = index % 2 == 0,
      rating = index.toString(),
      overview = "",
    )
  }.toMutableList()

  AppTheme {
    Surface {
      PopularMoviesList(
        movies = movies,
        onMovieClicked = {},
        onMarkAsFavoriteClicked = {},
        onLoadNextPage = {},
        isLoading = true,
      )
    }
  }
}
