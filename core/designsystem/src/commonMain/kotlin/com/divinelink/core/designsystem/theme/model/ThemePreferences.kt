package com.divinelink.core.designsystem.theme.model

import com.divinelink.core.designsystem.theme.seedLong

data class ThemePreferences(
  val theme: Theme,
  val colorSystem: ColorSystem,
  val themeColor: Long,
  val isPureBlack: Boolean,
) {
  companion object {
    val initial = ThemePreferences(
      theme = Theme.SYSTEM,
      colorSystem = ColorSystem.Dynamic,
      themeColor = seedLong,
      isPureBlack = false,
    )
  }
}
