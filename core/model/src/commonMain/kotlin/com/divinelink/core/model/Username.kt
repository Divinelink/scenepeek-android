package com.divinelink.core.model

import kotlin.jvm.JvmInline

@JvmInline
value class Username private constructor(private val username: String) {
  val value get() = username
  val isBlank get() = value.isBlank()
  val normalized get() = username.trim()

  companion object {
    fun from(value: String) = Username(value)
    val empty = Username("")
  }
}
