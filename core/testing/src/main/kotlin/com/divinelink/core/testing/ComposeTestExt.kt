package com.divinelink.core.testing

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.ScaffoldState
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController

fun ComposeTest.setContentWithTheme(content: @Composable () -> Unit) {
  composeTestRule.setContent {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    ProvideSnackbarController(snackbarHostState, coroutineScope) {
      AppTheme {
        content()
      }
    }
  }
}

fun ComposeTest.setVisibilityScopeContent(
  content: @Composable AnimatedVisibilityScope.(transitionScope: SharedTransitionScope) -> Unit,
) {
  composeTestRule.setContent {
    val state = rememberScenePeekAppState(
      networkMonitor = TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      navigationProvider = listOf(),
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    ProvideScenePeekAppState(appState = state) {
      ProvideSnackbarController(snackbarHostState, coroutineScope) {
        SharedTransitionScopeProvider { sharedTransitionScope ->
          state.sharedTransitionScope = sharedTransitionScope
          content(sharedTransitionScope)
        }
      }
    }
  }
}

fun ComposeTest.setScaffoldContent(
  navigationProvider: List<NavGraphExtension> = listOf(),
  content: @Composable ScaffoldState.() -> Unit,
) {
  composeTestRule.setContent {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val state = rememberScenePeekAppState(
      networkMonitor = TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      navigationProvider = navigationProvider,
    )

    ProvideScenePeekAppState(appState = state) {
      ProvideSnackbarController(snackbarHostState, coroutineScope) {
        SharedTransitionScopeProvider {
          state.sharedTransitionScope = it

          rememberScaffoldState(
            animatedVisibilityScope = this,
          ).content()
        }
      }
    }
  }
}
