package com.andreolas.movierama.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.destinations.DetailsScreenDestination
import com.andreolas.movierama.destinations.SettingsScreenDestination
import com.andreolas.movierama.details.ui.DetailsNavArguments
import com.andreolas.movierama.home.domain.model.MediaItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
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
    onGoToDetails = { movie ->
      if (movie is MediaItem.Media) { // FIXME
        val navArgs = DetailsNavArguments(
          id = movie.id,
          mediaType = movie.mediaType.value,
          isFavorite = movie.isFavorite,
        )
        val destination = DetailsScreenDestination(
          navArgs = navArgs,
        )
        navigator.navigate(destination)
      }
    },
    onFilterClicked = viewModel::onFilterClicked,
    onClearFiltersClicked = viewModel::onClearFiltersClicked,
    onSwipeDown = viewModel::onSwipeDown,
    onNavigateToSettings = {
      navigator.navigate(SettingsScreenDestination())
    }
  )
}
