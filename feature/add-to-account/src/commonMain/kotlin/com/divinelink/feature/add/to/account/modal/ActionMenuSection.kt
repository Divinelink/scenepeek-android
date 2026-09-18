package com.divinelink.feature.add.to.account.modal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.automirrored.outlined.NextPlan
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_select
import com.divinelink.core.ui.resources.core_ui_share
import com.divinelink.core.ui.resources.move_progress_show_to
import com.divinelink.core.ui.resources.on_hold
import com.divinelink.core.ui.resources.plan_to_watch
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_action_remove_from_list
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_list_title

sealed interface ActionMenuSection {

  data object Divider : ActionMenuSection

  sealed class Intent(
    val icon: ImageVector,
    val textRes: UIText,
  ) : ActionMenuSection {
    data object Share : Intent(
      icon = Icons.Default.Share,
      textRes = UIText.ResourceText(UiString.core_ui_share),
    )

    data object MultiSelect : Intent(
      icon = Icons.Default.CheckCircleOutline,
      textRes = UIText.ResourceText(UiString.core_ui_select),
    )

    data object AddToList : Intent(
      icon = Icons.AutoMirrored.Default.PlaylistAdd,
      textRes = UIText.ResourceText(Res.string.feature_add_to_account_list_title),
    )

    data object RemoveFromList : Intent(
      icon = Icons.Default.PlaylistRemove,
      textRes = UIText.ResourceText(Res.string.feature_add_to_account_action_remove_from_list),
    )

    data object PauseShow : Intent(
      icon = Icons.Default.PauseCircleOutline,
      textRes = UIText.MultiResourceText(
        UiString.move_progress_show_to,
        arrayOf(UiString.on_hold),
      ),
    )

    data object PlanToWatch : Intent(
      icon = Icons.AutoMirrored.Outlined.NextPlan,
      textRes = UIText.MultiResourceText(
        UiString.move_progress_show_to,
        arrayOf(UiString.plan_to_watch),
      ),
    )
  }
}
