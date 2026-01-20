package com.divinelink.core.model.ui

import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption

data class UiPreferences(
  val viewModes: Map<ViewableSection, ViewMode>,
  val sortOption: Map<ViewableSection, SortOption>,
) {
  companion object {
    val Initial = UiPreferences(
      viewModes = ViewableSection.entries.associateWith { section ->
        when (section) {
          ViewableSection.LISTS -> ViewMode.LIST
          ViewableSection.PERSON_CREDITS -> ViewMode.GRID
          ViewableSection.DISCOVER_MOVIES -> ViewMode.GRID
          ViewableSection.DISCOVER_SHOWS -> ViewMode.GRID
          ViewableSection.USER_DATA -> ViewMode.GRID
          ViewableSection.MEDIA_DETAILS -> ViewMode.GRID
          ViewableSection.LIST_DETAILS -> ViewMode.GRID
          ViewableSection.SEARCH -> ViewMode.GRID
        }
      },
      sortOption = mapOf(
        ViewableSection.DISCOVER_MOVIES to SortOption(
          SortBy.POPULARITY,
          SortDirection.DESC,
        ),
        ViewableSection.DISCOVER_SHOWS to SortOption(
          SortBy.POPULARITY,
          SortDirection.DESC,
        ),
      ),
    )
  }
}
