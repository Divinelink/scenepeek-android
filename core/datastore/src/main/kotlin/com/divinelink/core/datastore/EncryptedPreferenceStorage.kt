package com.divinelink.core.datastore

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore

interface EncryptedStorage {
  suspend fun clearSession()
  suspend fun setSessionId(sessionId: String)
  val sessionId: String?

  suspend fun clearAccessToken()
  suspend fun setAccessToken(accessToken: String)
  val accessToken: String?

  suspend fun clearTmdbAccountId()
  suspend fun setTmdbAccountId(accountId: String)
  val tmdbAccountId: String?

  suspend fun clearJellyseerrAuthCookie()
  suspend fun setJellyseerrAuthCookie(cookie: String)
  val jellyseerrAuthCookie: String?

  suspend fun clearJellyseerrPassword()
  suspend fun setJellyseerrPassword(password: String)
  val jellyseerrPassword: String?
}

class EncryptedPreferenceStorage(private val encryptedPreferences: SharedPreferences) :
  EncryptedStorage {

  object PreferencesKeys {
    const val SECRET_TMDB_SESSION_ID = "secret.tmdb.token.id"
    const val SECRET_JELLYSEERR_AUTH_COOKIE = "secret.jellyseerr.auth.cookie"
    const val SECRET_JELLYSEERR_PASSWORD = "secret.jellyseerr.password"

    const val SECRET_TMDB_ACCESS_TOKEN = "secret.tmdb.access.token"
    const val SECRET_TMDB_ACCOUNT_ID = "secret.tmdb.account.id"
  }

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

  override suspend fun clearAccessToken() {
    with(encryptedPreferences.edit()) {
      remove(PreferencesKeys.SECRET_TMDB_ACCESS_TOKEN)
      apply()
    }
  }

  override suspend fun setAccessToken(accessToken: String) {
    with(encryptedPreferences.edit()) {
      putString(PreferencesKeys.SECRET_TMDB_ACCESS_TOKEN, accessToken)
      apply()
    }
  }

  override val accessToken: String?
    get() = encryptedPreferences.getString(PreferencesKeys.SECRET_TMDB_ACCESS_TOKEN, null)

  override suspend fun clearTmdbAccountId() {
    with(encryptedPreferences.edit()) {
      remove(PreferencesKeys.SECRET_TMDB_ACCOUNT_ID)
      apply()
    }
  }

  override suspend fun setTmdbAccountId(accountId: String) {
    with(encryptedPreferences.edit()) {
      putString(PreferencesKeys.SECRET_TMDB_ACCOUNT_ID, accountId)
      apply()
    }
  }

  override val tmdbAccountId: String?
    get() = encryptedPreferences.getString(PreferencesKeys.SECRET_TMDB_ACCOUNT_ID, null)

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
}

/**
 * Completely destroys the keystore master key and encrypted shared preferences file. This will
 * cause all users to be logged out since the access and refresh tokens will be removed.
 *
 * This is not desirable and should only be called if we have completely failed to access our
 * encrypted shared preferences instance.
 */
fun destroyEncryptedSharedPreferencesAndRebuild(application: Application): SharedPreferences {
  // Delete the master key
  KeyStore.getInstance(KeyStore.getDefaultType()).run {
    load(null)
    deleteEntry(MasterKey.DEFAULT_MASTER_KEY_ALIAS)
  }
  // Deletes the encrypted shared preferences file
  application.deleteSharedPreferences(application.encryptedSharedPreferencesName)
  // Attempts to create the encrypted shared preferences instance
  return getEncryptedSharedPreferences(application = application)
}

/**
 * Helper method to get the app's encrypted shared preferences instance.
 */
fun getEncryptedSharedPreferences(application: Application): SharedPreferences =
  EncryptedSharedPreferences.create(
    application,
    application.encryptedSharedPreferencesName,
    MasterKey.Builder(application)
      .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
      .build(),
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
  )

/**
 * Helper method to get the app's encrypted shared preferences name.
 */
private val Application.encryptedSharedPreferencesName: String
  get() = "${packageName}_encrypted_preferences"
