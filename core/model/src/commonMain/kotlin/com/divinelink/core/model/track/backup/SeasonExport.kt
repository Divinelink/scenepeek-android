package com.divinelink.core.model.track.backup

import kotlinx.serialization.Serializable

@Serializable
data class SeasonExport(
  val id: Long,
  val showId: Long,
  val name: String,
  val airDate: String?,
  val runtime: String?,
  val number: Int,
  val seasonNumber: Int,
  val posterPath: String?,
)
