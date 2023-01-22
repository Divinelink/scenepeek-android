package com.andreolas.movierama.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.home.ui.HomeContent
import com.andreolas.movierama.home.ui.HomeViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(
    start = true
)
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
    )
}
