package com.divinelink.core.model

import kotlin.jvm.JvmInline

@JvmInline
value class Password(val value: String) {
  val isBlank get() = value.isBlank()
  companion object {
    val empty = Password("")
  }
}
