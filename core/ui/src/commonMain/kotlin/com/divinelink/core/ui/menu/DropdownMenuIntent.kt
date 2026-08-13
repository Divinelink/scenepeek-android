package com.divinelink.core.ui.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import com.divinelink.core.model.UIText
import com.divinelink.core.ui.IconWrapper
import com.divinelink.core.ui.UiDrawable
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.core_ui_eye
import com.divinelink.core.ui.resources.core_ui_eye_off
import com.divinelink.core.ui.resources.core_ui_hide_total_episodes_item
import com.divinelink.core.ui.resources.core_ui_open_in
import com.divinelink.core.ui.resources.core_ui_share
import com.divinelink.core.ui.resources.core_ui_show_total_episodes_item
import com.divinelink.core.ui.resources.ic_wikipedia

sealed class DropdownMenuIntent(
  open val icon: IconWrapper,
  open val textRes: UIText,
) {
  data object Share : DropdownMenuIntent(
    icon = IconWrapper.Vector(Icons.Default.Share),
    textRes = UIText.ResourceText(UiString.core_ui_share),
  )

  data class OpenInWikipedia(val title: String) : DropdownMenuIntent(
    icon = IconWrapper.Image(UiDrawable.ic_wikipedia),
    textRes = UIText.ResourceText(UiString.core_ui_open_in, "Wikipedia"),
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
      UIText.ResourceText(UiString.core_ui_show_total_episodes_item)
    } else {
      UIText.ResourceText(UiString.core_ui_hide_total_episodes_item)
    },
  )
}
