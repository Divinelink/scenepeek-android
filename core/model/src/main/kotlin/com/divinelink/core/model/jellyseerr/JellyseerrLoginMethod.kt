@file:Suppress("ktlint:standard:trailing-comma-on-declaration-site")

package com.divinelink.core.model.jellyseerr

enum class JellyseerrLoginMethod(val endpoint: String) {
  JELLYFIN("jellyfin"),
  JELLYSEERR("local");

  companion object {
    fun from(name: String): JellyseerrLoginMethod? = entries.find { it.name == name }
  }
}
