package com.divinelink.feature.requests.ui.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.media.MediaType
import com.divinelink.feature.requests.RequestsAction
import com.divinelink.feature.requests.ui.ActionButton

@Composable
fun PendingActionButtons(
  request: JellyseerrRequest,
  enabled: Boolean,
  hasPermission: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  if (hasPermission) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_8),
      ) {
        ActionButton.Approve(
          modifier = Modifier.weight(1f),
          enabled = enabled,
          onClick = { onAction(RequestsAction.ApproveRequest(request.id)) },
        )
        ActionButton.Decline(
          modifier = Modifier.weight(1f),
          enabled = enabled,
          onClick = { onAction(RequestsAction.DeclineRequest(request.id)) },
        )
      }
      ActionButton.EditRequest(
        enabled = enabled,
      ) { onAction(RequestsAction.EditRequest(request.id)) }
    }
  } else {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.keyline_4),
    ) {
      if (request.media.mediaType == MediaType.TV) {
        ActionButton.EditRequest(
          enabled = enabled,
        ) { onAction(RequestsAction.EditRequest(request.id)) }
      }

      ActionButton.CancelRequest(
        enabled = enabled,
      ) { onAction(RequestsAction.CancelRequest(request.id)) }
    }
  }
}
