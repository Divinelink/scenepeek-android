package com.divinelink.core.model.jellyseerr

enum class JellyseerrAuthMethod(
  val endpoint: String,
  val displayName: String,
  val serverType: Int,
) {
  JELLYSEERR("local", "Jellyseerr", 2),
  JELLYFIN("jellyfin", "Jellyfin", 3),
  EMBY("jellyfin", "Emby", 4),
  ;

  companion object {
    fun from(name: String): JellyseerrAuthMethod? = entries.find { it.name == name }
  }
}
