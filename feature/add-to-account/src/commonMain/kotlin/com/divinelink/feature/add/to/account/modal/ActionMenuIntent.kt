package com.divinelink.feature.add.to.account.modal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_select
import com.divinelink.core.ui.resources.core_ui_share
import com.divinelink.feature.add.to.account.resources.Res
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_action_remove_from_list
import com.divinelink.feature.add.to.account.resources.feature_add_to_account_list_title
import org.jetbrains.compose.resources.StringResource

enum class ActionMenuIntent(
  val icon: ImageVector,
  val textRes: StringResource,
) {
  Share(
    icon = Icons.Default.Share,
    textRes = UiString.core_ui_share,
  ),
  MultiSelect(
    icon = Icons.Default.CheckCircleOutline,
    textRes = UiString.core_ui_select,
  ),
  AddToList(
    icon = Icons.AutoMirrored.Default.PlaylistAdd,
    textRes = Res.string.feature_add_to_account_list_title,
  ),
  RemoveFromList(
    icon = Icons.Default.PlaylistRemove,
    textRes = Res.string.feature_add_to_account_action_remove_from_list,
  ),
}
