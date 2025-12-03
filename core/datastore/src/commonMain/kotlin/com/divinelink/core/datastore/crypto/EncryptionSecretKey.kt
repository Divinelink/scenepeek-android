package com.divinelink.core.datastore.crypto

enum class EncryptionSecretKey(val alias: String) {
  SAVED_STATE_KEY("saved_state_key"),
  ;

  companion object {
    fun from(alias: String): EncryptionSecretKey = entries.firstOrNull { it.alias == alias }
      ?: throw IllegalArgumentException("No enum constant for alias: $alias")
  }
}
