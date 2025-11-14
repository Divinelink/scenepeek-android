package com.divinelink.core.ui.snackbar.controller

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Immutable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Immutable
class SnackbarControllerImpl(
  private val snackbarHostState: SnackbarHostState,
  private val coroutineScope: CoroutineScope,
) : SnackbarController {
  override fun showMessage(
    message: String,
    actionLabel: String?,
    withDismissAction: Boolean,
    duration: SnackbarDuration,
    onSnackbarResult: (SnackbarResult) -> Unit,
  ) {
    coroutineScope.launch {
      snackbarHostState.currentSnackbarData?.dismiss()

      snackbarHostState.showSnackbar(
        message = message,
        actionLabel = actionLabel,
        withDismissAction = withDismissAction,
        duration = duration,
      ).let(onSnackbarResult)
    }
  }

  override fun showMessage(
    snackbarVisuals: SnackbarVisuals,
    onSnackbarResult: (SnackbarResult) -> Unit,
  ) {
    coroutineScope.launch {
      snackbarHostState.currentSnackbarData?.dismiss()

      snackbarHostState.showSnackbar(visuals = snackbarVisuals).let(onSnackbarResult)
    }
  }

  override fun isVisible(): Boolean = snackbarHostState.currentSnackbarData != null
}
