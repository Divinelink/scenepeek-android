package com.divinelink.scenepeek

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.divinelink.core.commons.provider.getBuildConfigProvider
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.domain.onboarding.OnboardingManager
import com.divinelink.core.scaffold.NavGraphExtension
import com.divinelink.core.scaffold.ScenePeekApp
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.core.ui.MainUiState
import com.divinelink.core.ui.composition.LocalProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@ExperimentalAnimationApi
class MainActivity :
  ComponentActivity(),
  KoinComponent {

  private val viewModel: MainViewModel by viewModel()

  private val networkMonitor: NetworkMonitor by inject<NetworkMonitor>()
  private val onboardingManager: OnboardingManager by inject<OnboardingManager>()
  private val preferencesRepository: PreferencesRepository by inject<PreferencesRepository>()
  private val navigationProviders: List<NavGraphExtension> by inject()

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    handleIntent(intent)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleIntent(intent)
    enableEdgeToEdge()
    setContent {
      val darkTheme = shouldUseDarkTheme(
        uiState = viewModel.uiState.collectAsState().value,
        selectedTheme = viewModel.theme.collectAsState().value,
      )

      val state = rememberScenePeekAppState(
        onboardingManager = onboardingManager,
        networkMonitor = networkMonitor,
        preferencesRepository = preferencesRepository,
        navigationProvider = navigationProviders,
      )

      LocalProvider(
        snackbarHostState = state.snackbarHostState,
        coroutineScope = state.scope,
        buildConfigProvider = getBuildConfigProvider(),
      ) {
        AppTheme(
          useDarkTheme = darkTheme,
          dynamicColor = viewModel.materialYou.collectAsState().value,
          blackBackground = viewModel.blackBackgrounds.collectAsState().value,
        ) {
          ScenePeekApp(
            state = state,
            uiState = viewModel.uiState.collectAsState().value,
            uiEvent = viewModel.uiEvent.collectAsState().value,
            onConsumeEvent = viewModel::consumeUiEvent,
          )
        }
      }
    }
  }

  private fun handleIntent(intent: Intent?) {
    if (intent != null && intent.action == Intent.ACTION_VIEW) {
      viewModel.handleDeepLink(intent.data?.path)
    }
  }
}

@Composable
private fun shouldUseDarkTheme(
  uiState: MainUiState,
  selectedTheme: Theme,
): Boolean = when (uiState) {
  is MainUiState.Loading -> isSystemInDarkTheme()
  is MainUiState.Completed -> when (selectedTheme) {
    Theme.SYSTEM -> isSystemInDarkTheme()
    Theme.LIGHT -> false
    Theme.DARK -> true
  }
}
