package com.divinelink.core.datastore.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.sortByKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.sortDirectionKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.viewModeKey
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.sort.other
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UiSettingsStorage {
  val uiPreferences: Flow<UiPreferences>

  suspend fun updateViewMode(section: ViewableSection)

  suspend fun updateSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  )
  suspend fun updateSortDirection(section: ViewableSection)
}

class DatastoreUiStorage(private val dataStore: DataStore<Preferences>) : UiSettingsStorage {

  companion object {
    const val PREFS_NAME = "ui_preferences"
  }

  private object PreferencesKeys {
    fun viewModeKey(section: ViewableSection) = stringPreferencesKey("${section.key}_view_mode")
    fun sortByKey(section: ViewableSection) = stringPreferencesKey("$${section.key}_sort_by")
    fun sortDirectionKey(section: ViewableSection) = stringPreferencesKey(
      "${section.key}_sort_direction",
    )
  }

  override val uiPreferences: Flow<UiPreferences> = dataStore.data.map {
    UiPreferences(
      viewModes = ViewableSection.entries.associateWith { section ->
        ViewMode.from(it[viewModeKey(section)])
      },
      sortBy = mapOf(
        ViewableSection.DISCOVER_MOVIES to SortOption(
          sortBy = SortBy.findDiscoverMovieOption(it[sortByKey(ViewableSection.DISCOVER_MOVIES)]),
          direction = SortDirection.from(it[sortDirectionKey(ViewableSection.DISCOVER_MOVIES)]),
        ),
        ViewableSection.DISCOVER_SHOWS to SortOption(
          sortBy = SortBy.findDiscoverShowOption(it[sortByKey(ViewableSection.DISCOVER_SHOWS)]),
          direction = SortDirection.from(it[sortDirectionKey(ViewableSection.DISCOVER_SHOWS)]),
        ),
      ),
    )
  }

  override suspend fun updateViewMode(section: ViewableSection) {
    dataStore.edit { preferences ->
      val currentViewMode = preferences[viewModeKey(section)]

      preferences[viewModeKey(section)] = ViewMode.from(currentViewMode).other().value
    }
  }

  override suspend fun updateSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  ) {
    dataStore.edit { preferences -> preferences[sortByKey(section)] = sortBy.value }
  }

  override suspend fun updateSortDirection(section: ViewableSection) {
    dataStore.edit { preferences ->
      val currentDirection = SortDirection.from(preferences[sortDirectionKey(section)])

      preferences[sortDirectionKey(section)] = currentDirection.other().value
    }
  }
}
