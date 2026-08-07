package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.ui.UiSettingsStorage
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.sort.other
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

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

    currentSortBy?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption +
          (section to option.copy(sortBy = option.sortBy)),
      )
    }
  }

  override suspend fun updateSortDirection(section: ViewableSection) {
    val currentSortBy = _uiPreferences.value.sortOption[section]

    currentSortBy?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        sortOption = _uiPreferences.value.sortOption +
          (section to option.copy(direction = option.direction.other())),
      )
    }
  }

  override suspend fun clearListSortOption(listId: Int) {
    val listSortOptions = _uiPreferences.value.listSortOptions[listId]

    listSortOptions?.let {
      _uiPreferences.value = _uiPreferences.value.copy(
        listSortOptions = _uiPreferences.value.listSortOptions - listId,
      )
    }
  }

  override fun listSortOption(id: Int): Flow<SortOption?> = uiPreferences
    .map { it.listSortOptions[id] }

  override suspend fun updateListSortBy(
    listId: Int,
    sortBy: SortBy,
  ) {
    _uiPreferences.value.listSortOptions[listId]?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        listSortOptions = _uiPreferences.value.listSortOptions +
          (listId to option.copy(sortBy = sortBy)),
      )
    }
  }

  override suspend fun updateListSortDirection(listId: Int) {
    _uiPreferences.value.listSortOptions[listId]?.let { option ->
      _uiPreferences.value = _uiPreferences.value.copy(
        listSortOptions = _uiPreferences.value.listSortOptions +
          (listId to option.copy(direction = option.direction.other())),
      )
    }
  }
}
