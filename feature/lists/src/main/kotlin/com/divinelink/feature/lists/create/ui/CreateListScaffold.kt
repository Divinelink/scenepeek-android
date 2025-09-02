package com.divinelink.feature.lists.create.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.model.UIText
import com.divinelink.core.scaffold.PersistentNavigationBar
import com.divinelink.core.scaffold.PersistentNavigationRail
import com.divinelink.core.scaffold.PersistentScaffold
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.components.AppTopAppBar
import com.divinelink.core.ui.composition.PreviewLocalProvider
import com.divinelink.core.ui.snackbar.SnackbarMessageHandler
import com.divinelink.feature.lists.R
import com.divinelink.feature.lists.create.CreateListAction
import com.divinelink.feature.lists.create.CreateListUiState
import com.divinelink.feature.lists.create.ui.provider.CreateListUiStateParameterProvider
import com.divinelink.core.ui.R as uiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedVisibilityScope.CreateListScaffold(
  onNavigateUp: () -> Unit,
  uiState: CreateListUiState,
  action: (CreateListAction) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
  val topAppBarColor = TopAppBarDefaults.topAppBarColors(
    scrolledContainerColor = MaterialTheme.colorScheme.surface,
  )

  SnackbarMessageHandler(
    snackbarMessage = uiState.snackbarMessage,
    onDismissSnackbar = { action(CreateListAction.DismissSnackbar) },
  )

  rememberScaffoldState(
    animatedVisibilityScope = this,
  ).PersistentScaffold(
    modifier = Modifier.testTag(TestTags.Lists.Create.SCREEN),
    navigationRail = {
      PersistentNavigationRail()
    },
    navigationBar = {
      PersistentNavigationBar()
    },
    topBar = {
      AppTopAppBar(
        scrollBehavior = scrollBehavior,
        topAppBarColors = topAppBarColor,
        text = if (uiState.editMode) {
          UIText.ResourceText(R.string.feature_lists_edit_list)
        } else {
          UIText.ResourceText(R.string.feature_lists_add_list)
        },
        actions = {
          TextButton(
            onClick = { action(CreateListAction.CreateOrEditList) },
            enabled = uiState.name.isNotBlank(),
          ) {
            Text(
              text = if (uiState.editMode) {
                stringResource(uiR.string.core_ui_edit)
              } else {
                stringResource(uiR.string.core_ui_create)
              },
              style = MaterialTheme.typography.labelLarge,
            )
          }
        },
        onNavigateUp = onNavigateUp,
      )
    },
    content = {
      Column {
        Spacer(modifier = Modifier.padding(top = it.calculateTopPadding()))

        CreateListContent(
          uiState = uiState,
          action = { action -> action(action) },
        )
      }
    },
  )
}

@Composable
@Previews
fun CreateListScaffoldPreview(
  @PreviewParameter(CreateListUiStateParameterProvider::class) state: CreateListUiState,
) {
  SharedTransitionScopeProvider {
    val appState = rememberScenePeekAppState(
      networkMonitor = TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      preferencesRepository = TestPreferencesRepository(),
      navigationProvider = emptyList(),
    )

    ProvideScenePeekAppState(
      appState = appState,
    ) {
      appState.sharedTransitionScope = it
      PreviewLocalProvider {
        CreateListScaffold(
          onNavigateUp = {},
          uiState = state,
          action = {},
        )
      }
    }
  }
}
