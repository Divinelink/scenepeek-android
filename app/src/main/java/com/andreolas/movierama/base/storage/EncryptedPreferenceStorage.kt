package com.andreolas.movierama.base.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

interface EncryptedStorage {
    suspend fun setTmdbAuthToken(key: String)
    val tmdbAuthToken: String
}

@Singleton
class EncryptedPreferenceStorage @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @ApplicationContext val context: Context,
) : EncryptedStorage {

    private var masterKey: MasterKey = MasterKey
        .Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Known issue: https://issuetracker.google.com/issues/158234058
    private val encryptedPreferences: SharedPreferences = EncryptedSharedPreferences(
        context,
        getFileName(),
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    object PreferencesKeys {
        const val SECRET_TMDB_AUTH_TOKEN = "secret.tmdb.auth.token"
    }

    override suspend fun setTmdbAuthToken(key: String) {
        with(encryptedPreferences.edit()) {
            putString(PreferencesKeys.SECRET_TMDB_AUTH_TOKEN, key)
            apply()
        }
    }

    override val tmdbAuthToken: String
        get() = encryptedPreferences.getString(PreferencesKeys.SECRET_TMDB_AUTH_TOKEN, "") ?: ""

    /**
     * Known issue: https://issuetracker.google.com/issues/158234058
     * This library makes the app crash when re-installing, so we have to make a by-pass for now until Google fixes it.
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
