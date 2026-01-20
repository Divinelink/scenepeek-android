package com.divinelink.core.domain.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.ui.SwitchPreferencesAction
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.launch

class SwitchViewButtonViewModel(private val repository: PreferencesRepository) : ViewModel() {

  fun onAction(action: SwitchPreferencesAction) {
    when (action) {
      is SwitchPreferencesAction.SwitchSortBy -> switchSortBy(action.section)
      is SwitchPreferencesAction.SwitchSortDirection -> switchSortDirection(action.section)
      is SwitchPreferencesAction.SwitchViewMode -> switchViewMode(action.section)
    }
  }

  private fun switchViewMode(section: ViewableSection) {
    viewModelScope.launch {
      repository.switchViewMode(section)
    }
  }

  private fun switchSortDirection(section: ViewableSection) {
    viewModelScope.launch {
      repository.switchSortDirection(section)
    }
  }

  private fun switchSortBy(section: ViewableSection) {
    viewModelScope.launch {
    }
  }
}
