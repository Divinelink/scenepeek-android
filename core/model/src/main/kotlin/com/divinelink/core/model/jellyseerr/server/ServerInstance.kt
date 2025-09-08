package com.divinelink.core.model.jellyseerr.server

import kotlinx.serialization.Serializable

sealed class ServerInstance {
  abstract val id: Int
  abstract val name: String
  abstract val is4k: Boolean
  abstract val isDefault: Boolean
  abstract val activeDirectory: String
  abstract val activeProfileId: Int

  @Serializable
  data class Sonarr(
    override val id: Int,
    override val name: String,
    override val is4k: Boolean,
    override val isDefault: Boolean,
    override val activeDirectory: String,
    override val activeProfileId: Int,
  ) : ServerInstance()

  @Serializable
  data class Radarr(
    override val id: Int,
    override val name: String,
    override val is4k: Boolean,
    override val isDefault: Boolean,
    override val activeDirectory: String,
    override val activeProfileId: Int,
  ) : ServerInstance()
}
