package com.divinelink.feature.search.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.SearchBarShape
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.ScenePeekSearchBar
import com.divinelink.core.ui.components.ToolbarState
import com.divinelink.feature.search.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.SearchScreen(
  focus: Boolean,
  onNavigateToSettings: () -> Unit,
  viewModel: SearchViewModel = koinViewModel(),
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
  var focusSearchBar by remember { mutableStateOf(focus) }
  var focusTrigger by remember { mutableIntStateOf(0) }

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier
      .testTag(TestTags.Search.SEARCH_SCAFFOLD),
    topBar = {
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
          IconButton(onClick = onNavigateToSettings) {
            Icon(
              imageVector = Icons.Filled.Settings,
              contentDescription = stringResource(
                R.string.feature_search_settings_button_content_description,
              ),
            )
          }
        },
        isLoading = false, // viewState.isSearchLoading,
        query = "", // viewState.query,
        onSearchFieldChanged = {
//          viewModel::onSearchMovies
        },
        onFocused = {
          // Do nothing
        },
        onClearClicked = {
//          viewModel::onClearClicked
        },
      )
    },
    navigationRail = {
      PersistentNavigationRail()
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
    content = { paddingValues ->
      Column {
        Spacer(modifier = Modifier.padding(top = paddingValues.calculateTopPadding()))
      }
    },
  )

  // Handle focus trigger changes
  LaunchedEffect(focusTrigger) {
    if (focusTrigger > 0) {
      focusSearchBar = true
    }
  }
}
