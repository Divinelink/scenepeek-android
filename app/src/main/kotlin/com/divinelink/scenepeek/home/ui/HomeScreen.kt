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
import com.divinelink.core.navigation.route.DetailsRoute
import com.divinelink.core.navigation.route.PersonRoute
import com.divinelink.core.ui.components.ScenePeekSearchBar
import com.divinelink.core.ui.components.scaffold.AppScaffold
import com.divinelink.scenepeek.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onNavigateToSettings: () -> Unit,
  onNavigateToDetails: (DetailsRoute) -> Unit,
  onNavigateToPerson: (PersonRoute) -> Unit,
  viewModel: HomeViewModel = koinViewModel(),
) {
  val viewState = viewModel.viewState.collectAsStateWithLifecycle().value

  AppScaffold(
    topBar = { scrollBehavior, _ ->
      ScenePeekSearchBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier.clip(SearchBarShape),
        actions = {
          IconButton(onClick = onNavigateToSettings) {
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
        when (media) {
          is MediaItem.Media -> {
            val route = DetailsRoute(
              id = media.id,
              mediaType = media.mediaType,
              isFavorite = media.isFavorite,
            )
            onNavigateToDetails(route)
          }
          is MediaItem.Person -> {
            val route = PersonRoute(
              id = media.id.toLong(),
              knownForDepartment = media.knownForDepartment,
              name = media.name,
              profilePath = media.profilePath,
              gender = media.gender,
            )
            onNavigateToPerson(route)
          }
          else -> {
            return@HomeContent
          }
        }
      },
      onFilterClick = viewModel::onFilterClick,
      onClearFiltersClick = viewModel::onClearFiltersClicked,
      onRetryClick = viewModel::onRetryClick,
    )
  }
}
