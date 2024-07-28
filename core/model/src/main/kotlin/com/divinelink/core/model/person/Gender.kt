@file:Suppress("ktlint:standard:trailing-comma-on-declaration-site")

package com.divinelink.core.model.person

enum class Gender(val value: Int) {
  NOT_SET(0),
  FEMALE(1),
  MALE(2),
  NON_BINARY(3);

  companion object {
    fun from(value: Int) = entries.find { it.value == value } ?: NOT_SET
  }
}
