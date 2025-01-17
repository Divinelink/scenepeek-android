package com.divinelink.scenepeek

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.divinelink.core.data.network.NetworkMonitor
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.scenepeek.ui.MovieApp
import com.divinelink.scenepeek.ui.rememberMovieAppState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@ExperimentalAnimationApi
class MainActivity :
  ComponentActivity(),
  KoinComponent {

  private val viewModel: MainViewModel by viewModel()

  private val networkMonitor: NetworkMonitor by inject<NetworkMonitor>()

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    if (intent.action == Intent.ACTION_VIEW) {
      viewModel.handleDeepLink(intent.data?.toString())
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val darkTheme = shouldUseDarkTheme(
        uiState = viewModel.uiState.collectAsState().value,
        selectedTheme = viewModel.theme.collectAsState().value,
      )

      val appState = rememberMovieAppState(
        networkMonitor = networkMonitor,
      )

      AppTheme(
        useDarkTheme = darkTheme,
        dynamicColor = viewModel.materialYou.collectAsState().value,
        blackBackground = viewModel.blackBackgrounds.collectAsState().value,
      ) {
        MovieApp(
          appState = appState,
          uiState = viewModel.uiState.collectAsState().value,
          uiEvent = viewModel.uiEvent.collectAsState().value,
          onConsumeEvent = viewModel::consumeUiEvent,
        )
      }
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
