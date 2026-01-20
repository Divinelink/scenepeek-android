package com.divinelink.core.model.ui

import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption

data class UiPreferences(
  val viewModes: Map<ViewableSection, ViewMode>,
  val sortBy: Map<ViewableSection, SortOption>,
) {
  companion object {
    val Initial = UiPreferences(
      viewModes = ViewableSection.entries.associateWith { section ->
        when (section) {
          ViewableSection.LISTS -> ViewMode.LIST
          ViewableSection.PERSON_CREDITS -> ViewMode.LIST
          ViewableSection.DISCOVER_MOVIES -> ViewMode.LIST
          ViewableSection.DISCOVER_SHOWS -> ViewMode.LIST
          ViewableSection.USER_DATA -> ViewMode.LIST
          ViewableSection.MEDIA_DETAILS -> ViewMode.LIST
          ViewableSection.LIST_DETAILS -> ViewMode.LIST
          ViewableSection.SEARCH -> ViewMode.GRID
        }
      },
      sortBy = mapOf(
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
