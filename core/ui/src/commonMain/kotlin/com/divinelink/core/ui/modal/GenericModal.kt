package com.divinelink.core.ui.modal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.ui.composition.LocalIntentManager
import com.divinelink.core.ui.manager.url.rememberUrlHandler
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericModal(
  actions: List<GenericModalAction>,
  onDismiss: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val intentManager = LocalIntentManager.current
  val urlHandler = rememberUrlHandler()

  ModalBottomSheet(
    sheetState = sheetState,
    onDismissRequest = onDismiss,
  ) {
    LazyColumn {
      items(
        items = actions,
        key = { it.title.key },
      ) { action ->
        Row(
          modifier = Modifier
            .clickable {
              when (action) {
                is GenericModalAction.Share -> intentManager.shareText(action.url)
                is GenericModalAction.OpenInNew -> urlHandler.openUrl(action.url)
              }
            }
            .padding(MaterialTheme.dimensions.keyline_16),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_16),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            imageVector = action.icon,
            contentDescription = null,
          )

          Text(
            text = stringResource(action.title),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }
    }
  }
}
