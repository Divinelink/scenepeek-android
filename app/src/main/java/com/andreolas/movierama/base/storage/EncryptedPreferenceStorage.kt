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
    fun setTmdbApiKey(key: String)
    val tmdbApiKey: String
}

@Singleton
class EncryptedPreferenceStorage @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @ApplicationContext val context: Context,
) : EncryptedStorage {

    private var masterKey: MasterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    // Known issue: https://issuetracker.google.com/issues/158234058
    // This library makes the app crash when re-installing, so we have to make a by-pass for now until Google fixes it.
    private fun preferences(): SharedPreferences = runBlocking {
        // TODO add this block when app runs for the first time
        val fileName = if (preferenceStorage.encryptedPreferences.first() == null) {
            val randomSecretFileName = UUID.randomUUID().toString()
            preferenceStorage.setEncryptedPreferences(randomSecretFileName)
            randomSecretFileName
        } else {
            preferenceStorage.encryptedPreferences.first()
        }
        EncryptedSharedPreferences(
            context,
            fileName!!,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    private var encryptedPreferences = preferences()

    object PreferencesKeys {
        const val SECRET_TMDB_API_KEY = "secret.tmdb.api.key"
    }

    override fun setTmdbApiKey(key: String) {
        with(encryptedPreferences.edit()) {
            putString(PreferencesKeys.SECRET_TMDB_API_KEY, key)
            apply()
        }
    }

    override val tmdbApiKey: String
        get() = encryptedPreferences.getString(PreferencesKeys.SECRET_TMDB_API_KEY, "") ?: ""
}
