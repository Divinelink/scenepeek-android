package com.divinelink.core.model

import kotlin.jvm.JvmInline

@JvmInline
value class Address private constructor(val address: String) {
  val value
    get() = address

  val normalized
    get() = address.trim()

  companion object {
    fun from(value: String) = Address(value)
    fun empty() = Address("")
  }
}
