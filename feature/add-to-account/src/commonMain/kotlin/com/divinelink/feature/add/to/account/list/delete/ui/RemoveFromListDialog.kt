package com.divinelink.feature.add.to.account.list.delete.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.divinelink.core.designsystem.theme.AppTheme
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.ui.Previews
import com.divinelink.core.ui.TestTags
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.core_ui_cancel
import com.divinelink.core.ui.core_ui_delete
import com.divinelink.core.ui.fromHtml
import com.divinelink.feature.add.to.account.Res
import com.divinelink.feature.add.to.account.feature_add_to_account_remove_items_confirmation
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun RemoveFromListDialog(
  onDismissRequest: () -> Unit,
  onConfirm: () -> Unit,
  item: RemoveItem,
) {
  AlertDialog(
    modifier = Modifier.testTag(TestTags.Dialogs.REMOVE_ITEMS_FROM_LIST),
    text = {
      Text(
        text = pluralStringResource(
          Res.plurals.feature_add_to_account_remove_items_confirmation,
          item.size,
          item.display,
          item.listName,
        ).fromHtml(),
      )
    },
    onDismissRequest = onDismissRequest,
    dismissButton = {
      TextButton(
        onClick = onDismissRequest,
        content = { Text(stringResource(UiString.core_ui_cancel)) },
      )
    },
    confirmButton = {
      Button(
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colors.crimsonRed,
        ),
        onClick = onConfirm,
        content = {
          Text(
            text = stringResource(UiString.core_ui_delete),
            color = Color.White,
          )
        },
      )
    },
  )
}

sealed class RemoveItem(
  val display: String,
  open val listName: String,
) {
  abstract val size: Int

  data class Item(
    val name: String,
    override val listName: String,
  ) : RemoveItem(name, listName) {
    override val size: Int = 1
  }

  data class Batch(
    override val size: Int,
    override val listName: String,
  ) : RemoveItem(size.toString(), listName)
}

@Composable
@Previews
fun RemoveItemsDialogPreview() {
  AppTheme {
    Surface {
      RemoveFromListDialog(
        onDismissRequest = { },
        onConfirm = { },
        item = RemoveItem.Item("Test Item", "My list"),
      )
    }
  }
}

@Composable
@Previews
fun RemoveItemsDialogBatchPreview() {
  AppTheme {
    Surface {
      RemoveFromListDialog(
        onDismissRequest = { },
        onConfirm = { },
        item = RemoveItem.Batch(35, "My list"),
      )
    }
  }
}
