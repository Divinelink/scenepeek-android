package com.divinelink.feature.add.to.account.list.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.ScaffoldFab
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.NavigateUpButton
import com.divinelink.feature.add.to.account.R
import com.divinelink.feature.add.to.account.list.AddToListAction
import com.divinelink.feature.add.to.account.list.AddToListUiState
import com.divinelink.feature.add.to.account.list.ui.provider.AddToListUiStateParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.AddToListScaffold(
  onNavigateUp: () -> Unit,
  onNavigateToCreateList: () -> Unit,
  uiState: AddToListUiState,
  action: (AddToListAction) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )
  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier.testTag(TestTags.Lists.SCREEN),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      TopAppBar(
        colors = topAppBarColor,
        scrollBehavior = scrollBehavior,
        title = {
          Text(text = stringResource(R.string.feature_add_to_account_list_title))
        },
        navigationIcon = { NavigateUpButton(onClick = onNavigateUp) },
      )
    },
    floatingActionButton = {
      if (uiState.error == null) {
        ScaffoldFab(
          modifier = Modifier.testTag(TestTags.Lists.CREATE_LIST_FAB),
          icon = Icons.Default.Add,
          text = null,
          expanded = false,
          onClick = onNavigateToCreateList,
        )
      }
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        AddToListContent(
          uiState = uiState,
          action = action,
        )
      }
    },
  )
}

@Previews
@Composable
fun AddToListScaffoldPreview(
  @PreviewParameter(AddToListUiStateParameterProvider::class) state: AddToListUiState,
) {
  val appState = rememberScenePeekAppState(
    networkMonitor = TestNetworkMonitor(),
    onboardingManager = TestOnboardingManager(),
    preferencesRepository = TestPreferencesRepository(),
    navigationProvider = emptyList(),
  )

  ProvideScenePeekAppState(
    appState = appState,
  ) {
    SharedTransitionScopeProvider {
      appState.sharedTransitionScope = it
      AppTheme {
        Surface {
          AddToListScaffold(
            onNavigateUp = {},
            onNavigateToCreateList = {},
            uiState = state,
            action = {},
          )
        }
      }
    }
  }
}
