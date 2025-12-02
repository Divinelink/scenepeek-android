package com.divinelink.core.datastore.crypto

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.CryptographyProviderApi
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.providers.base.toByteArray
import dev.whyoleg.cryptography.providers.base.toNSData
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import kotlinx.coroutines.flow.first
import platform.CoreFoundation.CFDictionaryAddValue
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRefVar
import platform.CoreFoundation.kCFBooleanFalse
import platform.CoreFoundation.kCFBooleanTrue
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSMutableData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.Foundation.dataWithLength
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecDuplicateItem
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccessible
import platform.Security.kSecAttrAccessibleAfterFirstUnlock
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecMatchLimit
import platform.Security.kSecMatchLimitOne
import platform.Security.kSecRandomDefault
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

internal class IOSEncryptionProvider(private val storage: KeystoreSecretsStorage) : DataEncryptor {

  companion object {
    private const val KEY_SIZE = 32 // Bytes = 256 Bits
    private const val SERVICE_NAME = "com.divinelink.scenepeek.encryption"
  }

  private val aes = CryptographyProvider.Default.get(AES.GCM)

  init {
    EncryptionSecretKey.entries.forEach { secret ->
      initKey(secret.alias)
    }
  }

  private fun initKey(alias: String) {
    if (!keyExists(alias)) {
      generateKey(alias)
    }
  }

  @OptIn(ExperimentalForeignApi::class)
  private fun keyExists(alias: String): Boolean {
    val query = CFDictionaryCreateMutable(null, 4, null, null).apply {
      CFDictionaryAddValue(this, kSecClass, kSecClassGenericPassword)
      CFDictionaryAddValue(this, kSecAttrService, CFBridgingRetain(SERVICE_NAME))
      CFDictionaryAddValue(this, kSecAttrAccount, CFBridgingRetain(alias))
      CFDictionaryAddValue(this, kSecReturnData, kCFBooleanFalse)
    }

    val status = SecItemCopyMatching(query, null)
    CFRelease(query)

    return status == errSecSuccess
  }

  @OptIn(ExperimentalForeignApi::class)
  private fun generateKey(alias: String) {
    val keyData = NSMutableData.dataWithLength(KEY_SIZE.toULong())!!
    val status = SecRandomCopyBytes(kSecRandomDefault, KEY_SIZE.toULong(), keyData.mutableBytes)

    if (status != errSecSuccess) {
      error("Failed to generate random key. Status: $status")
    }

    val query = CFDictionaryCreateMutable(null, 5, null, null).apply {
      CFDictionaryAddValue(this, kSecClass, kSecClassGenericPassword)
      CFDictionaryAddValue(this, kSecAttrService, CFBridgingRetain(SERVICE_NAME))
      CFDictionaryAddValue(this, kSecAttrAccount, CFBridgingRetain(alias))
      CFDictionaryAddValue(this, kSecValueData, CFBridgingRetain(keyData))
      CFDictionaryAddValue(this, kSecAttrAccessible, kSecAttrAccessibleAfterFirstUnlock)
    }

    val addStatus = SecItemAdd(query, null)
    CFRelease(query)

    if (addStatus != errSecSuccess && addStatus != errSecDuplicateItem) {
      error("Failed to store key in Keychain. Status: $addStatus")
    }
  }

  @OptIn(ExperimentalForeignApi::class, CryptographyProviderApi::class)
  private fun getSecretKey(alias: String): ByteArray {
    val query = CFDictionaryCreateMutable(null, 5, null, null).apply {
      CFDictionaryAddValue(this, kSecClass, kSecClassGenericPassword)
      CFDictionaryAddValue(this, kSecAttrService, CFBridgingRetain(SERVICE_NAME))
      CFDictionaryAddValue(this, kSecAttrAccount, CFBridgingRetain(alias))
      CFDictionaryAddValue(this, kSecReturnData, kCFBooleanTrue)
      CFDictionaryAddValue(this, kSecMatchLimit, kSecMatchLimitOne)
    }

    memScoped {
      val result = alloc<CFTypeRefVar>()
      val status = SecItemCopyMatching(query, result.ptr)
      CFRelease(query)

      if (status != errSecSuccess) {
        error("Failed to retrieve key from Keychain. Status: $status")
      }

      val keyData = CFBridgingRelease(result.value) as NSData
      return keyData.toByteArray()
    }
  }

  @OptIn(CryptographyProviderApi::class)
  override suspend fun encrypt(
    secret: EncryptionSecretKey,
    text: String,
  ): String {
    val data = text.encodeToByteArray()
    val encryptedData = encrypt(secret, data)
    return encryptedData.toNSData().base64EncodedStringWithOptions(0u)
  }

  @OptIn(BetaInteropApi::class, CryptographyProviderApi::class)
  override suspend fun decrypt(
    secret: EncryptionSecretKey,
    ciphertext: String,
  ): String {
    val encryptedData = NSData.create(base64Encoding = ciphertext) ?: error("Invalid base64 string")
    val decryptedData = decrypt(secret, encryptedData.toByteArray())
    return decryptedData.decodeToString()
  }

  private suspend fun encrypt(
    secret: EncryptionSecretKey,
    data: ByteArray,
  ): ByteArray {
    val keyBytes = getSecretKey(secret.alias)

    val key = aes.keyDecoder().decodeFromByteArray(
      format = AES.Key.Format.RAW,
      bytes = keyBytes,
    )
    val cipher = key.cipher()
    val encryptedData = cipher.encrypt(data)

    val iv = encryptedData.copyOfRange(0, 12)
    storage.setIvByteArray(secret, iv)

    return encryptedData.copyOfRange(12, encryptedData.size)
  }

  private suspend fun decrypt(
    secret: EncryptionSecretKey,
    encryptedData: ByteArray,
  ): ByteArray {
    val iv = storage.getIvByteArray(secret).first()
    if (iv.isEmpty()) error("IV not found")

    val keyBytes = getSecretKey(secret.alias)
    val key = aes.keyDecoder().decodeFromByteArray(
      format = AES.Key.Format.RAW,
      bytes = keyBytes,
    )
    val cipher = key.cipher()
    val dataWithIv = iv + encryptedData

    return cipher.decrypt(dataWithIv)
  }
}
