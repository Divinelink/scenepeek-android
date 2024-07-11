package com.andreolas.movierama

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.andreolas.movierama.ui.MovieApp
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.Theme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel: MainViewModel by viewModels()

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    if (intent != null && intent.action == Intent.ACTION_VIEW) {
      viewModel.handleDeepLink(intent.data?.toString())
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val darkTheme = shouldUseDarkTheme(
        uiState = viewModel.viewState.collectAsState().value,
        selectedTheme = viewModel.theme.collectAsState().value,
      )

      AppTheme(
        useDarkTheme = darkTheme,
        dynamicColor = viewModel.materialYou.collectAsState().value,
        blackBackground = viewModel.blackBackgrounds.collectAsState().value,
      ) {
        MovieApp(
          uiState = viewModel.viewState.collectAsState().value,
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
