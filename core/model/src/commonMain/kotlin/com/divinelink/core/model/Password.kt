package com.divinelink.core.model

@JvmInline
value class Password(val value: String) {
  companion object {
    fun empty() = Password("")
  }
}
