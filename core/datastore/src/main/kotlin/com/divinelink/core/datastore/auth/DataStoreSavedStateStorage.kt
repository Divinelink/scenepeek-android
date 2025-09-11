@file:OptIn(ExperimentalUuidApi::class)

package com.divinelink.core.datastore.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.commons.util.JsonHelper
import com.divinelink.core.datastore.auth.SavedState.JellyseerrAccount
import com.divinelink.core.datastore.crypto.DataEncryptor
import com.divinelink.core.datastore.crypto.EncryptionSecretKey
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

  override suspend fun setJellyseerrAccount(account: JellyseerrAccount) = updateState { state ->
    requireNotNull(state.selectedJellyseerrAccountId) {
      "There's no connected jellyseerr account"
    }

    state.copy(
      jellyseerrAccounts = state.jellyseerrAccounts.plus(
        state.selectedJellyseerrAccountId to account,
      ),
    )
  }

  override suspend fun setJellyseerrAuthCookie(cookie: String) {
    val accountId = Uuid.random().toHexDashString()
    updateState { state ->
      state.copy(
        selectedJellyseerrAccountId = accountId,
        jellyseerrAuthCookies = state.jellyseerrAuthCookies.plus(accountId to cookie),
      )
    }
    // Suspend till cookie has been saved and is readable
    savedState.first { it.jellyseerrAuthCookies[accountId] != null }
  }

  override suspend fun clearSelectedJellyseerrAccount() = updateState { state ->
    val uuid = state.selectedJellyseerrAccountId
    if (uuid == null) {
      state
    } else {
      state.copy(
        jellyseerrAuthCookies = state.jellyseerrAuthCookies.minus(uuid),
        jellyseerrAccounts = state.jellyseerrAccounts.minus(uuid),
        selectedJellyseerrAccountId = state.jellyseerrAccounts.keys.minus(uuid).firstOrNull(),
      )
    }
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
  jellyseerrAccounts = emptyMap(),
  jellyseerrAuthCookies = emptyMap(),
  selectedJellyseerrAccountId = null,
)

fun SavedState.isJellyseerrEnabled(): Boolean = jellyseerrAccounts.keys.isNotEmpty()
