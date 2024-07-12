package com.divinelink.core.model.credits

data class SeriesCast(
  val id: Int,
  val adult: Boolean,
  val name: String,
  val totalEpisodes: Int,
  val character: String?,
  val profilePath: String?,
)
