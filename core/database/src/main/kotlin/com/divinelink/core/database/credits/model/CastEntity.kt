package com.divinelink.core.database.credits.model

data class CastEntity(
  val id: Long,
  val name: String,
  val originalName: String,
  val profilePath: String?,
  val knownForDepartment: String?,
  val totalEpisodeCount: Long,
  val character: String,
  val gender: Long,
  val creditId: String,
)
