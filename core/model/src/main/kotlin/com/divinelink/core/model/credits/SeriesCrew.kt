package com.divinelink.core.model.credits

data class SeriesCrew(
  val id: Int,
  val creditId: String?,
  val adult: Boolean,
  val name: String,
  val episodeCount: Int,
  val job: String?,
  val profilePath: String?,
  val department: String,
)
