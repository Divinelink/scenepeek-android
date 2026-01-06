package com.divinelink.scenepeek

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.scaffold.ScenePeekApp
import com.divinelink.core.scaffold.rememberScenePeekAppState
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
      val uiState by viewModel.uiState.collectAsStateWithLifecycle()
      val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()

      val state = rememberScenePeekAppState(
        onboardingManager = viewModel.onboardingManager,
        networkMonitor = viewModel.networkMonitor,
        preferencesRepository = viewModel.preferencesRepository,
        navigationProvider = viewModel.navigationProviders,
      )

      ScenePeekApp(
        state = state,
        uiState = uiState,
        uiEvent = uiEvent,
        onConsumeEvent = viewModel::consumeUiEvent,
      )
    }
  }

  private fun handleIntent(intent: Intent?) {
    if (intent != null && intent.action == Intent.ACTION_VIEW) {
      viewModel.handleDeepLink(intent.data.toString())
    }
  }
}
