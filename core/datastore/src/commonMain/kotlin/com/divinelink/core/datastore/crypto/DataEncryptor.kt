package com.divinelink.core.datastore.crypto

interface DataEncryptor {
  suspend fun encrypt(
    secret: EncryptionSecretKey,
    text: String,
  ): String

  suspend fun decrypt(
    secret: EncryptionSecretKey,
    ciphertext: String,
  ): String
}

expect class EncryptionProvider : DataEncryptor
