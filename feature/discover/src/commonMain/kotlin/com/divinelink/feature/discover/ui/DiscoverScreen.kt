package com.divinelink.feature.discover.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.domain.components.SwitchViewButtonViewModel
import com.divinelink.core.model.UIText
import com.divinelink.core.navigation.route.Navigation
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.resources.core_ui_discover
import com.divinelink.feature.discover.DiscoverViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.DiscoverScreen(
  route: Navigation.DiscoverRoute,
  onNavigate: (Navigation) -> Unit,
  viewModel: DiscoverViewModel = koinViewModel { parametersOf(route) },
  switchViewButtonViewModel: SwitchViewButtonViewModel = koinViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      Column {
        AnimatedVisibility(uiState.isLoading) {
          LinearProgressIndicator(
            modifier = Modifier
              .testTag(TestTags.LINEAR_LOADING_INDICATOR)
              .fillMaxWidth(),
          )
        }

        AppTopAppBar(
          text = UIText.ResourceText(UiString.core_ui_discover),
          scrollBehavior = scrollBehavior,
          topAppBarColors = topAppBarColor,
          onNavigateUp = { onNavigate(Navigation.Back) },
        )
      }
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        DiscoverContent(
          uiState = uiState,
          action = viewModel::onAction,
          onNavigate = onNavigate,
          onSwitchPreferences = switchViewButtonViewModel::onAction,
        )
      }
    },
  )
}
