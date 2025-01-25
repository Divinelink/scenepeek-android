package com.divinelink.scenepeek.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.SearchBarShape
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.navigation.arguments.DetailsNavArguments
import com.divinelink.core.navigation.arguments.PersonNavArguments
import com.divinelink.core.ui.components.ScenePeekSearchBar
import com.divinelink.core.ui.components.scaffold.AppScaffold
import com.divinelink.feature.details.screens.destinations.DetailsScreenDestination
import com.divinelink.feature.details.screens.destinations.PersonScreenDestination
import com.divinelink.feature.settings.screens.destinations.SettingsScreenDestination
import com.divinelink.scenepeek.R
import com.divinelink.scenepeek.navigation.MainGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination<MainGraph>(start = true)
@Composable
fun HomeScreen(
  navigator: DestinationsNavigator,
  viewModel: HomeViewModel = koinViewModel(),
) {
  val viewState = viewModel.viewState.collectAsStateWithLifecycle().value

  AppScaffold(
    topBar = { scrollBehavior, _ ->
      ScenePeekSearchBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier.clip(SearchBarShape),
        actions = {
          IconButton(
            onClick = {
              navigator.navigate(SettingsScreenDestination())
            },
          ) {
            Icon(
              imageVector = Icons.Filled.Settings,
              contentDescription = stringResource(R.string.settings_button_content_description),
            )
          }
        },
        isLoading = viewState.isSearchLoading,
        query = viewState.query,
        onSearchFieldChanged = viewModel::onSearchMovies,
        onClearClicked = viewModel::onClearClicked,
      )
    },
  ) {
    HomeContent(
      modifier = Modifier,
      viewState = viewState,
      onMarkAsFavoriteClicked = viewModel::onMarkAsFavoriteClicked,
      onLoadNextPage = viewModel::onLoadNextPage,
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
      onRetryClick = viewModel::onRetryClick,
    )
  }
}
