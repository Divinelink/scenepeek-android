package com.divinelink.core.datastore.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import kotlinx.coroutines.flow.first
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.io.encoding.Base64

actual class EncryptionProvider(private val storage: KeystoreSecretsStorage) : DataEncryptor {

  companion object {
    private const val AES_TRANSFORMATION = "AES/GCM/NoPadding"
    private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
  }

  private val keyStore: KeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
    load(null)
  }

  init {
    EncryptionSecretKey.entries.forEach { secret ->
      initKey(secret.alias)
    }
  }

  private fun initKey(alias: String) {
    if (!keyStore.containsAlias(alias)) {
      KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER).apply {
        init(
          KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
          )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setUserAuthenticationRequired(false) // Set true for biometric lock
            .setRandomizedEncryptionRequired(true)
            .build(),
        )
        generateKey()
      }
    }
  }

  private fun getSecretKey(keyAlias: String): SecretKey =
    (keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry).secretKey

  override suspend fun encrypt(
    secret: EncryptionSecretKey,
    text: String,
  ): String {
    val encryptedData = encrypt(secret, text.encodeToByteArray())
    return Base64.encode(encryptedData)
  }

  override suspend fun decrypt(
    secret: EncryptionSecretKey,
    ciphertext: String,
  ): String {
    val encryptedData = Base64.decode(ciphertext)
    val decryptedData = decrypt(secret, encryptedData)

    return decryptedData.decodeToString()
  }

  private suspend fun encrypt(
    secret: EncryptionSecretKey,
    data: ByteArray,
  ): ByteArray {
    val cipher = Cipher.getInstance(AES_TRANSFORMATION).apply {
      init(Cipher.ENCRYPT_MODE, getSecretKey(secret.alias))
    }
    val iv = cipher.iv
    storage.setIvByteArray(secret, iv)

    return cipher.doFinal(data)
  }

  private suspend fun decrypt(
    secret: EncryptionSecretKey,
    encryptedData: ByteArray,
  ): ByteArray {
    val iv = storage.getIvByteArray(secret).first()

    if (iv.isEmpty()) error("IV not found")

    return Cipher.getInstance(AES_TRANSFORMATION).apply {
      init(Cipher.DECRYPT_MODE, getSecretKey(secret.alias), GCMParameterSpec(128, iv))
    }.doFinal(encryptedData)
  }
}
