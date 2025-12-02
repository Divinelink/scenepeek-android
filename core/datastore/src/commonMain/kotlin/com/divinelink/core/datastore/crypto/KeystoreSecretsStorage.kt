package com.divinelink.core.datastore.crypto

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.io.encoding.Base64

/**
 * Interface for storing auto-generated initialization vectors (IV) for encrypting/decrypting
 * tokens. IVs are randomly generated from the [javax.crypto.Cipher] and are needed for the
 * encryption/decryption process.
 */
interface KeystoreSecretsStorage {
  suspend fun setIvByteArray(
    secret: EncryptionSecretKey,
    iv: ByteArray,
  )

  fun getIvByteArray(secret: EncryptionSecretKey): Flow<ByteArray>
}

class DataStoreKeystoreSecretsStorage(private val dataStore: DataStore<Preferences>) :
  KeystoreSecretsStorage {

  companion object {
    const val PREFS_NAME = "secrets_preferences"

    fun ivPreference(secret: EncryptionSecretKey) = stringPreferencesKey(
      "keystore_${secret.alias}_iv",
    )
  }

  override suspend fun setIvByteArray(
    secret: EncryptionSecretKey,
    iv: ByteArray,
  ) {
    dataStore.edit { it[ivPreference(secret)] = Base64.encode(iv) }
  }

  override fun getIvByteArray(secret: EncryptionSecretKey): Flow<ByteArray> =
    dataStore.data.map { prefs ->
      prefs[ivPreference(secret)]?.let { secretPreferenceKey ->
        if (secretPreferenceKey.isEmpty()) {
          byteArrayOf()
        } else {
          Base64.decode(secretPreferenceKey)
        }
      } ?: byteArrayOf()
    }
}
