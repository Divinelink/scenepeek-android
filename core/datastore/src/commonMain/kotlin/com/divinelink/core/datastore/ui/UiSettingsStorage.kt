package com.divinelink.core.datastore.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.availableListsKey
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.listSortBy
import com.divinelink.core.datastore.ui.DatastoreUiStorage.PreferencesKeys.listSortDirection
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

interface UiSettingsStorage {
  val uiPreferences: Flow<UiPreferences>

  fun listSortOption(id: Int): Flow<SortOption?>

  suspend fun clearListSortOption(listId: Int)

  suspend fun updateViewMode(section: ViewableSection)

  suspend fun updateSortBy(
    section: ViewableSection,
    sortBy: SortBy,
  )

  suspend fun updateSortDirection(section: ViewableSection)

  suspend fun updateListSortBy(listId: Int, sortBy: SortBy)

  suspend fun updateListSortDirection(listId: Int)
}

class DatastoreUiStorage(
  private val dataStore: DataStore<Preferences>,
) : UiSettingsStorage {

  companion object {
    const val PREFS_NAME = "ui_preferences"
  }

  private object PreferencesKeys {
    fun viewModeKey(section: ViewableSection) = stringPreferencesKey("${section.key}_view_mode")
    fun sortByKey(section: ViewableSection) = stringPreferencesKey("$${section.key}_sort_by")
    fun sortDirectionKey(section: ViewableSection) = stringPreferencesKey(
      "${section.key}_sort_direction",
    )

    fun listSortBy(listId: Int) = stringPreferencesKey("${listId}_sort_by")
    fun listSortDirection(listId: Int) = stringPreferencesKey("${listId}_sort_direction")
    val availableListsKey = stringSetPreferencesKey("available_lists")
  }

  override val uiPreferences: Flow<UiPreferences> = dataStore.data.map {
    UiPreferences(
      viewModes = ViewableSection.entries.associateWith { section ->
        ViewMode.from(it[viewModeKey(section)])
      },
      sortOption = mapOf(
        ViewableSection.DISCOVER_MOVIES to SortOption(
          sortBy = SortBy.findDiscoverMovieOption(it[sortByKey(ViewableSection.DISCOVER_MOVIES)]),
          direction = SortDirection.from(it[sortDirectionKey(ViewableSection.DISCOVER_MOVIES)]),
        ),
        ViewableSection.DISCOVER_SHOWS to SortOption(
          sortBy = SortBy.findDiscoverShowOption(it[sortByKey(ViewableSection.DISCOVER_SHOWS)]),
          direction = SortDirection.from(it[sortDirectionKey(ViewableSection.DISCOVER_SHOWS)]),
        ),
        ViewableSection.USER_DATA to SortOption(
          sortBy = SortBy.findUserDataOption(it[sortByKey(ViewableSection.USER_DATA)]),
          direction = SortDirection.from(it[sortDirectionKey(ViewableSection.USER_DATA)]),
        ),
      ),
      listSortOptions = (it[availableListsKey]?.map { key -> key.toInt() } ?: emptyList())
        .associateWith { listId -> listSortOption(listId).firstOrNull() },
    )
  }.distinctUntilChanged()

  override fun listSortOption(id: Int): Flow<SortOption?> = dataStore.data
    .map { prefs ->
      val sortBy = SortBy.findListSortOption(prefs[listSortBy(id)]) ?: return@map null
      val direction = SortDirection.from(prefs[listSortDirection(id)])
      SortOption(sortBy = sortBy, direction = direction)
    }
    .distinctUntilChanged()

  override suspend fun updateViewMode(section: ViewableSection) {
    dataStore.edit { preferences ->
      val currentViewMode = ViewMode.from(preferences[viewModeKey(section)])

      if (section == ViewableSection.DISCOVER_MOVIES || section == ViewableSection.DISCOVER_SHOWS) {
        preferences[viewModeKey(ViewableSection.DISCOVER_SHOWS)] = currentViewMode.other().value
        preferences[viewModeKey(ViewableSection.DISCOVER_MOVIES)] = currentViewMode.other().value
      } else {
        preferences[viewModeKey(section)] = currentViewMode.other().value
      }
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

  override suspend fun updateListSortBy(listId: Int, sortBy: SortBy) {
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
      preferences.remove(listSortBy(listId))
    }
  }

  private fun MutablePreferences.addToAvailableLists(listId: Int) {
    val currentLists = this[availableListsKey].orEmpty()
    this[availableListsKey] = currentLists + listId.toString()
  }

  private fun MutablePreferences.removeFromAvailableLists(listId: Int) {
    val currentLists = this[availableListsKey].orEmpty()

    this[availableListsKey] = currentLists - listId.toString()
  }
}
