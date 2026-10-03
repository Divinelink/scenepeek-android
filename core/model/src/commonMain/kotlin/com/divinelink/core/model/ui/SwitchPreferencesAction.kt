package com.divinelink.core.model.ui

import com.divinelink.core.model.sort.SortBy

sealed interface SwitchPreferencesAction {
  data class SwitchViewMode(val section: ViewableSection) : SwitchPreferencesAction
  data class SwitchSortDirection(val section: SortableSection) : SwitchPreferencesAction
  data class SwitchSortBy(
    val section: SortableSection,
    val sortBy: SortBy,
  ) : SwitchPreferencesAction

  data class SwitchListSortBy(
    val listId: Int,
    val sortBy: SortBy,
  ) : SwitchPreferencesAction

  data class SwitchListSortDirection(val listId: Int) : SwitchPreferencesAction

  data class ClearSortOption(val listId: Int) : SwitchPreferencesAction

  data class UpdateExpandedState(val section: ExpandableSection) : SwitchPreferencesAction
}
