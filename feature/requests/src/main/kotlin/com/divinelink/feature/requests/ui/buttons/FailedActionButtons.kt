package com.divinelink.feature.requests.ui.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.ui.ActionButton

@Composable
fun FailedActionButtons(
  request: JellyseerrRequest,
  hasPermission: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  Column {
    if (hasPermission) {
      ActionButton.Retry(
        onClick = { onAction(RequestsAction.RetryRequest(request.id)) },
      )

      ActionButton.DeleteRequest(
        onClick = { onAction(RequestsAction.DeleteRequest(request.id)) },
      )

      if (request.canRemove) {
        ActionButton.RemoveFromServer(
          mediaType = request.media.mediaType,
          onClick = {
            onAction(RequestsAction.RemoveFromServer(request.id, request.media.mediaType))
          },
        )
      }
    }
  }
}
