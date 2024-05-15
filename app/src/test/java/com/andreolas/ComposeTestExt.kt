package com.andreolas

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.andreolas.core.designsystem.theme.AppTheme
import com.andreolas.movierama.ui.components.snackbar.controller.ProvideSnackbarController

fun ComposeTest.setContentWithTheme(
  content: @Composable () -> Unit
) {
  composeTestRule.setContent {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    ProvideSnackbarController(snackbarHostState, coroutineScope) {
      AppTheme {
        content()
      }
    }
  }
}
