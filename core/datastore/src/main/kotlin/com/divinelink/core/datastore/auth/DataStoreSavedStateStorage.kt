package com.divinelink.core.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.commons.util.JsonHelper
import com.divinelink.core.datastore.auth.SavedState.JellyseerrAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

val InitialSavedState: SavedState = ConcreteSavedState()

class DataStoreSavedStateStorage(
  private val dataStore: DataStore<Preferences>,
  private val json: JsonHelper,
  scope: CoroutineScope,
) : SavedStateStorage {

  override val savedState: StateFlow<SavedState> = dataStore.data.map { preferences ->
    preferences[SAVED_STATE_KEY]?.let { jsonString ->
      json.decodeFromString<ConcreteSavedState>(jsonString)
    } ?: InitialSavedState
  }.stateIn(
    scope = scope,
    started = SharingStarted.Eagerly,
    initialValue = InitialSavedState,
  )

  override suspend fun setJellyseerrAccount(account: JellyseerrAccount?) {
    dataStore.edit { preferences ->
      val currentState = preferences[SAVED_STATE_KEY]?.let { jsonString ->
        json.decodeFromString<ConcreteSavedState>(jsonString)
      } ?: ConcreteSavedState()

      val newState = currentState.copy(jellyseerr = account)
      preferences[SAVED_STATE_KEY] = json.encodeToString(newState)
    }
  }

  companion object {
    private val SAVED_STATE_KEY = stringPreferencesKey("saved_state")
  }
}

fun SavedState.isJellyseerrEnabled(): Boolean = jellyseerr != null
