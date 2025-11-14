@file:OptIn(ExperimentalUuidApi::class)

package com.divinelink.core.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.commons.util.JsonHelper
import com.divinelink.core.datastore.auth.SavedState.JellyseerrCredentials
import com.divinelink.core.datastore.crypto.DataEncryptor
import com.divinelink.core.datastore.crypto.EncryptionSecretKey
import com.divinelink.core.model.account.AccountDetails
import com.divinelink.core.model.jellyseerr.JellyseerrProfile
import com.divinelink.core.model.session.TmdbSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DataStoreSavedStateStorage(
  private val encryptor: DataEncryptor,
  private val dataStore: DataStore<Preferences>,
  private val json: JsonHelper,
  scope: CoroutineScope,
) : SavedStateStorage {

  override val savedState: StateFlow<SavedState> = dataStore.data.map { preferences ->
    preferences[SAVED_STATE_KEY]?.let { jsonString ->
      val decrypted = encryptor.decrypt(EncryptionSecretKey.SAVED_STATE_KEY, jsonString)
      json.decodeFromString<ConcreteSavedState>(decrypted)
    } ?: InitialSavedState
  }.stateIn(
    scope = scope,
    started = SharingStarted.Eagerly,
    initialValue = InitialSavedState,
  )

  override suspend fun setJellyseerrCredentials(credentials: JellyseerrCredentials) =
    updateState { state ->
      requireNotNull(state.selectedJellyseerrId) {
        "There's no connected jellyseerr credentials"
      }

      state.copy(
        jellyseerrCredentials = state.jellyseerrCredentials.plus(
          state.selectedJellyseerrId to credentials,
        ),
      )
    }

  override suspend fun setJellyseerrProfile(profile: JellyseerrProfile) = updateState { state ->
    requireNotNull(state.selectedJellyseerrId) {
      "There's no connected jellyseerr account"
    }

    state.copy(
      jellyseerrProfiles = state.jellyseerrProfiles.plus(
        state.selectedJellyseerrId to profile,
      ),
    )
  }

  override suspend fun setJellyseerrAuthCookie(cookie: String) {
    val accountId = Uuid.random().toHexDashString()
    updateState { state ->
      state.copy(
        selectedJellyseerrId = accountId,
        jellyseerrAuthCookies = state.jellyseerrAuthCookies.plus(accountId to cookie),
      )
    }
    // Suspend till cookie has been saved and is readable
    savedState.first { it.jellyseerrAuthCookies[accountId] != null }
  }

  override suspend fun clearSelectedJellyseerrAccount() = updateState { state ->
    val uuid = state.selectedJellyseerrId
    if (uuid == null) {
      state
    } else {
      val updatedCredentials = state.jellyseerrCredentials.minus(uuid)
      state.copy(
        jellyseerrAuthCookies = state.jellyseerrAuthCookies.minus(uuid),
        jellyseerrCredentials = updatedCredentials,
        jellyseerrProfiles = state.jellyseerrProfiles.minus(uuid),
        selectedJellyseerrId = updatedCredentials.keys.firstOrNull(),
      )
    }
  }


  override suspend fun setTMDBAccount(account: AccountDetails) = updateState { state ->
    state.copy(
      tmdbAccount = account,
    )
  }

  override suspend fun setTMDBSession(session: TmdbSession) = updateState { state ->
    state.copy(
      tmdbSession = session,
    )
  }

  override suspend fun clearTMDBSession() = updateState { state ->
    state.copy(
      tmdbSession = null,
      tmdbAccount = null,
    )
  }

  private suspend fun updateState(update: (ConcreteSavedState) -> ConcreteSavedState) {
    dataStore.edit { preferences ->
      val currentState = preferences[SAVED_STATE_KEY]?.let { encrypted ->
        val decrypted = encryptor.decrypt(
          secret = EncryptionSecretKey.SAVED_STATE_KEY,
          ciphertext = encrypted,
        )
        json.decodeFromString<ConcreteSavedState>(decrypted)
      } ?: InitialSavedState

      preferences[SAVED_STATE_KEY] = encryptor.encrypt(
        secret = EncryptionSecretKey.SAVED_STATE_KEY,
        text = json.encodeToString<ConcreteSavedState>(update(currentState)),
      )
    }
  }

  companion object {
    const val NAME = "savedState_preferences"
    private val SAVED_STATE_KEY = stringPreferencesKey("saved_state")
  }
}

val InitialSavedState: ConcreteSavedState = ConcreteSavedState(
  tmdbAccount = null,
  tmdbSession = null,
  jellyseerrCredentials = emptyMap(),
  jellyseerrProfiles = emptyMap(),
  jellyseerrAuthCookies = emptyMap(),
  selectedJellyseerrId = null,
)
