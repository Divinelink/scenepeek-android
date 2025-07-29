package com.divinelink.feature.add.to.account.modal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.PlaylistAdd
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.PlaylistRemove
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.feature.add.to.account.R
import com.divinelink.core.ui.R as uiR

enum class ActionMenuIntent(
  val icon: ImageVector,
  val textRes: Int,
) {
  Share(
    icon = Icons.Default.Share,
    textRes = uiR.string.core_ui_share,
  ),
  MultiSelect(
    icon = Icons.Default.CheckCircleOutline,
    textRes = uiR.string.core_ui_select,
  ),
  AddToList(
    icon = Icons.AutoMirrored.Default.PlaylistAdd,
    textRes = R.string.feature_add_to_account_list_title,
  ),
  RemoveFromList(
    icon = Icons.Default.PlaylistRemove,
    textRes = R.string.feature_add_to_account_action_remove_from_list,
  ),
}
