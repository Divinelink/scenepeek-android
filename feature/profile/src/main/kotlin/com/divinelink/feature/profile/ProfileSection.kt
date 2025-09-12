package com.divinelink.feature.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.ui.graphics.vector.ImageVector
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.UiString

sealed class ProfileSection(
  val title: UIText,
  val icon: ImageVector,
) {
  data object Watchlist : ProfileSection(
    title = UIText.ResourceText(UiString.core_ui_section_watchlist),
    icon = Icons.Rounded.Bookmarks,
  )

  data object Lists : ProfileSection(
    title = UIText.ResourceText(UiString.core_ui_section_lists),
    icon = Icons.AutoMirrored.Rounded.List,
  )

  data object Ratings : ProfileSection(
    title = UIText.ResourceText(UiString.core_ui_section_ratings),
    icon = Icons.Rounded.StarRate,
  )

  data object Requests : ProfileSection(
    title = UIText.ResourceText(UiString.core_ui_requests),
    icon = Icons.Rounded.AccessTime,
  )
}
