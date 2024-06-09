package com.andreolas.movierama.ui.components.snackbar.controller

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope
import javax.annotation.concurrent.Immutable

@Immutable
interface SnackbarController {
  fun showMessage(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onSnackbarResult: (SnackbarResult) -> Unit = {}
  )

  fun showMessage(
    snackbarVisuals: SnackbarVisuals,
    onSnackbarResult: (SnackbarResult) -> Unit = {}
  )
}

@Stable
fun SnackbarController(
  snackbarHostState: SnackbarHostState,
  coroutineScope: CoroutineScope
): SnackbarController = SnackbarControllerImpl(
  snackbarHostState = snackbarHostState,
  coroutineScope = coroutineScope
)
