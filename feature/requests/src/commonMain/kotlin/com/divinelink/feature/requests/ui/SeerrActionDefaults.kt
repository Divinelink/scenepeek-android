package com.divinelink.feature.requests.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.divinelink.core.designsystem.theme.colors
import com.divinelink.core.model.jellyseerr.media.SeerrRequestAction
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_approve
import com.divinelink.core.ui.resources.core_ui_cancel_request
import com.divinelink.core.ui.resources.core_ui_decline
import com.divinelink.core.ui.resources.core_ui_delete_request
import com.divinelink.core.ui.resources.core_ui_edit_request
import com.divinelink.core.ui.resources.core_ui_remove_from_radarr
import com.divinelink.core.ui.resources.core_ui_remove_from_sonarr
import com.divinelink.core.ui.resources.core_ui_retry
import org.jetbrains.compose.resources.StringResource

object SeerrActionDefaults {

  @Composable
  fun getColor(action: SeerrRequestAction): Color = when (action) {
    SeerrRequestAction.Approve -> MaterialTheme.colors.emeraldGreen
    SeerrRequestAction.Cancel -> MaterialTheme.colors.crimsonRed
    SeerrRequestAction.Decline -> MaterialTheme.colors.crimsonRed
    SeerrRequestAction.Delete -> MaterialTheme.colors.crimsonRed
    SeerrRequestAction.Edit -> MaterialTheme.colors.vibrantPurple
    SeerrRequestAction.Retry -> MaterialTheme.colors.vibrantPurple
    SeerrRequestAction.RemoveFromRadarr -> MaterialTheme.colors.crimsonRed
    SeerrRequestAction.RemoveFromSonarr -> MaterialTheme.colors.crimsonRed
  }

  @Composable
  fun getLabel(action: SeerrRequestAction): StringResource = when (action) {
    SeerrRequestAction.Approve -> UiString.core_ui_approve
    SeerrRequestAction.Cancel -> UiString.core_ui_cancel_request
    SeerrRequestAction.Decline -> UiString.core_ui_decline
    SeerrRequestAction.Delete -> UiString.core_ui_delete_request
    SeerrRequestAction.Edit -> UiString.core_ui_edit_request
    SeerrRequestAction.RemoveFromRadarr -> UiString.core_ui_remove_from_radarr
    SeerrRequestAction.RemoveFromSonarr -> UiString.core_ui_remove_from_sonarr
    SeerrRequestAction.Retry -> UiString.core_ui_retry
  }
}
