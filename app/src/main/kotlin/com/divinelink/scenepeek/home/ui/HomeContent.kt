@file:Suppress("MagicNumber", "UnusedPrivateMember")

package com.divinelink.scenepeek.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.LocalBottomNavigationPadding
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.home.HomeMode
import com.divinelink.core.model.home.HomePage
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.media.MediaSection
import com.divinelink.core.model.media.encodeToString
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.blankslate.BlankSlate
import com.divinelink.core.ui.components.Filter
import com.divinelink.core.ui.components.FilterBar
import com.divinelink.core.ui.components.LoadingContent
import com.divinelink.core.ui.media.MediaContent
import com.divinelink.scenepeek.ui.composables.transitionspec.fadeTransitionSpec

@Composable
fun HomeContent(
  viewState: HomeViewState,
  modifier: Modifier = Modifier,
  onLoadNextPage: () -> Unit,
  onNavigateToDetails: (MediaItem) -> Unit,
  onNavigate: (Navigation) -> Unit,
  onFilterClick: (Filter) -> Unit,
  onClearFiltersClick: () -> Unit,
  onRetryClick: () -> Unit,
) {
  Column {
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

    AnimatedContent(
      targetState = viewState.isEmpty,
      transitionSpec = fadeTransitionSpec(),
      label = "HomeContentEmptyTransition",
    ) { isEmpty ->
      when (isEmpty) {
        true -> if (viewState.blankSlate != null) {
          BlankSlate(
            modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current),
            uiState = viewState.blankSlate,
            onRetry = onRetryClick,
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
              onLoadNextPage = onLoadNextPage,
              onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
            )
            HomeMode.Filtered -> MediaContent(
              modifier = modifier,
              section = viewState.filteredResults,
              onMediaClick = onNavigateToDetails,
              onLoadNextPage = onLoadNextPage,
              onLongClick = { onNavigate(Navigation.ActionMenuRoute.Media(it.encodeToString())) },
            )
          }
        }
      }
    }
    if (viewState.initialLoading) {
      LoadingContent(modifier = Modifier.padding(bottom = LocalBottomNavigationPadding.current))
    }
  }
}

@Composable
@Previews
fun HomeContentPreview() {
  AppTheme {
    Surface {
      HomeContent(
        viewState = HomeViewState(
          isLoading = false,
          popularMovies = MediaSection(
            data = (0..12).map {
              MediaItem.Media.Movie(
                id = it,
                name = "Movie 1",
                backdropPath = "/backdrop1",
                posterPath = "/poster1",
                overview = "Overview 1",
                releaseDate = "2021-01-01",
                isFavorite = false,
                voteAverage = it.toDouble(),
                popularity = (it * 525.50),
                voteCount = it * 1000,
              )
            },
            shouldLoadMore = true,
          ),
          error = null,
          filters = HomeFilter.entries.map { it.filter },
          filteredResults = null,
          pages = mapOf(
            HomePage.Popular to 1,
          ),
          mode = HomeMode.Browser,
          retryAction = null,
        ),
        onLoadNextPage = {},
        onNavigateToDetails = {},
        onFilterClick = {},
        onClearFiltersClick = {},
        onRetryClick = {},
        onNavigate = {},
      )
    }
  }
}
