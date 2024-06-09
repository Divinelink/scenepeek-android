package com.andreolas.movierama.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.andreolas.movierama.ui.TestTags
import com.andreolas.movierama.ui.UIText
import com.andreolas.movierama.ui.getString

@Composable
fun SimpleAlertDialog(
  modifier: Modifier = Modifier,
  confirmClick: () -> Unit,
  dismissClick: (() -> Unit) = {},
  confirmText: UIText,
  dismissText: UIText? = null,
  uiState: AlertDialogUiState,
) {
  AlertDialog(
    modifier = modifier.testTag(TestTags.Dialogs.ALERT_DIALOG),
    onDismissRequest = dismissClick,
    confirmButton = {
      TextButton(
        modifier = Modifier.testTag(TestTags.Dialogs.CONFIRM_BUTTON),
        onClick = confirmClick,
      ) {
        Text(text = confirmText.getString())
      }
    },
    dismissButton = dismissText?.let {
      {
        TextButton(
          modifier = Modifier.testTag(TestTags.Dialogs.DISMISS_BUTTON),
          onClick = dismissClick,
        ) {
          Text(text = dismissText.getString())
        }
      }
    },
    title = {
      Text(text = uiState.title.getString())
    },
    text = {
      Text(text = uiState.text.getString())
    },
  )
}
