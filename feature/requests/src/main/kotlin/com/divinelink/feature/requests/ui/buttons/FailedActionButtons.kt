package com.divinelink.feature.requests.ui.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.ui.ActionButton

@Composable
fun FailedActionButtons(
  request: JellyseerrRequest,
  enabled: Boolean,
  hasPermission: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  Column {
    if (hasPermission) {
      ActionButton.Retry(
        enabled = enabled,
        onClick = { onAction(RequestsAction.RetryRequest(request.id)) },
      )

      ActionButton.DeleteRequest(
        enabled = enabled,
        onClick = { onAction(RequestsAction.DeleteRequest(request.id)) },
      )

      if (request.canRemove) {
        ActionButton.RemoveFromServer(
          mediaType = request.media.mediaType,
          enabled = enabled,
          onClick = {
            onAction(
              RequestsAction.RemoveFromServer(
                mediaId = request.jellyseerrMediaId,
                requestId = request.id,
              ),
            )
          },
        )
      }
    }
  }
}
