@file:Suppress("ktlint:standard:function-naming", "ktlint:standard:filename")

package com.divinelink.scenepeek

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.divinelink.core.scaffold.ScenePeekApp
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.scenepeek.shared.ExternalUriHandler
import org.koin.compose.viewmodel.koinViewModel
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
  val viewModel: MainViewModel = koinViewModel()

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()

  val state = rememberScenePeekAppState(
    onboardingManager = viewModel.onboardingManager,
    networkMonitor = viewModel.networkMonitor,
    preferencesRepository = viewModel.preferencesRepository,
    navigationProvider = viewModel.navigationProviders,
  )

  DisposableEffect(Unit) {
    ExternalUriHandler.listener = { uri ->
      viewModel.handleDeepLink(uri)
    }
    onDispose {
      ExternalUriHandler.listener = null
    }
  }

  ScenePeekApp(
    state = state,
    uiState = uiState,
    uiEvent = uiEvent,
    onConsumeEvent = viewModel::consumeUiEvent,
  )
}
