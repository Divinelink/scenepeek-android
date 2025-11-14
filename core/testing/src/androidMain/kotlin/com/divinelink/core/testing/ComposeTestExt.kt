package com.divinelink.core.testing

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.ScaffoldState
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.composition.PreviewLocalProvider

fun ComposeTest.setContentWithTheme(content: @Composable () -> Unit) {
  composeTestRule.setContent {
    PreviewLocalProvider {
      AppTheme {
        content()
      }
    }
  }
}

fun ComposeTest.setVisibilityScopeContent(
  preferencesRepository: TestPreferencesRepository = TestPreferencesRepository(),
  content: @Composable AnimatedVisibilityScope.(transitionScope: SharedTransitionScope) -> Unit,
) {
  composeTestRule.setContent {
    val state = rememberScenePeekAppState(
      networkMonitor = com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      preferencesRepository = preferencesRepository,
      navigationProvider = listOf(),
    )
    ProvideScenePeekAppState(appState = state) {
      PreviewLocalProvider {
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
    val state = rememberScenePeekAppState(
      networkMonitor = com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      preferencesRepository = TestPreferencesRepository(),
      navigationProvider = navigationProvider,
    )

    ProvideScenePeekAppState(appState = state) {
      PreviewLocalProvider {
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
