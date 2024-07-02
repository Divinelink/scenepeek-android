package com.divinelink.core.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.R
import com.divinelink.core.ui.TestTags

@Composable
fun RequestMovieDialog(
  title: String,
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
) {
  AlertDialog(
    modifier = Modifier.testTag(TestTags.Dialogs.REQUEST_MOVIE_DIALOG),
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
