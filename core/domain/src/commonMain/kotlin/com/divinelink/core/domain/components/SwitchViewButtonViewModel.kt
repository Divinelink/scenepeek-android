package com.divinelink.core.domain.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.launch

class SwitchViewButtonViewModel(private val repository: PreferencesRepository) : ViewModel() {

  fun onAction(action: SwitchPreferencesAction) {
    when (action) {
      is SwitchPreferencesAction.SwitchSortBy -> switchSortBy(action.section, action.sortBy)
      is SwitchPreferencesAction.SwitchSortDirection -> switchSortDirection(action.section)
      is SwitchPreferencesAction.SwitchViewMode -> switchViewMode(action.section)
      is SwitchPreferencesAction.SwitchListSortBy -> switchListSortBy(action)
      is SwitchPreferencesAction.SwitchListSortDirection -> switchListSortDirection(action.listId)
      is SwitchPreferencesAction.ClearSortOption -> clearListSortOption(action.listId)
      is SwitchPreferencesAction.UpdateExpandedState -> switchExpandedState(action.section)
    }
  }

  private fun switchViewMode(section: ViewableSection) {
    viewModelScope.launch {
      repository.switchViewMode(section)
    }
  }

  private fun switchSortDirection(section: SortableSection) {
    viewModelScope.launch {
      repository.switchSortDirection(section)
    }
  }

  private fun switchSortBy(
    section: SortableSection,
    sortBy: SortBy,
  ) {
    viewModelScope.launch {
      repository.switchSortBy(section, sortBy)
    }
  }

  private fun switchListSortBy(action: SwitchPreferencesAction.SwitchListSortBy) {
    viewModelScope.launch {
      repository.switchListSortBy(action.listId, action.sortBy)
    }
  }

  private fun switchListSortDirection(listId: Int) {
    viewModelScope.launch {
      repository.switchListSortDirection(listId)
    }
  }

  private fun clearListSortOption(listId: Int) {
    viewModelScope.launch {
      repository.clearListSortOption(listId)
    }
  }

  private fun switchExpandedState(section: ExpandableSection) {
    viewModelScope.launch {
      repository.switchExpandedState(section)
    }
  }
}
