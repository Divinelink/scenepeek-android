package com.divinelink.core.domain.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.launch

class SwitchViewButtonViewModel(private val repository: PreferencesRepository) : ViewModel() {

  fun switchViewMode(section: ViewableSection) {
    viewModelScope.launch {
      repository.switchViewMode(section)
    }
  }
}
