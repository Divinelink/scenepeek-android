package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.ui.UiSettingsStorage
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.other
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestUiSettingsStorage(uiPreferences: UiPreferences = UiPreferences.Initial) :
  UiSettingsStorage {

  private val _uiPreferences = MutableStateFlow(uiPreferences)
  override val uiPreferences: Flow<UiPreferences> = _uiPreferences

  override suspend fun updateViewMode(section: ViewableSection) {
    val currentViewMode = _uiPreferences.value.viewModes[section]

    currentViewMode?.let { viewMode ->
      _uiPreferences.value = _uiPreferences.value.copy(
        viewModes = _uiPreferences.value.viewModes + (section to viewMode.other()),
      )
    }
  }

  override suspend fun updateSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  ) {
    val currentSortBy = _uiPreferences.value.sortOption[section]

    currentSortBy?.let { sortOption ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption + (
          section to sortOption.copy(sortBy = sortOption.sortBy)
          ),
      )
    }
  }

  override suspend fun updateSortDirection(section: ViewableSection) {
    val currentSortBy = _uiPreferences.value.sortOption[section]

    currentSortBy?.let { sortBy ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption + (
          section to sortBy.copy(direction = sortBy.direction.other())
          ),
      )
    }
  }
}
