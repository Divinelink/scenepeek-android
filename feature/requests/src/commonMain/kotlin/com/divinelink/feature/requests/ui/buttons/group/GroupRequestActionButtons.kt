package com.divinelink.feature.requests.ui.buttons.group

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.divinelink.core.designsystem.theme.dimensions
import com.divinelink.core.model.jellyseerr.media.RequestUiItem
import com.divinelink.core.model.jellyseerr.media.SeerrRequestAction
import com.divinelink.feature.requests.RequestsAction

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GroupRequestActionButtons(
  item: RequestUiItem,
  enabled: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  Box(
    modifier = Modifier.heightIn(
      min = MaterialTheme.dimensions.keyline_48,
    ),
  ) {
    SeerrRequestActionsButtonGroup(
      actions = item.homeActions,
      enabled = enabled,
      action = { action ->
        val seerrAction = when (action) {
          SeerrRequestAction.Approve -> RequestsAction.ApproveRequest(item.request.id)
          SeerrRequestAction.Cancel -> RequestsAction.CancelRequest(item.request.id)
          SeerrRequestAction.Decline -> RequestsAction.DeclineRequest(item.request.id)
          SeerrRequestAction.Delete -> RequestsAction.DeleteRequest(item.request.id)
          SeerrRequestAction.Edit -> RequestsAction.EditRequest(item.request)
          SeerrRequestAction.Retry -> RequestsAction.RetryRequest(item.request.id)
          SeerrRequestAction.RemoveFromRadarr,
          SeerrRequestAction.RemoveFromSonarr,
            -> RequestsAction.RemoveFromServer(
              mediaId = item.request.media.mediaId,
              requestId = item.request.id,
            )
        }

        onAction(seerrAction)
      },
    )
  }
}
