package com.divinelink.core.ui.local

import androidx.compose.runtime.staticCompositionLocalOf
import com.divinelink.core.model.ui.UiPreferences

val LocalUiPreferences = staticCompositionLocalOf<UiPreferences> {
  error("UiPreferences not provided")
}
