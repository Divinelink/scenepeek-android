package com.divinelink.scenepeek

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.ComposeUIViewController
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.scaffold.ScenePeekApp
import com.divinelink.core.scaffold.rememberScenePeekAppState
import com.divinelink.scenepeek.ui.shouldUseDarkTheme
import org.koin.compose.viewmodel.koinViewModel
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
  val viewModel: MainViewModel = koinViewModel()

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
