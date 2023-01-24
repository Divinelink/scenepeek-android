package com.andreolas.movierama.base.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface EncryptedStorage {
    fun setTmdbApiKey(key: String)
    val tmdbApiKey: String
}

@Singleton
class EncryptedPreferenceStorage @Inject constructor(
    @ApplicationContext context: Context,
) : EncryptedStorage {

    var masterKey: MasterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private var encryptedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secret_shared_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

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
