package com.divinelink.core.designsystem

import com.divinelink.core.designsystem.theme.model.ColorSystem
import com.divinelink.core.designsystem.theme.model.Theme
import com.divinelink.core.designsystem.theme.model.ThemePreferences
import com.divinelink.core.designsystem.theme.seedLong
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ThemePreferencesTest {

  @Test
  fun `test initial values of theme preferences`() {
    ThemePreferences.initial shouldBe ThemePreferences(
      theme = Theme.SYSTEM,
      colorSystem = ColorSystem.Dynamic,
      themeColor = seedLong,
      isPureBlack = false,
    )
  }
}
