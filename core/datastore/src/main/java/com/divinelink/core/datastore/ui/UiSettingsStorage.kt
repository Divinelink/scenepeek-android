package com.divinelink.core.datastore.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.model.ui.SectionPreferences
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UiSettingsStorage {
  val uiPreferences: Flow<UiPreferences>

  suspend fun updateViewMode(
    section: ViewableSection,
    viewMode: ViewMode,
  )
}

class DatastoreUiStorage(private val dataStore: DataStore<Preferences>) : UiSettingsStorage {

  companion object {
    const val PREFS_NAME = "ui_preferences"
  }

  private object PreferencesKeys {
    fun viewModeKey(section: ViewableSection) = stringPreferencesKey("${section.key}_view_mode")
  }

  override val uiPreferences: Flow<UiPreferences> = dataStore.data.map { preferences ->
    val sections = ViewableSection.entries.associateWith { section ->
      SectionPreferences(
        viewMode = ViewMode.valueOf(
          preferences[PreferencesKeys.viewModeKey(section)] ?: ViewMode.LIST.name,
        ),
      )
    }

    UiPreferences(
      sections = sections,
    )
  }

  override suspend fun updateViewMode(
    section: ViewableSection,
    viewMode: ViewMode,
  ) {
    dataStore.edit { preferences ->
      preferences[PreferencesKeys.viewModeKey(section)] = viewMode.name
    }
  }
}
