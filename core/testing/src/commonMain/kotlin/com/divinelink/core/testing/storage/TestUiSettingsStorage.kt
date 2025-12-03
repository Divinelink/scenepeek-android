package com.divinelink.core.testing.storage

import com.divinelink.core.datastore.ui.UiSettingsStorage
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
}
