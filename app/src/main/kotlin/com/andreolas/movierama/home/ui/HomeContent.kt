@file:Suppress("MagicNumber", "UnusedPrivateMember")

package com.andreolas.movierama.home.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromKoverReport
import com.andreolas.movierama.R
import com.andreolas.movierama.ui.composables.transitionspec.fadeTransitionSpec
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.SearchBarShape
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.TestTags.MOVIES_LIST_TAG
import com.divinelink.core.ui.UIText
import com.divinelink.core.ui.components.EmptySectionCard
import com.divinelink.core.ui.components.FilterBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.components.MovieRamaSearchBar
import com.divinelink.core.ui.getString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
  viewState: HomeViewState,
  modifier: Modifier = Modifier,
  onMarkAsFavoriteClicked: (MediaItem) -> Unit,
  onSearchMovies: (String) -> Unit,
  onClearClicked: () -> Unit,
  onLoadNextPage: () -> Unit,
  onGoToDetails: (MediaItem) -> Unit,
  onFilterClicked: (String) -> Unit,
  onClearFiltersClicked: () -> Unit,
  onNavigateToSettings: () -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection)
      .navigationBarsPadding(),
    topBar = {
      MovieRamaSearchBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier
          .navigationBarsPadding()
          .clip(SearchBarShape),
        actions = {
          IconButton(onClick = onNavigateToSettings) {
            Icon(
              Icons.Filled.Settings,
              stringResource(R.string.settings_button_content_description)
            )
          }
        },
        isLoading = viewState.searchLoadingIndicator,
        query = viewState.query,
        onSearchFieldChanged = { query ->
          onSearchMovies(query)
        },
        onClearClicked = onClearClicked,
      )
    },
  ) { paddingValues ->
    Column(modifier = Modifier.padding(paddingValues)) {
      AnimatedVisibility(
        visible = viewState.query.isEmpty(),
      ) {
        FilterBar(
          modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp),
          filters = viewState.filters,
          onFilterClick = { homeFilter ->
            onFilterClicked(homeFilter.name)
          },
          onClearClick = onClearFiltersClicked,
        )
      }

      if (viewState.emptyResult) {
        EmptySectionCard(
          modifier = modifier
            .padding(start = 8.dp, end = 8.dp),
          title = UIText.ResourceText(R.string.search__empty_result_title).getString(),
          description = UIText.ResourceText(R.string.search__empty_result_description).getString(),
        )
      } else {
        AnimatedContent(
          targetState = viewState.filteredResults.isNullOrEmpty(),
          transitionSpec = fadeTransitionSpec(),
          label = "Movies",
        ) { unselectedFilters ->
          when (unselectedFilters) {
            true -> {
              MoviesLazyGrid(
                modifier = Modifier,
                onMovieClicked = onGoToDetails,
                onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
                searchList = viewState.searchList,
                onLoadNextPage = onLoadNextPage,
                loadMore = viewState.loadMore,
              )
            }
            false -> {
              MoviesLazyGrid(
                modifier = Modifier,
                onMovieClicked = onGoToDetails,
                onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
                searchList = viewState.filteredResults ?: emptyList(),
                onLoadNextPage = onLoadNextPage,
                loadMore = viewState.loadMore,
              )
            }
          }
        }
      }
    }
  }
  if (viewState.initialLoading) {
    LoadingContent()
  }
}

@Composable
private fun MoviesLazyGrid(
  modifier: Modifier,
  searchList: List<MediaItem>,
  onMovieClicked: (MediaItem) -> Unit,
  onMarkAsFavoriteClicked: (MediaItem) -> Unit,
  onLoadNextPage: () -> Unit,
  loadMore: Boolean,
) {
  MediaList(
    modifier = modifier
      .fillMaxSize()
      .testTag(MOVIES_LIST_TAG),
    searches = searchList,
    onMovieClicked = onMovieClicked,
    onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
    onLoadNextPage = onLoadNextPage,
    isLoading = loadMore,
  )
}

@Composable
@ExcludeFromKoverReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun HomeContentPreview() {
  AppTheme {
    Surface {
      HomeContent(
        viewState = HomeViewState(
          isLoading = false,
          popularMovies = (1..10).map {
            MediaItem.Media.Movie(
              id = it,
              name = "Movie 1",
              posterPath = "/poster1",
              overview = "Overview 1",
              releaseDate = "2021-01-01",
              isFavorite = false,
              rating = it.toString(),
            )
          },
          error = null,
          searchResults = (1..10).map {
            MediaItem.Media.Movie(
              id = it,
              name = "Movie 1",
              posterPath = "/poster1",
              overview = "Overview 1",
              releaseDate = "2021-01-01",
              isFavorite = false,
              rating = it.toString(),
            )
          },
        ),
        onMarkAsFavoriteClicked = {},
        onLoadNextPage = {},
        onSearchMovies = {},
        onClearClicked = {},
        onGoToDetails = {},
        onFilterClicked = {},
        onClearFiltersClicked = {},
        onNavigateToSettings = {},
      )
    }
  }
}
