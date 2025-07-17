package com.divinelink.core.fixtures.data.preferences

import com.divinelink.core.data.preferences.PreferencesRepository
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestPreferencesRepository(uiPreferences: UiPreferences = UiPreferences.Initial) :
  PreferencesRepository {

  private val uiPreferencesFlow = MutableStateFlow(uiPreferences)

  override val uiPreferences: Flow<UiPreferences> = uiPreferencesFlow

  override suspend fun setViewMode(
    section: ViewableSection,
    viewMode: ViewMode,
  ) {
    val currentPreferences = uiPreferencesFlow.value
    val updatedSections = currentPreferences.sections.toMutableMap().apply {
      this[section] = this[section]?.copy(viewMode = viewMode) ?: error("Section not found")
    }
    uiPreferencesFlow.value = UiPreferences(sections = updatedSections)
  }
}
