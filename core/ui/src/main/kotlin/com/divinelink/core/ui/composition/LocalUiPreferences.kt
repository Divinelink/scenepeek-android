package com.divinelink.core.ui.composition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection

val LocalUiPreferences = compositionLocalOf<UiPreferences> {
  error("UiPreferences not provided")
}

@Composable
fun rememberUiPreferences(): UiPreferences = LocalUiPreferences.current

@Composable
fun rememberViewModePreferences(section: ViewableSection): ViewMode {
  val uiPreferences = rememberUiPreferences()

  return uiPreferences.viewModes[section] ?: ViewMode.LIST
}
