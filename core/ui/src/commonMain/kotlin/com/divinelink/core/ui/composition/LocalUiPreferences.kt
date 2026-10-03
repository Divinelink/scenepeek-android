package com.divinelink.core.ui.composition

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.SortableSection
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

@Composable
fun rememberSortBy(section: SortableSection): SortOption {
  val uiPreferences = rememberUiPreferences()

  return uiPreferences.sortOption[section] ?: SortOption(
    sortBy = SortBy.Popularity,
    direction = SortDirection.DESC,
  )
}

@Composable
fun rememberListSortOption(listId: Int): SortOption? {
  val uiPreferences = rememberUiPreferences()

  return uiPreferences.listSortOptions[listId]
}

@Composable
fun rememberExpandState(section: ExpandableSection): Boolean {
  val uiPreferences = rememberUiPreferences()

  return uiPreferences.expanded[section] == true
}
