package com.divinelink.core.datastore.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.availableListsKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.discoverViewModeKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.expandedSection
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.listSortBy
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.listSortDirection
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.sortByKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.sortDirectionKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.viewModeKey
import com.divinelink.core.model.sort.SortBy
import com.divinelink.core.model.sort.SortContext
import com.divinelink.core.model.sort.SortDirection
import com.divinelink.core.model.sort.SortOption
import com.divinelink.core.model.sort.other
import com.divinelink.core.model.ui.ExpandableSection
import com.divinelink.core.model.ui.SortableSection
import com.divinelink.core.model.ui.UiPreferences
import com.divinelink.core.model.ui.ViewMode
import com.divinelink.core.model.ui.ViewableSection
import com.divinelink.core.model.ui.other
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface UiSettingsStorage {
  val uiPreferences: Flow<UiPreferences>

  fun listSortOption(id: Int): Flow<SortOption?>

  suspend fun clearListSortOption(listId: Int)

  suspend fun updateViewMode(section: ViewableSection)

  suspend fun updateSortBy(
    section: SortableSection,
    sortBy: SortBy,
  )

  suspend fun updateSortDirection(section: SortableSection)

  suspend fun updateListSortBy(
    listId: Int,
    sortBy: SortBy,
  )

  suspend fun updateListSortDirection(listId: Int)

  suspend fun expandSection(section: ExpandableSection)
}

class DatastoreUiStorage(
  private val dataStore: DataStore<Preferences>,
) : UiSettingsStorage {

  companion object {
    const val PREFS_NAME = "ui_preferences"
  }

  private object PreferencesKeys {
    val discoverViewModeKey = stringPreferencesKey("discover_view_mode")
    fun viewModeKey(section: ViewableSection) = stringPreferencesKey("${section.key}_view_mode")
    fun sortByKey(section: SortableSection) = stringPreferencesKey("${section.key}_sort_by")
    fun sortDirectionKey(section: SortableSection) = stringPreferencesKey(
      "${section.key}_sort_direction",
    )
    fun expandedSection(section: ExpandableSection) = booleanPreferencesKey(
      "${section.key}_expanded",
    )

    fun listSortBy(listId: Int) = stringPreferencesKey("${listId}_sort_by")
    fun listSortDirection(listId: Int) = stringPreferencesKey("${listId}_sort_direction")
    val availableListsKey = stringSetPreferencesKey("available_lists")
  }

  override val uiPreferences: Flow<UiPreferences> = dataStore.data.map { preferences ->
    UiPreferences(
      viewModes = ViewableSection.allSections.associateWith { section ->
        if (section is SortableSection.Discover) {
          ViewMode.from(preferences[discoverViewModeKey])
        } else {
          ViewMode.from(preferences[viewModeKey(section)])
        }
      },
      sortOption = SortableSection.all.associateWith { section ->
        val default = SortOption.default(section.sortContext)
        val sortBy = SortBy.from(preferences[sortByKey(section)])
        val direction = SortDirection.from(preferences[sortDirectionKey(section)])

        SortOption(
          sortBy = sortBy ?: default.sortBy,
          direction = direction,
        )
      },
      listSortOptions = (preferences[availableListsKey]?.map { key -> key.toInt() } ?: emptyList())
        .associateWith { listId -> listSortOption(listId).firstOrNull() },
      expanded = ExpandableSection.entries.associateWith { section ->
        preferences[expandedSection(section)] == true
      },
    )
  }.distinctUntilChanged()

  override fun listSortOption(id: Int): Flow<SortOption?> = dataStore.data
    .map { prefs ->
      val sortBy = SortContext.ListDetails.find(prefs[listSortBy(id)])
      val direction = SortDirection.from(prefs[listSortDirection(id)])
      SortOption(sortBy = sortBy, direction = direction)
    }
    .distinctUntilChanged()

  override suspend fun updateViewMode(section: ViewableSection) {
    dataStore.edit { preferences ->
      val key = if (section is SortableSection.Discover) {
        discoverViewModeKey
      } else {
        viewModeKey(section)
      }

      val currentViewMode = ViewMode.from(preferences[key])

      preferences[key] = currentViewMode.other().value
    }
  }

  override suspend fun updateSortBy(
    section: SortableSection,
    sortBy: SortBy,
  ) {
    dataStore.edit { preferences -> preferences[sortByKey(section)] = sortBy.value }
  }

  override suspend fun updateSortDirection(section: SortableSection) {
    dataStore.edit { preferences ->
      val currentDirection = SortDirection.from(preferences[sortDirectionKey(section)])

      preferences[sortDirectionKey(section)] = currentDirection.other().value
    }
  }

  override suspend fun updateListSortBy(
    listId: Int,
    sortBy: SortBy,
  ) {
    dataStore.edit { preferences ->
      preferences.addToAvailableLists(listId)
      preferences[listSortBy(listId)] = sortBy.value
    }
  }

  override suspend fun updateListSortDirection(listId: Int) {
    dataStore.edit { preferences ->
      preferences.addToAvailableLists(listId)

      val currentDirection = SortDirection.from(preferences[listSortDirection(listId)])
      preferences[listSortDirection(listId)] = currentDirection.other().value
    }
  }

  override suspend fun clearListSortOption(listId: Int) {
    dataStore.edit { preferences ->
      preferences.removeFromAvailableLists(listId)
      preferences.remove(listSortBy(listId))
      preferences.remove(listSortDirection(listId))
    }
  }

  override suspend fun expandSection(section: ExpandableSection) {
    dataStore.edit { preferences ->
      val expanded = preferences[expandedSection(section)] == true

      preferences[expandedSection(section)] = !expanded
    }
  }

  /**
   * Helper functions
   */
  private fun MutablePreferences.addToAvailableLists(listId: Int) {
    val currentLists = this[availableListsKey].orEmpty()
    this[availableListsKey] = currentLists + listId.toString()
  }

  private fun MutablePreferences.removeFromAvailableLists(listId: Int) {
    val currentLists = this[availableListsKey].orEmpty()

    this[availableListsKey] = currentLists - listId.toString()
  }
}
