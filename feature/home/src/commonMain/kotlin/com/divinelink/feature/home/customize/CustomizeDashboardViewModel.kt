package com.divinelink.feature.home.customize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.feature.home.buildSections
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomizeDashboardViewModel(
  private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<CustomizeDashboardUiState> = MutableStateFlow(
    CustomizeDashboardUiState.initial,
  )
  val uiState: StateFlow<CustomizeDashboardUiState> = _uiState

  init {
    preferencesRepository
      .homeSections
      .distinctUntilChanged()
      .onEach { preferences ->
        _uiState.update { state ->
          val sections = preferences.homeSectionsDisplayOrder.buildSections()
          state.copy(
            sections = sections,
            hiddenSections = preferences.hiddenHomeSections.toList().buildSections().toSet(),
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun updateSectionOrder(
    sections: List<HomeSectionInfo>,
  ) {
    viewModelScope.launch {
      preferencesRepository.updateSectionsOrder(sections.map { it.section })
    }
  }

  fun onToggleSectionVisibility(section: MediaListSection.Home) {
    viewModelScope.launch {
      preferencesRepository.toggleHomeSectionVisibility(section)
    }
  }
}

