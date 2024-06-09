package com.divinelink.core.ui.snackbar.controller

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope

/**
 * https://afigaliyev.medium.com/snackbar-state-management-best-practices-for-jetpack-compose-1a5963d86d98
 */

@Composable
fun ProvideSnackbarController(
  snackbarHostState: SnackbarHostState,
  coroutineScope: CoroutineScope,
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalSnackbarController provides SnackbarController(
      snackbarHostState = snackbarHostState,
      coroutineScope = coroutineScope
    ),
    content = content
  )
}

val LocalSnackbarController = staticCompositionLocalOf<SnackbarController> {
  error("No SnackbarController provided.")
}
