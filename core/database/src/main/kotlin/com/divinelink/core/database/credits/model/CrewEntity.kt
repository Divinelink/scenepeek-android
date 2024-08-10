package com.divinelink.core.database.credits.model

data class CrewEntity(
  val id: Long,
  val creditId: String,
  val name: String,
  val originalName: String,
  val job: String,
  val profilePath: String?,
  val department: String,
  val knownForDepartment: String?,
  val totalEpisodeCount: Long,
  val aggregateCreditId: Long,
)
