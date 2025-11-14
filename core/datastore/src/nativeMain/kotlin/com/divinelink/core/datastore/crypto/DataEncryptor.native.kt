package com.divinelink.core.datastore.crypto

actual class EncryptionProvider : DataEncryptor {
  override suspend fun encrypt(
    secret: EncryptionSecretKey,
    text: String,
  ): String {
    // TODO KMP Implement encryption logic
    return text
  }

  override suspend fun decrypt(
    secret: EncryptionSecretKey,
    ciphertext: String,
  ): String {
    // TODO KMP Implement decryption logic
    return ciphertext
  }
}
