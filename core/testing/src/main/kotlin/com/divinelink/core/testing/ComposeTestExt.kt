package com.divinelink.core.testing

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.AnimatedVisibilityScopeProvider
import com.divinelink.core.ui.snackbar.controller.ProvideSnackbarController

fun ComposeTest.setContentWithTheme(content: @Composable () -> Unit) {
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

fun ComposeTest.setSharedLayoutContent(
  content: @Composable (
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
  ) -> Unit,
) {
  composeTestRule.setContent {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    ProvideSnackbarController(snackbarHostState, coroutineScope) {
      AnimatedVisibilityScopeProvider { transitionScope, visibilityScope ->
        content(transitionScope, visibilityScope)
      }
    }
  }
}
