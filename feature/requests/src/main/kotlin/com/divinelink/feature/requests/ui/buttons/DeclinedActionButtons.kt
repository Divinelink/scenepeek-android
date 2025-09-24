package com.divinelink.feature.requests.ui.buttons

import androidx.compose.runtime.Composable
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.core.ui.button.action.ActionButton

@Composable
fun DeclinedActionButtons(
  request: JellyseerrRequest,
  enabled: Boolean,
  hasPermission: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  if (hasPermission) {
    ActionButton.DeleteRequest(
      enabled = enabled,
      onClick = { onAction(RequestsAction.DeleteRequest(request.id)) },
    )
  }
}
