package com.divinelink.core.model

import kotlin.jvm.JvmInline

@JvmInline
value class AuthToken(val value: String) {
  val isBlank get() = value.isBlank()
  val normalized get() = value.trim()

  companion object {
    val empty = AuthToken("")
  }
}
