package com.divinelink.core.model.jellyseerr

enum class SeerrAuthMethod(
  val endpoint: String,
  val displayName: String,
  val serverType: Int,
) {
  SEERR("local", "Seerr", 2),
  JELLYFIN("jellyfin", "Jellyfin", 3),
  EMBY("jellyfin", "Emby", 4),
  PLEX("plex", "Plex", -1),
  ;

  companion object {
    fun from(name: String): SeerrAuthMethod? = entries.find { it.name == name }
  }
}
