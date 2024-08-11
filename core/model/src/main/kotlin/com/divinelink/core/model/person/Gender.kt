@file:Suppress("ktlint:standard:trailing-comma-on-declaration-site")

package com.divinelink.core.model.person

import com.divinelink.core.model.R

enum class Gender(
  val value: Int,
  val stringRes: Int,
) {
  NOT_SET(value = 0, stringRes = R.string.gender_not_set),
  FEMALE(value = 1, stringRes = R.string.gender_female),
  MALE(value = 2, stringRes = R.string.gender_male),
  NON_BINARY(value = 3, stringRes = R.string.gender_non_binary);

  companion object {
    fun from(value: Int?) = entries.find { it.value == value } ?: NOT_SET
  }
}
