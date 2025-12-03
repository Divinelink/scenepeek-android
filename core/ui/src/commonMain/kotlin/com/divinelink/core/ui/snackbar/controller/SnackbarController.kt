package com.divinelink.core.ui.snackbar.controller

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope

@Immutable
interface SnackbarController {
  fun showMessage(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onSnackbarResult: (SnackbarResult) -> Unit = {},
  )

  fun showMessage(
    snackbarVisuals: SnackbarVisuals,
    onSnackbarResult: (SnackbarResult) -> Unit = {},
  )

  fun isVisible(): Boolean
}

@Stable
fun SnackbarController(
  snackbarHostState: SnackbarHostState,
  coroutineScope: CoroutineScope,
): SnackbarController = SnackbarControllerImpl(
  snackbarHostState = snackbarHostState,
  coroutineScope = coroutineScope,
)
