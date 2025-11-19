package com.divinelink.core.model.user.data

import kotlinx.serialization.Serializable

@Serializable
enum class UserDataSection(val value: String) {
  Watchlist("watchlist"),
  Ratings("ratings"),
  ;

  companion object {
    fun from(value: String): UserDataSection = entries.first { it.value == value }
  }
}
