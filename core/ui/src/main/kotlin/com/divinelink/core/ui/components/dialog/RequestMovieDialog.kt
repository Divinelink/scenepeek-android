package com.divinelink.core.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R

@Composable
fun RequestMovieDialog(
  title: String,
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
) {
  AlertDialog(
    title = { Text(stringResource(id = R.string.core_ui_request_movie)) },
    text = { Text(stringResource(id = R.string.core_ui_request_confirmation_text, title)) },
    onDismissRequest = onDismissRequest,
    dismissButton = {
      ElevatedButton(
        onClick = onDismissRequest,
        content = { Text(stringResource(id = R.string.core_ui_cancel)) },
      )
    },
    confirmButton = {
      Button(
        onClick = { onConfirm() },
        content = { Text(stringResource(id = R.string.core_ui_confirm)) },
      )
    },
  )
}

@Previews
@Composable
private fun RequestMovieDialogPreview() {
  AppTheme {
    RequestMovieDialog(
      title = "Movie Title",
      onDismissRequest = {},
      onConfirm = {},
    )
  }
}
