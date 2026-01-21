package com.divinelink.feature.search.ui

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.designsystem.theme.SearchBarShape
import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.model.tab.SearchTab
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.DiscoverFab
import com.divinelink.core.ui.components.ScenePeekSearchBar
import com.divinelink.core.ui.components.ToolbarState
import com.divinelink.core.ui.components.extensions.showExpandedFab
import com.divinelink.feature.search.resources.Res
import com.divinelink.feature.search.resources.feature_search_settings_button_content_description
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.SearchScreen(
  onNavigate: (Navigation) -> Unit,
  viewModel: SearchViewModel = koinViewModel(),
  switchViewButtonViewModel: SwitchViewButtonViewModel = koinViewModel(),
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val focusManager = LocalFocusManager.current
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  var focusSearchBar by remember { mutableStateOf(false) }
  var focusTrigger by remember { mutableIntStateOf(0) }

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  val searchAllTabState = rememberLazyGridState()
  val searchMovieTabState = rememberLazyGridState()
  val searchPeopleTabState = rememberLazyGridState()
  val searchTVTabState = rememberLazyGridState()

  LaunchedEffect(uiState.query) {
    if (uiState.query != uiState.selectedQuery) {
      when (uiState.selectedTab) {
        SearchTab.All -> searchAllTabState.scrollToItem(0)
        SearchTab.Movie -> searchMovieTabState.scrollToItem(0)
        SearchTab.People -> searchPeopleTabState.scrollToItem(0)
        SearchTab.TV -> searchTVTabState.scrollToItem(0)
      }
    }
  }

  LaunchedEffect(searchAllTabState, searchMovieTabState, searchPeopleTabState, searchTVTabState) {
    snapshotFlow {
      searchAllTabState.isScrollInProgress ||
        searchPeopleTabState.isScrollInProgress ||
        searchTVTabState.isScrollInProgress ||
        searchMovieTabState.isScrollInProgress
    }
      .distinctUntilChanged()
      .collect { isScrolling ->
        if (isScrolling) {
          keyboardController?.hide()
          focusManager.clearFocus()
        }
      }
  }

  LaunchedEffect(uiState.focusSearch) {
    if (uiState.focusSearch) {
      focusSearchBar = true
      focusTrigger++
      viewModel.updateEntryPoint()
    } else {
      focusSearchBar = false
    }
  }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier
      .testTag(TestTags.Search.SEARCH_SCAFFOLD),
    topBar = {
      Column {
        ScenePeekSearchBar(
          modifier = Modifier.clip(SearchBarShape),
          scrollBehavior = scrollBehavior,
          focusTrigger = focusTrigger,
          state = if (focusSearchBar) {
            ToolbarState.Focused
          } else {
            ToolbarState.Unfocused
          },
          actions = {
            IconButton(onClick = { onNavigate(Navigation.SettingsRoute) }) {
              Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = stringResource(
                  Res.string.feature_search_settings_button_content_description,
                ),
              )
            }
          },
          isLoading = uiState.isLoading,
          query = uiState.query,
          onSearchFieldChanged = { query ->
            viewModel.onSearch(
              query = query,
              reset = true,
            )
          },
          onFocused = {
            // Do nothing
          },
          onClearClicked = viewModel::onClearClick,
        )

        SearchTabs(
          tabs = uiState.tabs,
          selected = SearchTab.fromIndex(uiState.selectedTabIndex),
          onClick = { viewModel.onSelectTab(it) },
        )
      }
    },
    navigationRail = {
      PersistentNavigationRail(
        onNavItemReselected = {
          focusTrigger++
          focusSearchBar = true
          true
        },
      )
    },
    navigationBar = {
      PersistentNavigationBar(
        onNavItemReselected = {
          focusTrigger++
          focusSearchBar = true
          true
        },
      )
    },
    floatingActionButton = {
      DiscoverFab(
        expanded = when (uiState.selectedTab) {
          SearchTab.All -> searchAllTabState.showExpandedFab()
          SearchTab.Movie -> searchMovieTabState.showExpandedFab()
          SearchTab.People -> searchPeopleTabState.showExpandedFab()
          SearchTab.TV -> searchTVTabState.showExpandedFab()
        },
        onNavigate = onNavigate,
      )
    },
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))

        SearchContent(
          uiState = uiState,
          onNavigate = onNavigate,
          onLoadNextPage = viewModel::onLoadNextPage,
          onRetryClick = viewModel::onRetryClick,
          searchAllTabState = searchAllTabState,
          searchMovieTabState = searchMovieTabState,
          searchPeopleTabState = searchPeopleTabState,
          searchTVTabState = searchTVTabState,
          onSwitchPreferences = switchViewButtonViewModel::onAction,
        )
      }
    },
  )

  LaunchedEffect(focusTrigger) {
    if (focusTrigger > 0) {
      focusSearchBar = true
    }
  }
}
