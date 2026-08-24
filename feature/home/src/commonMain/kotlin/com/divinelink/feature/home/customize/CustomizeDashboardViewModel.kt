package com.divinelink.feature.home.customize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divinelink.core.data.auth.AuthRepository
import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.DisplayMessage
import com.divinelink.core.model.UIText
import com.divinelink.core.model.home.HomeSectionInfo
import com.divinelink.core.model.home.MediaListSection
import com.divinelink.core.ui.UiString
import com.divinelink.core.ui.resources.error_at_least_one_section_visible
import com.divinelink.feature.home.buildSections
import com.divinelink.feature.home.withFeatureFlags
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomizeDashboardViewModel(
  private val preferencesRepository: PreferencesRepository,
  authRepository: AuthRepository,
) : ViewModel() {

  private val _uiState: MutableStateFlow<CustomizeDashboardUiState> = MutableStateFlow(
    CustomizeDashboardUiState.initial,
  )
  val uiState: StateFlow<CustomizeDashboardUiState> = _uiState

  init {
    preferencesRepository
      .homeSections
      .withFeatureFlags(seerrEnabled = authRepository.seerrEnabled)
      .distinctUntilChanged()
      .onEach { preferences ->
        _uiState.update { state ->
          val sections = preferences.order.buildSections()
          state.copy(
            sections = sections,
            hiddenSections = preferences.hidden.toList().buildSections().toSet(),
          )
        }
      }
      .launchIn(viewModelScope)
  }

  fun onAction(action: CustomizeDashboardAction) {
    when (action) {
      CustomizeDashboardAction.ConsumeMessage -> _uiState.update { it.copy(displayMessage = null) }
      is CustomizeDashboardAction.ReorderSections -> updateSectionOrder(action.newOrder)
      is CustomizeDashboardAction.ToggleVisibility -> onToggleSectionVisibility(action.section)
    }
  }

  private fun updateSectionOrder(order: List<HomeSectionInfo>) {
    viewModelScope.launch {
      val sections = order.map { it.section }

      val currentConfig = preferencesRepository.homeSections.first()
      val originalFullOrder = currentConfig.order

      val finalOrder = mutableListOf<MediaListSection.Home>()

      val newVisibleQueue = ArrayDeque(sections)

      for (section in originalFullOrder) {
        if (section in sections) {
          if (newVisibleQueue.isNotEmpty()) {
            finalOrder.add(newVisibleQueue.removeFirst())
          }
        } else {
          finalOrder.add(section)
        }
      }

      finalOrder.addAll(newVisibleQueue)

      preferencesRepository.updateHomeSectionsOrder(finalOrder)
    }
  }

  private fun onToggleSectionVisibility(section: HomeSectionInfo) {
    viewModelScope.launch {
      val hiddenSections = uiState.value.hiddenSections
      val currentSections = uiState.value.sections

      if (currentSections.size - hiddenSections.size == 1 && section !in hiddenSections) {
        _uiState.update { state ->
          state.copy(
            displayMessage = DisplayMessage.Error(
              message = UIText.ResourceText(UiString.error_at_least_one_section_visible),
            ),
          )
        }
      } else {
        preferencesRepository.toggleHomeSectionVisibility(section.section)
      }
    }
  }
}

