package com.divinelink.core.model

@JvmInline
value class Username(val value: String) {
  companion object {
    fun empty() = Username("")
  }
}
