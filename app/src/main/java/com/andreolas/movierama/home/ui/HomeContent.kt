@file:Suppress("MagicNumber", "UnusedPrivateMember")

package com.andreolas.movierama.home.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromKoverReport
import com.andreolas.movierama.R
import com.andreolas.movierama.home.domain.model.MediaItem
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.BottomSheetMovieContent
import com.andreolas.movierama.ui.components.EmptySectionCard
import com.andreolas.movierama.ui.components.FilterBar
import com.andreolas.movierama.ui.components.Material3CircularProgressIndicator
import com.andreolas.movierama.ui.components.MovieRamaSearchBar
import com.andreolas.movierama.ui.components.bottomsheet.BottomSheetUiState
import com.andreolas.movierama.ui.components.bottomsheet.animateBottomSheet
import com.andreolas.movierama.ui.composables.transitionspec.fadeTransitionSpec
import com.andreolas.movierama.ui.getString
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.SearchBarShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val LOADING_CONTENT_TAG = "LOADING_CONTENT_TAG"
const val MOVIES_LIST_TAG = "MOVIES_LIST_TAG"

@OptIn(
  ExperimentalMaterial3Api::class,
)
@Suppress("LongMethod")
@Composable
fun HomeContent(
  viewState: HomeViewState,
  modifier: Modifier = Modifier,
  onMovieClicked: (MediaItem) -> Unit,
  onMarkAsFavoriteClicked: (MediaItem) -> Unit,
  onSearchMovies: (String) -> Unit,
  onClearClicked: () -> Unit,
  onLoadNextPage: () -> Unit,
  onGoToDetails: (MediaItem) -> Unit,
  onFilterClicked: (String) -> Unit,
  onClearFiltersClicked: () -> Unit,
  onSwipeDown: () -> Unit,
  onNavigateToSettings: () -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val keyboardController = LocalSoftwareKeyboardController.current
  val scope = rememberCoroutineScope()

  val scaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberStandardBottomSheetState(
      initialValue = SheetValue.Hidden,
      skipHiddenState = false,
      confirmValueChange = {
        if (it == SheetValue.Hidden) {
          onSwipeDown()
        }
        true
      },
    )
  )

  LaunchedEffect(viewState.bottomSheetUiState) {
    if (viewState.bottomSheetUiState is BottomSheetUiState.Visible) {
      keyboardController?.hide()
    }
    animateBottomSheet(
      uiState = viewState.bottomSheetUiState,
      bottomSheetState = scaffoldState.bottomSheetState,
    )
  }

  BackHandler(scaffoldState.bottomSheetState.isVisible) {
    scope.launch {
      scaffoldState.bottomSheetState.hide()
    }
  }

  BottomSheetScaffold(
    scaffoldState = scaffoldState,
    sheetPeekHeight = 1.dp,
    sheetContent = {
      if (viewState.bottomSheetUiState is BottomSheetUiState.Visible) {
        BottomSheetMovieContent(
          onContentClicked = onGoToDetails,
          movie = viewState.bottomSheetUiState.data,
          onMarkAsFavoriteClicked = { onMarkAsFavoriteClicked.invoke(it) },
        )
      }
    },
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
    Column {
      AnimatedVisibility(
        visible = viewState.query.isEmpty(),
      ) {
        FilterBar(
          modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
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
            .padding(paddingValues)
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
                modifier = modifier,
                paddingValues = paddingValues,
                onMovieClicked = onMovieClicked,
                scope = scope,
                bottomSheetState = scaffoldState,
                onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
                searchList = viewState.searchList,
                onLoadNextPage = onLoadNextPage,
                loadMore = viewState.loadMore,
              )
            }
            false -> {
              MoviesLazyGrid(
                modifier = modifier,
                paddingValues = paddingValues,
                onMovieClicked = onMovieClicked,
                scope = scope,
                bottomSheetState = scaffoldState,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MoviesLazyGrid(
  modifier: Modifier,
  paddingValues: PaddingValues,
  searchList: List<MediaItem>,
  onMovieClicked: (MediaItem) -> Unit,
  scope: CoroutineScope,
  bottomSheetState: BottomSheetScaffoldState,
  onMarkAsFavoriteClicked: (MediaItem) -> Unit,
  onLoadNextPage: () -> Unit,
  loadMore: Boolean,
) {
  MediaList(
    modifier = modifier
      .fillMaxSize()
      .testTag(MOVIES_LIST_TAG)
      .padding(paddingValues),
    searches = searchList,
    onMovieClicked = {
      scope.launch {
        if (bottomSheetState.bottomSheetState.isVisible) {
          bottomSheetState.bottomSheetState.hide()
        }
        onMovieClicked(it)
      }
    },
    onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
    onLoadNextPage = onLoadNextPage,
    isLoading = loadMore,
  )
}

@Composable
fun LoadingContent(
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .testTag(LOADING_CONTENT_TAG)
      .fillMaxSize(),
  ) {
    Material3CircularProgressIndicator(
      modifier = Modifier
        .wrapContentSize()
        .align(Alignment.Center),
    )
  }
}

@Composable
@ExcludeFromKoverReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun HomeContentPreview() {
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
          selectedMedia = null,
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
        onMovieClicked = {},
        onMarkAsFavoriteClicked = {},
        onLoadNextPage = {},
        onSearchMovies = {},
        onClearClicked = {},
        onGoToDetails = {},
        onFilterClicked = {},
        onClearFiltersClicked = {},
        onSwipeDown = {},
        onNavigateToSettings = {},
      )
    }
  }
}
