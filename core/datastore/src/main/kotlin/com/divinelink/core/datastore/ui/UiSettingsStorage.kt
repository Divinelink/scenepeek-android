package com.divinelink.core.datastore.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.viewModeKey
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UiSettingsStorage {
  val uiPreferences: Flow<UiPreferences>

  suspend fun updateViewMode(section: ViewableSection)
}

class DatastoreUiStorage(private val dataStore: DataStore<Preferences>) : UiSettingsStorage {

  companion object {
    const val PREFS_NAME = "ui_preferences"
  }

  private object PreferencesKeys {
    fun viewModeKey(section: ViewableSection) = stringPreferencesKey("${section.key}_view_mode")
  }

  override val uiPreferences: Flow<UiPreferences> = dataStore.data.map {
    UiPreferences(
      personCreditsViewMode = ViewMode.from(it[viewModeKey(ViewableSection.PERSON_CREDITS)]),
      listsViewMode = ViewMode.from(it[viewModeKey(ViewableSection.LISTS)]),
    )
  }

  override suspend fun updateViewMode(section: ViewableSection) {
    dataStore.edit { preferences ->
      val currentViewMode = when (section) {
        ViewableSection.PERSON_CREDITS -> preferences[viewModeKey(ViewableSection.PERSON_CREDITS)]
        ViewableSection.LISTS -> preferences[viewModeKey(ViewableSection.LISTS)]
      }

      preferences[viewModeKey(section)] = ViewMode.from(currentViewMode).other().value
    }
  }
}
