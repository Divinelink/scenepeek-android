package com.divinelink.core.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.getString

@Composable
fun TwoButtonDialog(
  state: TwoButtonDialogState,
  onConfirm: () -> Unit,
  onDismiss: () -> Unit,
  onDismissRequest: () -> Unit,
) {
  AlertDialog(
    modifier = Modifier.testTag(TestTags.Dialogs.TWO_BUTTON_DIALOG),
    onDismissRequest = onDismissRequest,
    confirmButton = {
      TextButton(
        modifier = Modifier.testTag(TestTags.Dialogs.CONFIRM_BUTTON),
        onClick = onConfirm,
      ) {
        Text(text = state.confirmButtonText.getString())
      }
    },
    dismissButton = {
      TextButton(
        modifier = Modifier.testTag(TestTags.Dialogs.DISMISS_BUTTON),
        onClick = onDismiss,
      ) {
        Text(text = state.dismissButtonText.getString())
      }
    },
    title = if (state.title == null) {
      null
    } else {
      {
        Text(text = state.title.getString())
      }
    },
    text = {
      Text(text = state.message.getString())
    },
  )
}

data class TwoButtonDialogState(
  val title: UIText? = null,
  val message: UIText,
  val confirmButtonText: UIText,
  val dismissButtonText: UIText,
)
