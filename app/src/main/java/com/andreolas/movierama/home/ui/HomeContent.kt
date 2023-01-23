package com.andreolas.movierama.home.ui

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.andreolas.movierama.home.domain.model.PopularMovie
import com.andreolas.movierama.settings.app.AppSettingsActivity
import com.andreolas.movierama.ui.components.SearchBar
import com.andreolas.movierama.ui.theme.AppTheme
import com.andreolas.movierama.ui.theme.SearchBarShape

@Suppress("UnusedPrivateMember")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    viewState: HomeViewState,
    modifier: Modifier = Modifier,
    onMovieClicked: (PopularMovie) -> Unit,
    onMarkAsFavoriteClicked: (PopularMovie) -> Unit,
    onLoadNextPage: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                        Icon(Icons.Filled.Settings, null)
                    }
                },
                onSearchFieldChanged = {
                    // todo
                },
                onClearClicked = {
                    // todo
                },
            )
        },
    ) { paddingValues ->
        PopularMoviesList(
            modifier = modifier.padding(paddingValues),
            isLoading = viewState.isLoading,
            movies = viewState.moviesList,
            onMovieClicked = onMovieClicked,
            onMarkAsFavoriteClicked = onMarkAsFavoriteClicked,
            onLoadNextPage = onLoadNextPage,
        )

        AnimatedVisibility(visible = viewState.isLoading) {
            // todo
        }
    }
}

@Composable
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
            )
        }
    }
}
