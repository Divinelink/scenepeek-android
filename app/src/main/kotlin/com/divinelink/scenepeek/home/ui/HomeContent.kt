@file:Suppress("MagicNumber", "UnusedPrivateMember")

package com.divinelink.scenepeek.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.home.HomePage
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags.MEDIA_LIST_TAG
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.Filter
import com.divinelink.core.ui.components.FilterBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.scenepeek.ui.composables.transitionspec.fadeTransitionSpec

@Composable
fun HomeContent(
  viewState: HomeViewState,
  modifier: Modifier = Modifier,
  onMarkAsFavoriteClicked: (MediaItem) -> Unit,
  onLoadNextPage: () -> Unit,
  onNavigateToDetails: (MediaItem) -> Unit,
  onFilterClick: (Filter) -> Unit,
  onClearFiltersClick: () -> Unit,
  onRetryClick: () -> Unit,
) {
  AnimatedVisibility(visible = viewState.query.isEmpty()) {
    FilterBar(
      modifier = modifier
        .padding(
          horizontal = MaterialTheme.dimensions.keyline_8,
          vertical = MaterialTheme.dimensions.keyline_4,
        ),
      filters = viewState.filters,
      onFilterClick = onFilterClick,
      onClearClick = onClearFiltersClick,
    )
  }

  AnimatedContent(
    targetState = viewState.isEmpty,
    transitionSpec = fadeTransitionSpec(),
    label = "HomeContentEmptyTransition",
  ) { isEmpty ->
    when (isEmpty) {
      true -> if (viewState.blankSlate != null) {
        BlankSlate(
          uiState = viewState.blankSlate,
          onRetry = viewState.retryAction?.let { onRetryClick },
        )
      }
      false -> AnimatedContent(
        targetState = viewState.mode,
        transitionSpec = fadeTransitionSpec(),
        label = "HomeContentTransition",
      ) { mode ->
        when (mode) {
          HomeMode.Browser -> MediaContent(
            modifier = modifier,
            section = viewState.popularMovies,
            onMediaClick = onNavigateToDetails,
            onMarkAsFavoriteClick = onMarkAsFavoriteClicked,
            onLoadNextPage = onLoadNextPage,
          )
          HomeMode.Search -> MediaContent(
            modifier = modifier,
            section = viewState.searchResults,
            onMediaClick = onNavigateToDetails,
            onMarkAsFavoriteClick = onMarkAsFavoriteClicked,
            onLoadNextPage = onLoadNextPage,
          )
          HomeMode.Filtered -> MediaContent(
            modifier = modifier,
            section = viewState.filteredResults,
            onMediaClick = onNavigateToDetails,
            onMarkAsFavoriteClick = onMarkAsFavoriteClicked,
            onLoadNextPage = onLoadNextPage,
          )
        }
      }
    }
  }
  if (viewState.initialLoading) {
    LoadingContent()
  }
}

@Composable
private fun MediaContent(
  modifier: Modifier = Modifier,
  section: MediaSection?,
  onMediaClick: (MediaItem) -> Unit,
  onMarkAsFavoriteClick: (MediaItem) -> Unit,
  onLoadNextPage: () -> Unit,
) {
  if (section == null) return

  FlatMediaList(
    modifier = modifier.testTag(MEDIA_LIST_TAG),
    data = section.data,
    onItemClick = onMediaClick,
    onMarkAsFavoriteClicked = onMarkAsFavoriteClick,
    onLoadNextPage = onLoadNextPage,
    isLoading = section.shouldLoadMore,
  )
}

@Composable
@Previews
private fun HomeContentPreview() {
  AppTheme {
    Surface {
      HomeContent(
        viewState = HomeViewState(
          isLoading = false,
          popularMovies = MediaSection(
            data = (1..10).map {
              MediaItem.Media.Movie(
                id = it,
                name = "Movie 1",
                posterPath = "/poster1",
                overview = "Overview 1",
                releaseDate = "2021-01-01",
                isFavorite = false,
                voteAverage = it.toDouble(),
                voteCount = it * 1000,
              )
            },
            shouldLoadMore = true,
          ),
          error = null,
          searchResults = MediaSection(
            data = (1..10).map {
              MediaItem.Media.Movie(
                id = it,
                name = "Movie 1",
                posterPath = "/poster1",
                overview = "Overview 1",
                releaseDate = "2021-01-01",
                isFavorite = false,
                voteAverage = it.toDouble(),
                voteCount = it * 1000,
              )
            },
            shouldLoadMore = false,
          ),
          filters = HomeFilter.entries.map { it.filter },
          filteredResults = null,
          isSearchLoading = false,
          query = "",
          pages = mapOf(
            HomePage.Popular to 1,
            HomePage.Search to 1,
          ),
          mode = HomeMode.Browser,
          retryAction = null,
        ),
        onMarkAsFavoriteClicked = {},
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = {},
      )
    }
  }
}
