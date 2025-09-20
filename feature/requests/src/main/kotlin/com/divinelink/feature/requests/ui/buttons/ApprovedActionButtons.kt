package com.divinelink.feature.requests.ui.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.ui.ActionButton

@Composable
fun ApprovedActionButtons(
  request: JellyseerrRequest,
  hasPermission: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  if (hasPermission) {
    Column {
      ActionButton.DeleteRequest { onAction(RequestsAction.DeleteRequest(request.id)) }

      ActionButton.RemoveFromServer(
        mediaType = request.media.mediaType,
        onClick = {
          onAction(
            RequestsAction.RemoveFromServer(
              id = request.id,
              mediaType = request.media.mediaType,
            ),
          )
        },
      )
    }
  }
}
