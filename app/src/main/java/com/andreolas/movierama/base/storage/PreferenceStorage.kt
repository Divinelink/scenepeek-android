package com.andreolas.movierama.base.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.andreolas.movierama.base.storage.DataStorePreferenceStorage.PreferencesKeys.PREF_ENCRYPTED_SHARED_PREFS
import com.andreolas.movierama.base.storage.DataStorePreferenceStorage.PreferencesKeys.PREF_SELECTED_THEME
import com.andreolas.movierama.ui.theme.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

interface PreferenceStorage {
    suspend fun selectTheme(theme: String)
    val selectedTheme: Flow<String>

    suspend fun setEncryptedPreferences(value: String)
    val encryptedPreferences: Flow<String?>
}

@Singleton
class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : PreferenceStorage {
    companion object {
        const val PREFS_NAME = "application_preferences"
    }

    object PreferencesKeys {
        val PREF_ENCRYPTED_SHARED_PREFS = stringPreferencesKey("encrypted.shared.prefs")
        val PREF_SELECTED_THEME = stringPreferencesKey("settings.theme")
    }

    override suspend fun selectTheme(theme: String) {
        dataStore.edit {
            Timber.d("Set theme to $theme")
            it[PREF_SELECTED_THEME] = theme
        }
    }

    override val selectedTheme =
        dataStore.data.map {
            it[PREF_SELECTED_THEME] ?: Theme.SYSTEM.storageKey
        }

    override suspend fun setEncryptedPreferences(value: String) {
        dataStore.edit {
            it[PREF_ENCRYPTED_SHARED_PREFS] = value
        }
    }

    override val encryptedPreferences =
        dataStore.data.map {
            it[PREF_ENCRYPTED_SHARED_PREFS]
        }
}
