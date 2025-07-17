package com.divinelink.core.model.ui

data class UiPreferences(val sections: Map<ViewableSection, SectionPreferences>) {
  companion object {
    val Initial = UiPreferences(
      sections = ViewableSection.entries.associateWith { SectionPreferences(ViewMode.GRID) },
    )
  }
}
