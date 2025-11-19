package com.divinelink.scenepeek.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.divinelink.core.designsystem.theme.Theme
import com.divinelink.core.ui.MainUiState

@Composable
fun shouldUseDarkTheme(
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
