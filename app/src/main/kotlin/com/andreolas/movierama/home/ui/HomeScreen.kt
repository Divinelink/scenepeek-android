package com.andreolas.movierama.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreolas.movierama.navigation.MainGraph
import com.divinelink.core.model.media.MediaItem
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.ui.DetailsNavArguments
import com.divinelink.feature.settings.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<MainGraph>(start = true)
@Composable
fun HomeScreen(
  navigator: DestinationsNavigator,
  viewModel: HomeViewModel = hiltViewModel(),
) {
  val viewState = viewModel.viewState.collectAsState()

  HomeContent(
    modifier = Modifier,
    viewState = viewState.value,
    onMarkAsFavoriteClicked = viewModel::onMarkAsFavoriteClicked,
    onLoadNextPage = viewModel::onLoadNextPage,
    onSearchMovies = viewModel::onSearchMovies,
    onClearClicked = viewModel::onClearClicked,
    onNavigateToDetails = { movie ->
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
    onFilterClick = viewModel::onFilterClick,
    onClearFiltersClick = viewModel::onClearFiltersClicked,
    onNavigateToSettings = {
      navigator.navigate(SettingsScreenDestination())
    },
  )
}
