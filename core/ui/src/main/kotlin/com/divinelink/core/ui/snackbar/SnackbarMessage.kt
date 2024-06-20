package com.divinelink.core.ui.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import com.divinelink.core.ui.UIText

sealed interface SnackbarMessage {

  data class Text(
    val text: UIText,
    val actionLabelText: UIText? = null,
    val withDismissAction: Boolean = false,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val onSnackbarResult: (SnackbarResult) -> Unit = {},
  ) : SnackbarMessage

  data class Visuals(
    val snackbarVisuals: SnackbarVisuals,
    val onSnackbarResult: (SnackbarResult) -> Unit = {},
  ) : SnackbarMessage

  companion object {
    fun from(
      text: UIText,
      actionLabelText: UIText? = null,
      withDismissAction: Boolean = false,
      duration: SnackbarDuration = SnackbarDuration.Short,
      onSnackbarResult: (SnackbarResult) -> Unit = {},
    ) = Text(
      text = text,
      actionLabelText = actionLabelText,
      withDismissAction = withDismissAction,
      duration = duration,
      onSnackbarResult = onSnackbarResult,
    )

    fun from(
      snackbarVisuals: SnackbarVisuals,
      onSnackbarResult: (SnackbarResult) -> Unit,
    ) = Visuals(snackbarVisuals = snackbarVisuals, onSnackbarResult = onSnackbarResult)
  }
}
