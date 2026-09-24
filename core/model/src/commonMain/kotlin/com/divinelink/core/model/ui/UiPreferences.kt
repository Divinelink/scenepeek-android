package com.divinelink.core.model.ui

import com.divinelink.core.model.sort.SortOption

data class UiPreferences(
  val viewModes: Map<ViewableSection, ViewMode>,
  val sortOption: Map<SortableSection, SortOption>,
  val listSortOptions: Map<Int, SortOption?>,
  val expanded: Map<ExpandableSection, Boolean>,
) {
  companion object {
    val Initial = UiPreferences(
      viewModes = ViewableSection.allSections.associateWith { ViewMode.GRID },
      sortOption = SortableSection.all.associateWith { section ->
        SortOption.default(section.sortContext)
      },
      listSortOptions = emptyMap(),
      expanded = ExpandableSection.entries.associateWith { section ->
        when (section) {
          ExpandableSection.ON_HOLD_SHOWS -> false
        }
      },
    )
  }
}
