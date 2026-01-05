package com.divinelink.scenepeek

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.scaffold.ScenePeekApp
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.scenepeek.ui.shouldUseDarkTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
  private val viewModel: MainViewModel by viewModel()

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
        onboardingManager = viewModel.onboardingManager,
        networkMonitor = viewModel.networkMonitor,
        preferencesRepository = viewModel.preferencesRepository,
        navigationProvider = viewModel.navigationProviders,
      )

      AppTheme(
        useDarkTheme = darkTheme,
        seedColor = viewModel.customColor.collectAsState().value,
        colorPreference = viewModel.colorPreference.collectAsState().value,
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

  private fun handleIntent(intent: Intent?) {
    if (intent != null && intent.action == Intent.ACTION_VIEW) {
      viewModel.handleDeepLink(intent.data.toString())
    }
  }
}
