package com.divinelink.feature.requests.ui.buttons

import androidx.compose.runtime.Composable
import com.divinelink.core.model.jellyseerr.media.JellyseerrRequest
import com.divinelink.core.model.jellyseerr.media.JellyseerrStatus
import com.divinelink.feature.requests.RequestsAction

/**
 * @param compact is used on home screen actions in order to reduce space and display only the
 * main actions.
 */
@Composable
fun ActionButtons(
  request: JellyseerrRequest,
  enabled: Boolean,
  canManageRequest: Boolean,
  canRequestAdvanced: Boolean,
  compact: Boolean,
  onAction: (RequestsAction) -> Unit,
) {
  when (request.requestStatus) {
    JellyseerrStatus.Request.PENDING -> PendingActionButtons(
      canManageRequests = canManageRequest,
      canRequestAdvanced = canRequestAdvanced,
      onAction = onAction,
      enabled = enabled,
      request = request,
      compact = compact,
    )
    JellyseerrStatus.Request.APPROVED -> ApprovedActionButtons(
      hasPermission = canManageRequest,
      onAction = onAction,
      enabled = enabled,
      request = request,
    )

    JellyseerrStatus.Request.DECLINED -> DeclinedActionButtons(
      hasPermission = canManageRequest,
      onAction = onAction,
      enabled = enabled,
      request = request,
    )

    JellyseerrStatus.Request.FAILED -> FailedActionButtons(
      hasPermission = canManageRequest,
      onAction = onAction,
      enabled = enabled,
      request = request,
    )
    JellyseerrStatus.Request.UNKNOWN -> Unit
  }
}
