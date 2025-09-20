package com.divinelink.feature.requests.ui.buttons

import androidx.compose.runtime.Composable
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.ui.ActionButton

@Composable
fun DeclinedActionButtons(
  request: JellyseerrRequest,
  hasPermission: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  if (hasPermission) {
    ActionButton.DeleteRequest(
      onClick = { onAction(RequestsAction.DeleteRequest(request.id)) },
    )
  }
}
