package com.divinelink.core.model

@JvmInline
value class Username private constructor(private val username: String) {
  val value
    get() = username

  val normalized
    get() = username.trim()

  companion object {
    fun from(value: String) = Username(value)
    fun empty() = Username("")
  }
}
