package com.divinelink.core.ui.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_eye
import com.divinelink.core.ui.resources.core_ui_eye_off
import com.divinelink.core.ui.resources.core_ui_hide_total_episodes_item
import com.divinelink.core.ui.resources.core_ui_share
import com.divinelink.core.ui.resources.core_ui_show_total_episodes_item
import org.jetbrains.compose.resources.StringResource

sealed class DropdownMenuIntent(
  open val icon: IconWrapper,
  open val textRes: StringResource,
) {
  data object Share : DropdownMenuIntent(
    icon = IconWrapper.Vector(Icons.Default.Share),
    textRes = UiString.core_ui_share,
  )

  data class ShowOrHideSpoilers(
    val obfuscated: Boolean,
  ) : DropdownMenuIntent(
    icon = if (obfuscated) {
      IconWrapper.Image(UiDrawable.core_ui_eye)
    } else {
      IconWrapper.Image(UiDrawable.core_ui_eye_off)
    },
    textRes = if (obfuscated) {
      UiString.core_ui_show_total_episodes_item
    } else {
      UiString.core_ui_hide_total_episodes_item
    },
  )
}
