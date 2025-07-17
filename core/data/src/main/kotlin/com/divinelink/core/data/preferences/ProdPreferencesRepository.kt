package com.divinelink.core.data.preferences

import com.divinelink.core.datastore.ui.UiSettingsStorage
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection

class ProdPreferencesRepository(private val storage: UiSettingsStorage) : PreferencesRepository {
  override val uiPreferences
    get() = storage.uiPreferences

  override suspend fun setViewMode(
    section: ViewableSection,
    viewMode: ViewMode,
  ) {
    storage.updateViewMode(
      section = section,
      viewMode = viewMode,
    )
  }
}
