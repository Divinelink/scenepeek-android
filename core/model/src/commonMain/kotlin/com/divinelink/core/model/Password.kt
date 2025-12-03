package com.divinelink.core.model

import kotlin.jvm.JvmInline

@JvmInline
value class Password(val value: String) {
  companion object {
    fun empty() = Password("")
  }
}
