package com.andreolas.movierama.home.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.andreolas.movierama.ExcludeFromJacocoGeneratedReport
import com.andreolas.movierama.R
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.settings.app.AppSettingsActivity
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.components.BottomSheetMovieContent
import com.andreolas.movierama.ui.components.EmptySectionCard
import com.andreolas.movierama.ui.components.FilterBar
import com.andreolas.movierama.ui.components.Material3CircularProgressIndicator
import com.andreolas.movierama.ui.components.SearchBar
import com.andreolas.movierama.ui.getString
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.SearchBarShape
import kotlinx.coroutines.launch

const val LOADING_CONTENT_TAG = "LOADING_CONTENT_TAG"
const val MOVIES_LIST_TAG = "MOVIES_LIST_TAG"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Suppress("LongMethod")
@Composable
fun HomeContent(
    viewState: HomeViewState,
    modifier: Modifier = Modifier,
    onMovieClicked: (PopularMovie) -> Unit,
    onMarkAsFavoriteClicked: (PopularMovie) -> Unit,
    onSearchMovies: (String) -> Unit,
    onClearClicked: () -> Unit,
    onLoadNextPage: () -> Unit,
    onGoToDetails: (PopularMovie) -> Unit,
    onFilterClicked: (String) -> Unit,
    onClearFiltersClicked: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )

    DisposableEffect(viewState.selectedMovie) {
        if (viewState.selectedMovie != null) {
            keyboardController?.hide()
        }

        onDispose { }
    }

    BackHandler(bottomSheetScaffoldState.bottomSheetState.isExpanded) {
        coroutineScope.launch {
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }

    val context = LocalContext.current
    BottomSheetScaffold(
        sheetElevation = 30.dp,
        sheetPeekHeight = 1.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (viewState.selectedMovie != null) {
                BottomSheetMovieContent(
                    onContentClicked = onGoToDetails,
                    movie = viewState.selectedMovie,
                    onMarkAsFavoriteClicked = { onMarkAsFavoriteClicked.invoke(it) },
                )
            }
        },
        modifier = Modifier
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchBar(
                modifier = Modifier
                    .clip(SearchBarShape),
                actions = {
                    IconButton(
                        onClick = {
                            context.startActivity(
                                Intent(context, AppSettingsActivity::class.java)
                            )
                        },
                    ) {
                        Icon(Icons.Filled.Settings, stringResource(R.string.settings_button_content_description))
                    }
                },
                isLoading = viewState.searchLoading,
                searchValue = viewState.query,
                onSearchFieldChanged = { query ->
                    onSearchMovies(query)
                },
                onClearClicked = {
                    onClearClicked()
                },
            )
        },
    ) { paddingValues ->
        AnimatedVisibility(
            visible = viewState.query.isEmpty(),
        ) {
            FilterBar(
                modifier = modifier
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
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
            PopularMoviesList(
                modifier = modifier
                    .fillMaxSize()
                    .testTag(MOVIES_LIST_TAG)
                    .padding(paddingValues),
                movies = viewState.getMoviesList(),
                onMovieClicked = {
                    onMovieClicked(it)
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                },
                onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
                onLoadNextPage = onLoadNextPage,
                isLoading = viewState.loadMore,
            )
        }
    }
    if (viewState.initialLoading) {
        LoadingContent()
    }
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
@ExcludeFromJacocoGeneratedReport
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun HomeContentPreview() {
    AppTheme {
        Surface {
            HomeContent(
                viewState = HomeViewState(
                    isLoading = false,
                    moviesList = listOf(),
                    selectedMovie = null,
                    error = null,
                ),
                onMovieClicked = {},
                onMarkAsFavoriteClicked = {},
                onLoadNextPage = {},
                onSearchMovies = {},
                onClearClicked = {},
                onGoToDetails = {},
                onFilterClicked = {},
                onClearFiltersClicked = {},
            )
        }
    }
}
