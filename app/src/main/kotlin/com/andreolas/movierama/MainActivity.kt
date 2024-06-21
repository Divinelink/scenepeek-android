package com.andreolas.movierama

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
        )
      }
    }
  }
}

@Composable
private fun shouldUseDarkTheme(
  uiState: MainViewState,
  selectedTheme: Theme,
): Boolean = when (uiState) {
  is MainViewState.Loading -> isSystemInDarkTheme()
  is MainViewState.Completed -> when (selectedTheme) {
    Theme.SYSTEM -> isSystemInDarkTheme()
    Theme.LIGHT -> false
    Theme.DARK -> true
  }
}
