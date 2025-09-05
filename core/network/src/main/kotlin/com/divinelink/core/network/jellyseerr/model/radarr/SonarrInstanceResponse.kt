package com.divinelink.core.network.jellyseerr.model.radarr

import kotlinx.serialization.Serializable

@Serializable
data class SonarrInstanceResponse(
  val id: Int,
  val name: String,
  val is4k: Boolean,
  val isDefault: Boolean,
  val activeDirectory: String,
  val activeProfileId: Int,
  val activeAnimeProfileId: Int? = null,
  val activeAnimeDirectory: String? = null,
  val activeLanguageProfileId: Int? = null,
  val activeAnimeLanguageProfileId: Int? = null,
)
