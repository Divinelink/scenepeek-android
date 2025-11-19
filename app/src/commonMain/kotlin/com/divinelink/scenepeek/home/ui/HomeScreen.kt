package com.divinelink.scenepeek.home.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.model.media.MediaItem
import com.divinelink.core.model.search.SearchEntryPoint
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.navigation.route.Navigation.DetailsRoute
import com.divinelink.core.navigation.route.Navigation.PersonRoute
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.components.DiscoverFab
import com.divinelink.core.ui.components.ScenePeekSearchBar
import com.divinelink.core.ui.components.extensions.showExpandedFab
import com.divinelink.scenepeek.Res
import com.divinelink.scenepeek.settings_button_content_description
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  onNavigate: (Navigation) -> Unit,
  animatedVisibilityScope: AnimatedVisibilityScope,
  viewModel: HomeViewModel = koinViewModel(),
) {
  val viewState by viewModel.viewState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val browserScrollState = rememberLazyGridState()
  val filterScrollState = rememberLazyGridState()

  rememberScaffoldState(
    animatedVisibilityScope = animatedVisibilityScope,
  ).PersistentScaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      ScenePeekSearchBar(
        scrollBehavior = scrollBehavior,
        modifier = Modifier,
        onFocused = {
          viewModel.onNavigateToSearch()
          onNavigate(Navigation.SearchRoute(SearchEntryPoint.HOME))
        },
        actions = {
          IconButton(onClick = { onNavigate(Navigation.SettingsRoute) }) {
            Icon(
              imageVector = Icons.Filled.Settings,
              contentDescription = stringResource(Res.string.settings_button_content_description),
            )
          }
        },
        isLoading = false,
        isSearchable = false,
        query = "",
        onSearchFieldChanged = {
          // Do nothing
        },
        onClearClicked = {
          // Do nothing
        },
      )
    },
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    floatingActionButton = {
      DiscoverFab(
        expanded = browserScrollState.showExpandedFab() && filterScrollState.showExpandedFab(),
        onNavigate = onNavigate,
      )
    },
  ) {
    Column {
      Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

      HomeContent(
        modifier = Modifier,
        viewState = viewState,
        onLoadNextPage = viewModel::onLoadNextPage,
        onNavigateToDetails = { media ->
          when (media) {
            is MediaItem.Media -> {
              val route = DetailsRoute(
                id = media.id,
                mediaType = media.mediaType.value,
                isFavorite = media.isFavorite,
              )
              onNavigate(route)
            }
            is MediaItem.Person -> {
              val route = PersonRoute(
                id = media.id.toLong(),
                knownForDepartment = media.knownForDepartment,
                name = media.name,
                profilePath = media.profilePath,
                gender = media.gender.value,
              )
              onNavigate(route)
            }
            else -> {
              return@HomeContent
            }
          }
        },
        onFilterClick = viewModel::onFilterClick,
        onClearFiltersClick = viewModel::onClearFiltersClicked,
        onRetryClick = viewModel::onRetryClick,
        onNavigate = onNavigate,
        browserScrollState = browserScrollState,
        filterScrollState = filterScrollState,
      )
    }
  }
}
