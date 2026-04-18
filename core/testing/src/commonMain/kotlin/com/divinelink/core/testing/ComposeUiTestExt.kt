package com.divinelink.core.testing

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.runComposeUiTest
import com.divinelink.core.fixtures.core.data.network.TestNetworkMonitor
import com.divinelink.core.fixtures.data.app.TestAppInfoRepository
import com.divinelink.core.fixtures.data.preferences.TestPreferencesRepository
import com.divinelink.core.fixtures.manager.TestOnboardingManager
import com.divinelink.core.navigation.Navigator
import com.divinelink.core.scaffold.ProvideScenePeekAppState
import com.divinelink.core.scaffold.ScaffoldState
import com.divinelink.core.scaffold.rememberScaffoldState
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.SharedTransitionScopeProvider
import com.divinelink.core.ui.composition.PreviewLocalProvider
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinConfiguration

@OptIn(ExperimentalTestApi::class)
fun uiTest(test: suspend ComposeUiTest.() -> Unit) = runComposeUiTest {
  test()
}

@OptIn(ExperimentalTestApi::class)
fun ComposeUiTest.setContentWithTheme(content: @Composable () -> Unit) {
  setContent {
    InitPreviewContextConfiguration()
    PreviewLocalProvider {
      KoinApplication(
        configuration = KoinConfiguration {},
      ) {
        content()
      }
    }
  }
}

fun ComposeUiTest.setVisibilityScopeContent(
  preferencesRepository: TestPreferencesRepository = TestPreferencesRepository(),
  content: @Composable AnimatedVisibilityScope.(transitionScope: SharedTransitionScope) -> Unit,
) {
  setContent {
    InitPreviewContextConfiguration()
    val state = rememberScenePeekAppState(
      networkMonitor = TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      preferencesRepository = preferencesRepository,
      appInfoRepository = TestAppInfoRepository(),
      navigator = Navigator(),
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

fun ComposeUiTest.setScaffoldContent(
  navigator: Navigator = Navigator(),
  content: @Composable ScaffoldState.() -> Unit,
) {
  setContent {
    InitPreviewContextConfiguration()
    val state = rememberScenePeekAppState(
      networkMonitor = TestNetworkMonitor(),
      onboardingManager = TestOnboardingManager(),
      preferencesRepository = TestPreferencesRepository(),
      appInfoRepository = TestAppInfoRepository(),
      navigator = navigator,
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

fun hasClickLabel(label: String) = SemanticsMatcher(
  "Clickable action with label: $label",
) {
  it.config.getOrNull(
    SemanticsActions.OnClick,
  )?.label == label
}
