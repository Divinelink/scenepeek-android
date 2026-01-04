package com.divinelink.core.model.ui

data class UiPreferences(val viewModes: Map<ViewableSection, ViewMode>) {
  companion object {
    val Initial = UiPreferences(
      viewModes = ViewableSection.entries.associateWith { section ->
        when (section) {
          ViewableSection.LISTS -> ViewMode.LIST
          ViewableSection.PERSON_CREDITS -> ViewMode.LIST
          ViewableSection.DISCOVER -> ViewMode.LIST
          ViewableSection.USER_DATA -> ViewMode.LIST
          ViewableSection.MEDIA_DETAILS -> ViewMode.LIST
          ViewableSection.LIST_DETAILS -> ViewMode.LIST
          ViewableSection.SEARCH -> ViewMode.GRID
        }
      },
    )
  }
}
