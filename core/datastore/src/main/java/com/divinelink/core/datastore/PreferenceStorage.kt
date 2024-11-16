package com.divinelink.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_ACCOUNT_ID
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_BLACK_BACKGROUNDS
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_ENCRYPTED_SHARED_PREFS
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_HAS_SESSION
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_JELLYSEERR_ACCOUNT
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_JELLYSEERR_ADDRESS
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_JELLYSEERR_AUTH_METHOD
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_MATERIAL_YOU
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_REQUEST_TOKEN
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_THEME
import com.divinelink.core.datastore.DataStorePreferenceStorage.PreferencesKeys.PREF_SERIES_TOTAL_EPISODES_OBFUSCATION
import com.divinelink.core.designsystem.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber

interface PreferenceStorage {
  suspend fun selectTheme(theme: String)
  val selectedTheme: Flow<String>

  suspend fun setMaterialYou(isEnabled: Boolean)
  val isMaterialYouEnabled: Flow<Boolean>

  suspend fun setBlackBackgrounds(isEnabled: Boolean)
  val isBlackBackgroundsEnabled: Flow<Boolean>

  suspend fun clearAccountId()
  suspend fun setAccountId(accountId: String)
  val accountId: Flow<String?>

  suspend fun setEncryptedPreferences(value: String)
  val encryptedPreferences: Flow<String?>

  suspend fun setSpoilersObfuscation(isEnabled: Boolean)
  val spoilersObfuscation: Flow<Boolean>

  suspend fun clearToken()
  suspend fun setToken(token: String)
  val token: Flow<String?>

  suspend fun setHasSession(hasSession: Boolean)
  val hasSession: Flow<Boolean>

  suspend fun clearJellyseerrAddress()
  suspend fun setJellyseerrAddress(address: String)
  val jellyseerrAddress: Flow<String?>

  suspend fun clearJellyseerrAccount()
  suspend fun setJellyseerrAccount(account: String)
  val jellyseerrAccount: Flow<String?>

  suspend fun clearJellyseerrSignInMethod()
  suspend fun setJellyseerrAuthMethod(authMethod: String)
  val jellyseerrAuthMethod: Flow<String?>
}

class DataStorePreferenceStorage(private val dataStore: DataStore<Preferences>) :
  PreferenceStorage {

  companion object {
    const val PREFS_NAME = "application_preferences"
  }

  object PreferencesKeys {
    val PREF_ENCRYPTED_SHARED_PREFS = stringPreferencesKey("encrypted.shared.prefs")
    val PREF_SELECTED_THEME = stringPreferencesKey("settings.theme")
    val PREF_MATERIAL_YOU = booleanPreferencesKey("settings.material.you")
    val PREF_BLACK_BACKGROUNDS = booleanPreferencesKey("settings.black.backgrounds")

    val PREF_SERIES_TOTAL_EPISODES_OBFUSCATION = booleanPreferencesKey(
      "settings.series.episode.obfuscation",
    )

    val PREF_ACCOUNT_ID = stringPreferencesKey("account.id")

    val PREF_REQUEST_TOKEN = stringPreferencesKey("request.token")
    val PREF_HAS_SESSION = booleanPreferencesKey("user.has.valid.session")

    val PREF_JELLYSEERR_ADDRESS = stringPreferencesKey("jellyseerr.address")
    val PREF_JELLYSEERR_ACCOUNT = stringPreferencesKey("jellyseerr.account")
    val PREF_JELLYSEERR_AUTH_METHOD = stringPreferencesKey("jellyseerr.sign.in.method")
  }

  override suspend fun selectTheme(theme: String) {
    dataStore.edit {
      Timber.d("Set theme to $theme")
      it[PREF_SELECTED_THEME] = theme
    }
  }

  override val selectedTheme = dataStore.data.map {
    it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey
  }

  override suspend fun setMaterialYou(isEnabled: Boolean) {
    dataStore.edit {
      it[PREF_MATERIAL_YOU] = isEnabled
    }
  }

  override val isMaterialYouEnabled = dataStore.data.map {
    it[PREF_MATERIAL_YOU] ?: false
  }

  override suspend fun setBlackBackgrounds(isEnabled: Boolean) {
    dataStore.edit {
      it[PREF_BLACK_BACKGROUNDS] = isEnabled
    }
  }

  override val isBlackBackgroundsEnabled: Flow<Boolean> = dataStore.data.map {
    it[PREF_BLACK_BACKGROUNDS] ?: false
  }

  override suspend fun setEncryptedPreferences(value: String) {
    dataStore.edit {
      it[PREF_ENCRYPTED_SHARED_PREFS] = value
    }
  }

  override val encryptedPreferences = dataStore.data.map { preferences ->
    preferences[PREF_ENCRYPTED_SHARED_PREFS]
  }

  override suspend fun setSpoilersObfuscation(isEnabled: Boolean) {
    dataStore.edit {
      it[PREF_SERIES_TOTAL_EPISODES_OBFUSCATION] = isEnabled
    }
  }

  override val spoilersObfuscation = dataStore.data.mapLatest {
    it[PREF_SERIES_TOTAL_EPISODES_OBFUSCATION] ?: false
  }

  override suspend fun clearToken() {
    dataStore.edit {
      it.remove(PREF_REQUEST_TOKEN)
    }
  }

  override suspend fun setToken(token: String) {
    dataStore.edit {
      it[PREF_REQUEST_TOKEN] = token
    }
  }

  override val token = dataStore.data.map {
    val token = it[PREF_REQUEST_TOKEN]
    if (token.isNullOrEmpty()) {
      return@map null
    } else {
      token
    }
  }

  override suspend fun setHasSession(hasSession: Boolean) {
    dataStore.edit {
      it[PREF_HAS_SESSION] = hasSession
    }
  }

  override val hasSession: Flow<Boolean> = dataStore.data.map {
    it[PREF_HAS_SESSION] ?: false
  }.distinctUntilChanged()

  override suspend fun clearAccountId() {
    dataStore.edit {
      it.remove(PREF_ACCOUNT_ID)
    }
  }

  override suspend fun setAccountId(accountId: String) {
    dataStore.edit {
      it[PREF_ACCOUNT_ID] = accountId
    }
  }

  override val accountId: Flow<String?> = dataStore.data.map {
    it[PREF_ACCOUNT_ID]
  }

  override suspend fun clearJellyseerrAddress() {
    dataStore.edit {
      it.remove(PREF_JELLYSEERR_ADDRESS)
    }
  }

  override suspend fun setJellyseerrAddress(address: String) {
    dataStore.edit {
      it[PREF_JELLYSEERR_ADDRESS] = address
    }
  }

  override val jellyseerrAddress: Flow<String?> = dataStore.data.map {
    it[PREF_JELLYSEERR_ADDRESS]
  }

  override suspend fun clearJellyseerrAccount() {
    dataStore.edit {
      it.remove(PREF_JELLYSEERR_ACCOUNT)
    }
  }

  override suspend fun setJellyseerrAccount(account: String) {
    dataStore.edit {
      it[PREF_JELLYSEERR_ACCOUNT] = account
    }
  }

  override val jellyseerrAccount: Flow<String?> = dataStore.data.map {
    it[PREF_JELLYSEERR_ACCOUNT]
  }

  override suspend fun clearJellyseerrSignInMethod() {
    dataStore.edit {
      it.remove(PREF_JELLYSEERR_AUTH_METHOD)
    }
  }

  override suspend fun setJellyseerrAuthMethod(authMethod: String) {
    dataStore.edit {
      it[PREF_JELLYSEERR_AUTH_METHOD] = authMethod
    }
  }

  override val jellyseerrAuthMethod: Flow<String?> = dataStore.data.map {
    it[PREF_JELLYSEERR_AUTH_METHOD]
  }
}
