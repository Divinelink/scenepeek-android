package com.divinelink.core.model.ui

import com.divinelink.core.model.sort.SortBy

sealed interface SwitchPreferencesAction {
  data class SwitchViewMode(val section: ViewableSection) : SwitchPreferencesAction
  data class SwitchSortDirection(val section: ViewableSection) : SwitchPreferencesAction
  data class SwitchSortBy(
    val section: ViewableSection,
    val sortBy: SortBy,
  ) : SwitchPreferencesAction
}
