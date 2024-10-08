package com.divinelink.core.datastore

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.UUID

interface EncryptedStorage {
  suspend fun setTmdbAuthToken(key: String)
  val tmdbAuthToken: String

  suspend fun clearSession()
  suspend fun setSessionId(sessionId: String)
  val sessionId: String?

  suspend fun clearJellyseerrAuthCookie()
  suspend fun setJellyseerrAuthCookie(cookie: String)
  val jellyseerrAuthCookie: String?

  suspend fun clearJellyseerrPassword()
  suspend fun setJellyseerrPassword(password: String)
  val jellyseerrPassword: String?
}

class EncryptedPreferenceStorage(
  private val preferenceStorage: PreferenceStorage,
  context: Context,
) : EncryptedStorage {

  private var masterKey: MasterKey = MasterKey
    .Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

  // Known issue: https://issuetracker.google.com/issues/158234058
  private val encryptedPreferences: SharedPreferences = EncryptedSharedPreferences(
    context = context,
    fileName = getFileName(),
    masterKey = masterKey,
    prefKeyEncryptionScheme = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    prefValueEncryptionScheme = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
  )

  object PreferencesKeys {
    const val SECRET_TMDB_AUTH_TOKEN = "secret.tmdb.auth.token"
    const val SECRET_TMDB_SESSION_ID = "secret.tmdb.token.id"
    const val SECRET_JELLYSEERR_AUTH_COOKIE = "secret.jellyseerr.auth.cookie"
    const val SECRET_JELLYSEERR_PASSWORD = "secret.jellyseerr.password"
  }

  override suspend fun setTmdbAuthToken(key: String) {
    with(encryptedPreferences.edit()) {
      putString(PreferencesKeys.SECRET_TMDB_AUTH_TOKEN, key)
      apply()
    }
  }

  override val tmdbAuthToken: String
    get() = encryptedPreferences.getString(PreferencesKeys.SECRET_TMDB_AUTH_TOKEN, "") ?: ""

  override suspend fun setSessionId(sessionId: String) {
    with(encryptedPreferences.edit()) {
      putString(PreferencesKeys.SECRET_TMDB_SESSION_ID, sessionId)
      apply()
    }
  }

  override suspend fun clearSession() {
    with(encryptedPreferences.edit()) {
      remove(PreferencesKeys.SECRET_TMDB_SESSION_ID)
      apply()
    }
  }

  override val sessionId: String?
    get() = encryptedPreferences.getString(PreferencesKeys.SECRET_TMDB_SESSION_ID, null)

  override suspend fun clearJellyseerrAuthCookie() {
    with(encryptedPreferences.edit()) {
      remove(PreferencesKeys.SECRET_JELLYSEERR_AUTH_COOKIE)
      apply()
    }
  }

  override suspend fun setJellyseerrAuthCookie(cookie: String) {
    with(encryptedPreferences.edit()) {
      putString(PreferencesKeys.SECRET_JELLYSEERR_AUTH_COOKIE, cookie)
      apply()
    }
  }

  override val jellyseerrAuthCookie: String?
    get() = encryptedPreferences.getString(PreferencesKeys.SECRET_JELLYSEERR_AUTH_COOKIE, null)

  override suspend fun clearJellyseerrPassword() {
    with(encryptedPreferences.edit()) {
      remove(PreferencesKeys.SECRET_JELLYSEERR_PASSWORD)
      apply()
    }
  }

  override suspend fun setJellyseerrPassword(password: String) {
    with(encryptedPreferences.edit()) {
      putString(PreferencesKeys.SECRET_JELLYSEERR_PASSWORD, password)
      apply()
    }
  }

  override val jellyseerrPassword: String?
    get() = encryptedPreferences.getString(PreferencesKeys.SECRET_JELLYSEERR_PASSWORD, null)

  /**
   * Known issue: https://issuetracker.google.com/issues/158234058
   * This library makes the app crash when re-installing,
   * so we have to make a by-pass for now until Google fixes it.
   *
   * Therefore we have to create a new encryption file name in case the current one is not available
   * and save that to our data store preferences.
   */
  private fun getFileName(): String = runBlocking {
    val encryptedFileName = preferenceStorage.encryptedPreferences.first()
    if (encryptedFileName == null) {
      val newEncryptedFileName = UUID.randomUUID().toString() + ".encrypted.secrets"
      preferenceStorage.setEncryptedPreferences(newEncryptedFileName)
      return@runBlocking newEncryptedFileName
    } else {
      preferenceStorage.setEncryptedPreferences(encryptedFileName)
      return@runBlocking encryptedFileName
    }
  }
}
