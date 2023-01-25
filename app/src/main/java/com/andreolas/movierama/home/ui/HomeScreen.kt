package com.andreolas.movierama.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Suppress("UnusedPrivateMember")
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val viewState = viewModel.viewState.collectAsState()
    HomeContent(
        modifier = Modifier,
        viewState = viewState.value,
        onMovieClicked = viewModel::onMovieClicked,
        onMarkAsFavoriteClicked = viewModel::onMarkAsFavoriteClicked,
        onLoadNextPage = viewModel::onLoadNextPage,
        onSearchMovies = viewModel::onSearchMovies,
        onClearClicked = viewModel::onClearClicked,
    )
}
