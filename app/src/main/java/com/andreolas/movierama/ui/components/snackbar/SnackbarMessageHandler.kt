package com.andreolas.movierama.ui.components.snackbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.andreolas.movierama.ui.components.snackbar.controller.LocalSnackbarController
import com.andreolas.movierama.ui.components.snackbar.controller.SnackbarController
import com.andreolas.movierama.ui.getString

@Composable
fun SnackbarMessageHandler(
  snackbarMessage: SnackbarMessage?,
  onDismissSnackbar: () -> Unit,
  onShowMessage: () -> Unit = {},
  snackbarController: SnackbarController = LocalSnackbarController.current
) {
  if (snackbarMessage == null) return

  when (snackbarMessage) {
    is SnackbarMessage.Text -> {
      val message = snackbarMessage.text.getString()
      val actionLabel = snackbarMessage.actionLabelText?.getString()

      LaunchedEffect(snackbarMessage, onDismissSnackbar) {
        onShowMessage()

        snackbarController.showMessage(
          message = message,
          actionLabel = actionLabel,
          withDismissAction = snackbarMessage.withDismissAction,
          duration = snackbarMessage.duration,
          onSnackbarResult = snackbarMessage.onSnackbarResult
        )

        onDismissSnackbar()
      }
    }

    is SnackbarMessage.Visuals -> {
      LaunchedEffect(snackbarMessage, onDismissSnackbar) {
        snackbarController.showMessage(
          snackbarVisuals = snackbarMessage.snackbarVisuals,
          onSnackbarResult = snackbarMessage.onSnackbarResult
        )

        onDismissSnackbar()
      }
    }
  }
}
