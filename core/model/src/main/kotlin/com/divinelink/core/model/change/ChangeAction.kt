@file:Suppress("ktlint:standard:trailing-comma-on-declaration-site")

package com.divinelink.core.model.change

enum class ChangeAction(val value: String) {
  ADDED("added"),
  UPDATED("updated"),
  DELETED("deleted"),
  UNKNOWN("unknown");

  companion object {
    fun from(value: String): ChangeAction = entries.firstOrNull { it.value == value } ?: UNKNOWN
  }
}
