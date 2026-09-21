package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class MediaExport(
  val id: Long,
  val posterPath: String?,
  val title: String,
  val type: String,
  val savedAt: String,
)
