package com.divinelink.core.model.ui

data class UiPreferences(val viewModes: Map<ViewableSection, ViewMode>) {
  companion object {
    val Initial = UiPreferences(
      viewModes = ViewableSection.entries.associateWith { ViewMode.LIST },
    )
  }
}
