package com.divinelink.core.data.preferences

import com.divinelink.core.datastore.ui.UiSettingsStorage
import com.divinelink.core.model.ui.ViewableSection

class ProdPreferencesRepository(private val storage: UiSettingsStorage) : PreferencesRepository {
  override val uiPreferences
    get() = storage.uiPreferences

  override suspend fun switchViewMode(section: ViewableSection) {
    storage.updateViewMode(
      section = section,
    )
  }
}
