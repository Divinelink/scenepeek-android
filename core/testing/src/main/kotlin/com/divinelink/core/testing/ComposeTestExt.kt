package com.divinelink.core.testing

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.ui.snackbar.controller.ProvideSnackbarController

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
