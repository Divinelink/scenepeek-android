package com.divinelink.core.model.ui

data class UiPreferences(
  val personCreditsViewMode: ViewMode,
  val listsViewMode: ViewMode,
  val userDataViewMode: ViewMode,
  val discoverViewMode: ViewMode,
) {
  companion object {
    val Initial = UiPreferences(
      personCreditsViewMode = ViewMode.LIST,
      listsViewMode = ViewMode.LIST,
      userDataViewMode = ViewMode.LIST,
      discoverViewMode = ViewMode.LIST,
    )
  }
}
