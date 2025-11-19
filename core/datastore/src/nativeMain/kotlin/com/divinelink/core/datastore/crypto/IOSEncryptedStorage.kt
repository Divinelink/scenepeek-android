package com.divinelink.core.datastore.crypto

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.datastore.EncryptedStorage


class IOSEncryptedStorage(private val dataStore: DataStore<Preferences>) : EncryptedStorage {

  companion object {
    const val PREFS_NAME = "ui_preferences"
  }

  private object PreferencesKeys {
    val SESSION_ID = stringPreferencesKey("session_id")
    val ACCESS_TOKEN = stringPreferencesKey("accesstoken")
    val TMDB_ACCOUNT_ID = intPreferencesKey("tmdbaccountid")
  }

  override suspend fun clearSession() {

  }

  override suspend fun setSessionId(sessionId: String) {
    //
  }

  override val sessionId: String?
    get() = ""

  override suspend fun clearAccessToken() {

  }

  override suspend fun setAccessToken(accessToken: String) {

  }

  override val accessToken: String?
    get() = ""

  override suspend fun clearTmdbAccountId() {

  }

  override suspend fun setTmdbAccountId(accountId: String) {

  }

  override val tmdbAccountId: String?
    get() = ""
}
