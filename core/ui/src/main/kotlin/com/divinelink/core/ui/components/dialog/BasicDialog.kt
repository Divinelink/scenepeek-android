package com.divinelink.core.ui.components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.composition.LocalIntentManager
import com.divinelink.core.ui.getString
import com.divinelink.core.ui.manager.IntentManager

@Composable
fun BasicDialog(
  dialogState: DialogState,
  onConfirm: () -> Unit = {},
  onDismissRequest: () -> Unit,
  intentManager: IntentManager = LocalIntentManager.current,
) {
  AlertDialog(
    onDismissRequest = onDismissRequest,
    confirmButton = {
      TextButton(
        content = {
          Text(text = stringResource(id = UiString.core_ui_okay))
        },
        onClick = {
          onConfirm()
          onDismissRequest()
        },
        modifier = Modifier.testTag(TestTags.Dialogs.CONFIRM_BUTTON),
      )
    },
    dismissButton = dialogState.error?.let { error ->
      {
        TextButton(
          content = {
            Text(text = stringResource(id = UiString.core_ui_share_error_details))
          },
          onClick = {
            intentManager.shareErrorReport(throwable = error)
            onDismissRequest()
          },
          modifier = Modifier.testTag(TestTags.Dialogs.SHARE_ERROR_BUTTON),
        )
      }
    },
    title = dialogState.title?.getString()?.let {
      {
        Text(
          text = it,
          style = MaterialTheme.typography.headlineSmall,
        )
      }
    },
    text = {
      Text(
        text = dialogState.message.getString(),
        style = MaterialTheme.typography.bodyMedium,
      )
    },
    modifier = Modifier.semantics {
      testTag = TestTags.Dialogs.ALERT_DIALOG
    },
  )
}

data class DialogState(
  val title: UIText? = null,
  val message: UIText,
  val error: Throwable?,
)
