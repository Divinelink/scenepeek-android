package com.divinelink.core.testing.factories.datastore.crypto

import com.divinelink.core.datastore.crypto.DataEncryptor
import com.divinelink.core.datastore.crypto.EncryptionSecretKey
import io.ktor.utils.io.core.toByteArray
import kotlin.io.encoding.Base64

/**
 * Mock implementation of [DataEncryptor]. Should only be used for testing purposes.
 */
class TestDataEncryptor : DataEncryptor {

  override suspend fun encrypt(
    secret: EncryptionSecretKey,
    text: String,
  ): String {
    val key = secret.name.hashCode().toByte()
    val encrypted = text.toByteArray().map { byte ->
      (byte.toInt() xor key.toInt()).toByte()
    }.toByteArray()

    return Base64.encode(encrypted)
  }

  override suspend fun decrypt(
    secret: EncryptionSecretKey,
    ciphertext: String,
  ): String {
    val key = secret.name.hashCode().toByte()
    val encrypted = Base64.decode(ciphertext)
    val decrypted = encrypted.map { byte ->
      (byte.toInt() xor key.toInt()).toByte()
    }.toByteArray()

    return decrypted.decodeToString()
  }
}
