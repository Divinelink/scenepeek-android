package com.andreolas.movierama.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.andreolas.movierama.navigation.MainGraph
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import com.divinelink.feature.settings.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination<MainGraph>(start = true)
@Composable
fun HomeScreen(
  navigator: DestinationsNavigator,
  viewModel: HomeViewModel = koinViewModel(),
) {
  val viewState = viewModel.viewState.collectAsState()

  HomeContent(
    modifier = Modifier,
    viewState = viewState.value,
    onMarkAsFavoriteClicked = viewModel::onMarkAsFavoriteClicked,
    onLoadNextPage = viewModel::onLoadNextPage,
    onSearchMovies = viewModel::onSearchMovies,
    onClearClicked = viewModel::onClearClicked,
    onNavigateToDetails = { media ->
      val destination = when (media) {
        is MediaItem.Media -> {
          val navArgs = DetailsNavArguments(
            id = media.id,
            mediaType = media.mediaType.value,
            isFavorite = media.isFavorite,
          )
          DetailsScreenDestination(navArgs = navArgs)
        }
        is MediaItem.Person -> {
          PersonScreenDestination(
            navArgs = PersonNavArguments(
              id = media.id.toLong(),
              knownForDepartment = media.knownForDepartment,
              name = media.name,
              profilePath = media.profilePath,
              gender = media.gender,
            ),
          )
        }
        else -> {
          return@HomeContent
        }
      }
      navigator.navigate(destination)
    },
    onFilterClick = viewModel::onFilterClick,
    onClearFiltersClick = viewModel::onClearFiltersClicked,
    onNavigateToSettings = {
      navigator.navigate(SettingsScreenDestination())
    },
    onRetryClick = viewModel::onRetryClick,
  )
}
