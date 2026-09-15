package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class EpisodeExport(
  val id: Int,
  val showId: Long,
  val name: String,
  val airDate: String?,
  val runtime: String?,
  val number: Int,
  val seasonNumber: Int,
  val stillPath: String?,
)
